package com.pureatio.linechatbot.service;

import java.util.Arrays;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pureatio.linechatbot.dto.line.out.common.TextMessageDto;
import com.pureatio.linechatbot.dto.line.out.push.PushMessageDto;
import com.pureatio.linechatbot.dto.service.ServiceResultDto;

/**
 * LINE用サービス
 */
public class LineService {

	/** Pushメッセージ用URL */
	public static final String PUSH_MESSAGE_URL = "https://api.line.me/v2/bot/message/push";

	/** LINEのAPIトークン */
	private static final String LINE_ACCESS_TOKEN = "LINE_ACCESS_TOKEN";
	// Json変換用Mapper
	private static ObjectMapper jsonMapper = new ObjectMapper();

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
					result.setErrorReason(response.getStatusLine().getReasonPhrase());
				}
			}
		}

		return result;
	}

	/**
	 * Pushメッセージ送信用のリクエストデータを取得する
	 * 
	 * @param userId ユーザーID
	 * @param text   テキスト
	 * @return リクエストデータ
	 * @throws JsonProcessingException
	 */
	public static String getPushMessageRequestData(String userId, String text) throws JsonProcessingException {

		PushMessageDto<TextMessageDto> pushMessageDto = new PushMessageDto<>();
		pushMessageDto.setTo(userId);
		pushMessageDto.setMessages(Arrays.asList(new TextMessageDto(text)));
		pushMessageDto.setNotificationDisabled(true);
		return jsonMapper.writeValueAsString(pushMessageDto);
	}
}
