package com.pureatio.line.api.sample;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pureatio.line.api.sample.dto.message.TextMessageDto;
import com.pureatio.line.api.sample.dto.message.TextMessagesDto;
import com.pureatio.line.api.sample.dto.response.FollowerCountDto;
import com.pureatio.line.api.sample.dto.response.MessageCountDto;

/**
 * LineAPIサンプル通信用クラス
 */
public class LineAPISample {

	/** API Token */
	private static final String ACCESS_TOKEN = "{your api access token}";
	// Json用Mapper
	private static ObjectMapper jsonMapper = new ObjectMapper();
	// 日付フォーマッタ
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");

	/**
	 * メイン処理
	 * 
	 * @param args 起動引数
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		String date = dateFormatter.format(calendar.getTime());
		// アカウントのフォロワー状況を取得する。
		countFollowers(date);
		// ブロードキャストメッセージを送信する。
		sendBroadcastMessage();
		// 送信件数を取得する。
		countBroadcastMessage(date);
	}

	/**
	 * フォロワーを取得する。
	 * 
	 * @param date 対象日付
	 * @throws Exception
	 */
	private static void countFollowers(String date) throws Exception {

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpGet request = createFollowerRequest(date);

			try (CloseableHttpResponse response = client.execute(request)) {
				int status = response.getStatusLine().getStatusCode();

				if (HttpStatus.SC_OK == status) {
					FollowerCountDto countDto = jsonMapper.readValue(response.getEntity().getContent(),
							FollowerCountDto.class);
					// statusがreadyの場合はレスポンスが取得できている。
					if ("ready".equals(countDto.getStatus())) {
						System.out.println(String.format("[%s]時点で友達に追加された回数は%d回、ブロックしているユーザーは%d人です。", date,
								countDto.getFollowers(), countDto.getBlocks()));
					}

					System.out.println("API access succeed.");
				} else {
					System.err.println(String.format("API acccess failure, status = %d", status));
					System.err.println(response.getStatusLine().getReasonPhrase());
				}
			}
		}
	}

	/**
	 * フォロワー取得用のリクエストを作成する
	 * 
	 * @param date 取得日付
	 * @return リクエスト
	 * @throws Exception
	 */
	private static HttpGet createFollowerRequest(String date) throws Exception {

		HttpGet request = new HttpGet(String.format("https://api.line.me/v2/bot/insight/followers?date=%s", date));
		request.addHeader("Authorization", String.format("Bearer %s", ACCESS_TOKEN));

		return request;
	}

	/**
	 * ブロードキャストメッセージを送信する
	 * 
	 * @throws Exception
	 */
	private static void sendBroadcastMessage() throws Exception {

		try (CloseableHttpClient client = HttpClients.createDefault()) {

			HttpPost request = createBroadcastMessageRequest();

			try (CloseableHttpResponse response = client.execute(request)) {
				int status = response.getStatusLine().getStatusCode();

				if (HttpStatus.SC_OK == status) {
					System.out.println("ブロードキャストメッセージを送信しました。");
					System.out.println("API access succeed.");
				} else {
					System.err.println(String.format("API acccess failure, status = %d", status));
					System.err.println(response.getStatusLine().getReasonPhrase());
				}
			}
		}
	}

	/**
	 * ブロードキャストメッセージ送信用のリクエストを作成する
	 * 
	 * @return リクエスト
	 * @throws Exception
	 */
	private static HttpPost createBroadcastMessageRequest() throws Exception {

		HttpPost request = new HttpPost("https://api.line.me/v2/bot/message/broadcast");
		request.addHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
		request.addHeader("Authorization", String.format("Bearer %s", ACCESS_TOKEN));

		TextMessageDto textDto = new TextMessageDto("サンプルブロードキャストメッセージです。");
		request.setEntity(new StringEntity(jsonMapper.writeValueAsString(new TextMessagesDto(Arrays.asList(textDto))),
				ContentType.APPLICATION_JSON));

		return request;
	}

	/**
	 * ブロードキャストメッセージ送信数を取得する
	 * 
	 * @param date 対象日付
	 * @throws Exception
	 */
	private static void countBroadcastMessage(String date) throws Exception {

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpGet request = createCountBroadcastMessageRequest(date);

			try (CloseableHttpResponse response = client.execute(request)) {
				int status = response.getStatusLine().getStatusCode();

				if (HttpStatus.SC_OK == status) {
					MessageCountDto countDto = jsonMapper.readValue(response.getEntity().getContent(),
							MessageCountDto.class);
					// statusがreadyの場合はレスポンスが取得できている。
					if ("ready".equals(countDto.getStatus())) {
						System.out.println(
								String.format("[%s]はAPI経由でブロードキャストメッセージを%d件送信しました。", date, countDto.getApiBroadcast()));
					}

					System.out.println("API access succeed.");
				} else {
					System.err.println(String.format("API acccess failure, status = %d", status));
					System.err.println(response.getStatusLine().getReasonPhrase());
				}
			}
		}
	}

	/**
	 * ブロードキャストメッセージ送信数取得用のリクエストを作成する
	 * 
	 * @param date 取得日付
	 * @return リクエスト
	 * @throws Exception
	 */
	private static HttpGet createCountBroadcastMessageRequest(String date) throws Exception {

		HttpGet request = new HttpGet(
				String.format("https://api.line.me/v2/bot/insight/message/delivery?date=%s", date));
		request.addHeader("Authorization", String.format("Bearer %s", ACCESS_TOKEN));

		return request;
	}
}
