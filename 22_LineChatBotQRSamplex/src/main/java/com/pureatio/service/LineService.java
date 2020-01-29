package com.pureatio.service;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pureatio.dto.line.out.common.ErrorMessageDto;
import com.pureatio.dto.line.out.common.FlexBubbleMessageDto;
import com.pureatio.dto.line.out.common.FlexMessageDto;
import com.pureatio.dto.line.out.common.ImageMessageDto;
import com.pureatio.dto.line.out.common.TextMessageDto;
import com.pureatio.dto.line.out.common.flex.FlexTextContentDto;
import com.pureatio.dto.line.out.common.flex.VerticalBoxTypeDto;
import com.pureatio.dto.line.out.push.PushMessageDto;
import com.pureatio.dto.service.ServiceResultDto;

/**
 * LINE用サービス
 */
public class LineService {

	/** コンテンツ取得用URL */
	public static final String GET_CONTENT_URL = "https://api-data.line.me/v2/bot/message/%s/content";
	/** Pushメッセージ用URL */
	public static final String PUSH_MESSAGE_URL = "https://api.line.me/v2/bot/message/push";

	/** LINEのAPIトークン */
	private static final String LINE_ACCESS_TOKEN = "LINE_ACCESS_TOKEN";
	// Json変換用Mapper
	private static ObjectMapper jsonMapper = new ObjectMapper();

	/**
	 * LINE Messaging APIを利用したPOST通信を行う<br>
	 * 返却値として取得したStreamを変換せずに返却する。
	 * 
	 * @param url 接続URL
	 * @return Stream
	 * @throws Exception
	 */
	public static ServiceResultDto<BufferedImage> get(String url) throws Exception {

		ServiceResultDto<BufferedImage> result = new ServiceResultDto<>();

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			// 通信用のリクエストを設定する。
			HttpGet request = new HttpGet(url);
			request.addHeader("Authorization", String.format("Bearer %s", System.getenv(LINE_ACCESS_TOKEN)));

			// Messaging APIを利用して通信する。
			try (CloseableHttpResponse response = client.execute(request)) {
				int statusCode = response.getStatusLine().getStatusCode();
				result.setStatusCode(statusCode);

				if (HttpStatus.SC_OK == statusCode) {
					result.setSuccess(true);
					BufferedImage bufImage = ImageIO.read(response.getEntity().getContent());
					result.setValue(bufImage);
				} else {
					result.setErrorReason(response.getStatusLine().getReasonPhrase());
				}
			}
		}

		return result;
	}

	/**
	 * LINE Messaging APIを利用したPOST通信を行う
	 * 
	 * @param url          接続URL
	 * @param postDataJson POSTデータ
	 * @return 処理結果
	 * @throws Exception
	 */
	public static ServiceResultDto<Map<String, String>> post(String url, String postDataJson) throws Exception {

		ServiceResultDto<Map<String, String>> result = new ServiceResultDto<>();

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			// 通信用のリクエストを設定する。
			HttpPost request = new HttpPost(url);
			request.addHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
			request.addHeader("Authorization", String.format("Bearer %s", System.getenv(LINE_ACCESS_TOKEN)));
			request.setEntity(new StringEntity(postDataJson, ContentType.APPLICATION_JSON));

			// Messaging APIを利用して通信する。
			try (CloseableHttpResponse response = client.execute(request)) {
				int statusCode = response.getStatusLine().getStatusCode();
				result.setStatusCode(statusCode);

				if (HttpStatus.SC_OK == statusCode) {
					result.setSuccess(true);
					Map<String, String> apiResultMap = jsonMapper.readValue(response.getEntity().getContent(),
							new TypeReference<Map<String, String>>() {
							});
					result.setValue(apiResultMap);
				} else {
					ErrorMessageDto errorDto = jsonMapper.readValue(response.getEntity().getContent(),
							ErrorMessageDto.class);
					result.setErrorReason(String.format("%s%s%s", response.getStatusLine().getReasonPhrase(),
							System.lineSeparator(), errorDto.getMessage()));
				}
			}
		}

		return result;
	}

	/**
	 * 例外発生時にメッセージを送信する
	 * 
	 * @param userId ユーザーID
	 * @throws Exception
	 */
	public static void sendErrorPushMessage(String userId) throws Exception {

		String errorMessageJson = getPushTextMessageRequestData(userId, "エラーが発生しました。詳細は開発元にお問い合わせください。");

		// 通信エラーが発生している可能性もあるため、再送信等の後続対応はしない。
		post(PUSH_MESSAGE_URL, errorMessageJson);
	}

	/**
	 * Pushメッセージ(テキスト)送信用のリクエストデータを取得する
	 * 
	 * @param userId ユーザーID
	 * @param text   テキスト
	 * @return リクエストデータ
	 * @throws JsonProcessingException
	 */
	public static String getPushTextMessageRequestData(String userId, String text) throws JsonProcessingException {

		PushMessageDto<TextMessageDto> pushMessageDto = new PushMessageDto<>();
		pushMessageDto.setTo(userId);
		pushMessageDto.setMessages(Arrays.asList(new TextMessageDto(text)));
		pushMessageDto.setNotificationDisabled(true);
		return jsonMapper.writeValueAsString(pushMessageDto);
	}

	/**
	 * Pushメッセージ(画像)送信用のリクエストデータを取得する
	 * 
	 * @param userId             ユーザーID
	 * @param originalContentUrl 画像URL
	 * @param previewImageUrl    プレビュー用URL
	 * @return リクエストデータ
	 * @throws JsonProcessingException
	 */
	public static String getPushImageMessageRequestData(String userId, String originalContentUrl,
			String previewImageUrl) throws JsonProcessingException {

		PushMessageDto<ImageMessageDto> pushMessageDto = new PushMessageDto<>();
		pushMessageDto.setTo(userId);
		pushMessageDto.setMessages(Arrays.asList(new ImageMessageDto(originalContentUrl, previewImageUrl)));
		pushMessageDto.setNotificationDisabled(true);
		return jsonMapper.writeValueAsString(pushMessageDto);
	}

	/**
	 * Pushメッセージ(Flex)送信用のリクエストデータを取得する
	 * 
	 * @param userId ユーザーID
	 * @param url    対象URL
	 * @return リクエストデータ
	 * @throws JsonProcessingException
	 */
	public static String getPushFlexMessageRequestData(String userId, String url) throws JsonProcessingException {

		PushMessageDto<FlexMessageDto<FlexBubbleMessageDto>> pushMessageDto = new PushMessageDto<>();
		pushMessageDto.setTo(userId);
		VerticalBoxTypeDto headerDto = new VerticalBoxTypeDto(Arrays.asList(new FlexTextContentDto("第22回勉強会")));
		VerticalBoxTypeDto bodyDto = new VerticalBoxTypeDto(Arrays.asList(new FlexTextContentDto(url)));
		VerticalBoxTypeDto footerDto = new VerticalBoxTypeDto(
				Arrays.asList(new FlexTextContentDto("Copyright 2019 株式会社PUreatio")));
		FlexBubbleMessageDto messageDto = new FlexBubbleMessageDto(headerDto, bodyDto, footerDto);
		pushMessageDto.setMessages(Arrays.asList(new FlexMessageDto<>(messageDto)));
		pushMessageDto.setNotificationDisabled(true);
		return jsonMapper.writeValueAsString(pushMessageDto);
	}
}
