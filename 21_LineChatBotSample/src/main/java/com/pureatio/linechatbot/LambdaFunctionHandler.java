package com.pureatio.linechatbot;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pureatio.linechatbot.dto.address.AddressContentDto;
import com.pureatio.linechatbot.dto.address.AddressResultDto;
import com.pureatio.linechatbot.dto.line.in.EventDto;
import com.pureatio.linechatbot.dto.line.in.LineInputDto;
import com.pureatio.linechatbot.dto.service.ServiceResultDto;
import com.pureatio.linechatbot.service.LineService;
import com.pureatio.linechatbot.service.ZipService;

public class LambdaFunctionHandler implements RequestHandler<Object, String> {

	// Json変換用Mapper
	private static ObjectMapper jsonMapper = new ObjectMapper();

	@Override
	public String handleRequest(Object input, Context context) {
		context.getLogger().log("Input: " + input);

		try {
			// Mapで取得するため、一度文字列にしてからDTOに変換する
			LineInputDto inputDto = jsonMapper.readValue(jsonMapper.writeValueAsString(input), LineInputDto.class);

			// メッセージ以外は何も行わない
			EventDto eventDto = inputDto.getEvents().get(0);
			if (!"message".equals(eventDto.getType())) {
				return String.format("It not message event: %s", inputDto.getEvents().get(0).getType());
			}

			// 入力内容の郵便番号から住所を取得する。
			ServiceResultDto<AddressResultDto> addressDto = ZipService.getAddress(eventDto.getMessage().getText());
			if (!addressDto.isSuccess()) {
				return String.format("%d: %s", addressDto.getStatusCode(), addressDto.getErrorReason());
			}

			AddressContentDto addressContent = addressDto.getValue().getResult().get(0);
			context.getLogger().log(String.format("%s(%s)", addressContent.getAddress(), addressContent.getKana()));

			// 返却用のJsonを構成する。
			String lineMessageJson = LineService.getPushMessageRequestData(eventDto.getSource().getUserId(),
					String.format("%s(%s)", addressContent.getAddress(), addressContent.getKana()));
			context.getLogger().log(lineMessageJson);
			ServiceResultDto<Map<String, String>> lineResult = LineService.post(LineService.PUSH_MESSAGE_URL,
					lineMessageJson);
			if (!lineResult.isSuccess()) {
				return String.format("%d: %s", lineResult.getStatusCode(), lineResult.getErrorReason());
			}
		} catch (Exception e) {
			context.getLogger().log(e.getMessage());
		}

		return "Send Address To Line.... Success!";
	}

}
