package com.pureatio.handler;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.pureatio.dto.line.in.EventDto;
import com.pureatio.dto.line.in.ImageMessageDto;
import com.pureatio.dto.line.in.LineInputDto;
import com.pureatio.dto.line.in.TextMessageDto;
import com.pureatio.dto.service.ServiceResultDto;
import com.pureatio.service.AwsService;
import com.pureatio.service.DatabaseService;
import com.pureatio.service.LineService;
import com.pureatio.service.QRService;

public class LambdaFunctionHandler implements RequestHandler<Object, String> {

	/** QRコードをアップロードするURLベース */
	private static final String QRCODE_URL_BASE = "QRCODE_URL_BASE";

	// Json変換用Mapper
	private static ObjectMapper jsonMapper = new ObjectMapper();
	// 日付フォーマッタ
	private static DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.JAPAN);

	@Override
	public String handleRequest(Object input, Context context) {

		context.getLogger().log("Input: " + input);
		String userId = null;
		Path qrCodePath = null;

		try {
			// Mapで取得するため、一度文字列にしてからDTOに変換する
			LineInputDto inputDto = jsonMapper.readValue(jsonMapper.writeValueAsString(input), LineInputDto.class);

			// メッセージ以外は何も行わない
			EventDto eventDto = inputDto.getEvents().get(0);
			userId = eventDto.getSource().getUserId();
			if (!"message".equals(eventDto.getType())) {
				return String.format("It not message event: %s", inputDto.getEvents().get(0).getType());
			}

			Map<String, Object> messageMap = eventDto.getMessage();
			String messageString = jsonMapper.writeValueAsString(messageMap);
			String messageType = messageMap.get("type").toString();
			// テキストメッセージの場合
			if ("text".equals(messageType)) {
				TextMessageDto textMessage = jsonMapper.readValue(messageString, TextMessageDto.class);
				
				// データを登録する。
				int refCount = DatabaseService.insertQRContents(textMessage.getText());
				context.getLogger().log(String.format("reflection count: %d", refCount));

				// テキストからQRコードを作成する。
				BitMatrix qrMatrix = QRService.getQRCodeMatrix(textMessage.getText());
				qrCodePath = Paths.get(String.format("/tmp/%s.png", formatter.format(new Date())));
				MatrixToImageWriter.writeToPath(qrMatrix, "png", qrCodePath);
				context.getLogger().log(String.format("Create QRCode File: %s", qrCodePath.toString()));

				// S3にputする。
				AwsService.putToS3(qrCodePath);

				// S3にputした画像をLINEに送信する。
				String qrcodeUrl = String.format(System.getenv(QRCODE_URL_BASE), qrCodePath.getFileName());
				String qrImageJson = LineService.getPushImageMessageRequestData(userId, qrcodeUrl, qrcodeUrl);
				ServiceResultDto<Map<String, String>> qrImageResult = LineService.post(LineService.PUSH_MESSAGE_URL,
						qrImageJson);
				if (!qrImageResult.isSuccess()) {
					String qrImageErrorMessage = String.format("%d: %s", qrImageResult.getStatusCode(),
							qrImageResult.getErrorReason());
					context.getLogger().log(qrImageErrorMessage);
					return qrImageErrorMessage;
				}
			} else if ("image".equals(messageType)) {
				// 画像メッセージの場合
				ImageMessageDto imageMessage = jsonMapper.readValue(messageString, ImageMessageDto.class);

				// 送信された画像を取得する。
				String contentUrl = String.format(LineService.GET_CONTENT_URL, imageMessage.getId());
				context.getLogger().log(String.format("Access contents url : %s", contentUrl));

				ServiceResultDto<BufferedImage> imageContentResult = LineService.get(contentUrl);
				if (!imageContentResult.isSuccess()) {
					String imageContentErrorMessage = String.format("%d: %s", imageContentResult.getStatusCode(),
							imageContentResult.getErrorReason());
					context.getLogger().log(imageContentErrorMessage);
					return imageContentErrorMessage;
				}

				String qrText = QRService.getQRContents(imageContentResult.getValue());
				context.getLogger().log(String.format("QRCode Contents: %s", qrText));

				// データを登録する。
				int refCount = DatabaseService.insertQRContents(qrText);
				context.getLogger().log(String.format("reflection count: %d", refCount));

				// 返信用のメッセージを取得し、LINEに対して返信を行う。
				String reqTextMessageJson = null;
				if (qrText.startsWith("http")) {
					reqTextMessageJson = LineService.getPushFlexMessageRequestData(userId, qrText);
				} else {
					reqTextMessageJson = LineService.getPushTextMessageRequestData(userId, qrText);
				}
				ServiceResultDto<Map<String, String>> qrTextResult = LineService.post(LineService.PUSH_MESSAGE_URL,
						reqTextMessageJson);
				if (!qrTextResult.isSuccess()) {
					String qrTextErrorMessage = String.format("%d: %s", qrTextResult.getStatusCode(),
							qrTextResult.getErrorReason());
					context.getLogger().log(qrTextErrorMessage);
					return qrTextErrorMessage;
				}
			}
		} catch (Exception e) {
			context.getLogger().log(e.getMessage());
			for (StackTraceElement errorElm : e.getStackTrace()) {
				context.getLogger().log(errorElm.toString());
			}

			try {
				// 例外が発生した場合には、エラーメッセージを送信する。
				LineService.sendErrorPushMessage(userId);
			} catch (Exception e1) {
				context.getLogger().log(e1.getMessage());
				for (StackTraceElement errorElm : e1.getStackTrace()) {
					context.getLogger().log(errorElm.toString());
				}
			}
		} finally {
			// 作成したQRコードを削除する。
			if (qrCodePath != null) {
				try {
					Files.delete(qrCodePath);
				} catch (IOException e) {
					context.getLogger().log(e.getMessage());
					for (StackTraceElement errorElm : e.getStackTrace()) {
						context.getLogger().log(errorElm.toString());
					}
				}
			}
		}

		return "Send QRCode Contents To Line.... Success!";
	}
}