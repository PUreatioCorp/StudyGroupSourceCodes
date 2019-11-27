package com.pureatio.linechatbot.service;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pureatio.linechatbot.dto.address.AddressResultDto;
import com.pureatio.linechatbot.dto.service.ServiceResultDto;

/**
 * 住所サービス
 */
public class ZipService {

	// JSON用マッパー
	private static ObjectMapper jsonMapper = new ObjectMapper();

	/**
	 * 郵便番号から住所を取得する
	 * 
	 * @param zipCode 郵便番号
	 * @return 住所情報
	 * @throws IOException
	 */
	public static ServiceResultDto<AddressResultDto> getAddress(String zipCode) throws IOException {

		ServiceResultDto<AddressResultDto> resultDto = new ServiceResultDto<>();

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpGet request = new HttpGet(String.format("http://zipcloud.ibsnet.co.jp/api/search?zipcode=%s", zipCode));

			try (CloseableHttpResponse response = client.execute(request)) {
				int statusCode = response.getStatusLine().getStatusCode();
				resultDto.setStatusCode(statusCode);

				if (HttpStatus.SC_OK == statusCode) {
					resultDto.setSuccess(true);
					AddressResultDto addressDto = jsonMapper.readValue(response.getEntity().getContent(),
							AddressResultDto.class);
					resultDto.setValue(addressDto);
				} else {
					resultDto.setErrorReason(response.getStatusLine().getReasonPhrase());
				}
			}
		}

		return resultDto;
	}
}
