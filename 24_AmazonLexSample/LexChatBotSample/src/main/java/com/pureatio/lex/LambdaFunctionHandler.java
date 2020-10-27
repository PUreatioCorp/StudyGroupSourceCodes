package com.pureatio.lex;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pureatio.lex.dto.address.AddressResultDto;
import com.pureatio.lex.dto.lex.in.LexInputDto;
import com.pureatio.lex.dto.lex.out.DialogActionDto;
import com.pureatio.lex.dto.lex.out.LexResultDto;
import com.pureatio.lex.dto.lex.out.MessageDto;
import com.pureatio.lex.dto.service.ServiceResultDto;
import com.pureatio.lex.service.ZipService;

/**
 * AWS LexのLambda関数
 */
public class LambdaFunctionHandler implements RequestHandler<Object, Object> {

	// JSON用マッパー
	private static ObjectMapper jsonMapper = new ObjectMapper();

	/**
	 * @see RequestHandler#handleRequest(Object, Context)
	 */
	@Override
	public Object handleRequest(Object input, Context context) {
		context.getLogger().log("Input: " + input);
		LexResultDto resultDto = new LexResultDto();

		try {
			LexInputDto inputDto = jsonMapper.readValue(jsonMapper.writeValueAsString(input), LexInputDto.class);
			resultDto.setSessionAttributes(inputDto.getSessionAttributes());

			// 郵便番号から住所を取得する。
			String zipCode = inputDto.getCurrentIntent().getSlots().get("zipCode").toString();
			ServiceResultDto<AddressResultDto> addressDto = ZipService.getAddress(zipCode);

			DialogActionDto dialogActionDto = new DialogActionDto();
			if (addressDto.isSuccess()) {
				MessageDto messageDto = new MessageDto();
				dialogActionDto.setFulfillmentState("Fulfilled");
				messageDto.setContent(addressDto.getValue().getResult().get(0).getAddress());
				dialogActionDto.setMessage(messageDto);
			} else {
				dialogActionDto.setFulfillmentState("Failed");
				MessageDto messageDto = new MessageDto();
				if (StringUtils.isNullOrEmpty(addressDto.getErrorReason().trim())) {
					messageDto.setContent("住所の取得に失敗しました。");
				} else {
					messageDto.setContent(addressDto.getErrorReason());
				}
				dialogActionDto.setMessage(messageDto);
			}

			resultDto.setDialogAction(dialogActionDto);
			return resultDto;
		} catch (Exception e) {
			DialogActionDto dialogActionDto = new DialogActionDto();
			dialogActionDto.setFulfillmentState("Failed");
			MessageDto messageDto = new MessageDto();
			messageDto.setContent("Cause Exception : " + e.getMessage());
			dialogActionDto.setMessage(messageDto);
			resultDto.setDialogAction(dialogActionDto);
			return resultDto;
		}
	}
}
