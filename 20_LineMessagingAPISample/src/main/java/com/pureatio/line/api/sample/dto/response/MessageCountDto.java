package com.pureatio.line.api.sample.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * メッセージ件数カウント用DTO
 */
@JsonIgnoreProperties({ "broadcast", "targeting", "autoResponse", "welcomeResponse", "chat", "apiPush", "apiMulticast",
		"apiReply" })
public class MessageCountDto {

	@Getter
	@Setter
	@JsonProperty("status")
	private String status = null;

	@Getter
	@Setter
	@JsonProperty("apiBroadcast")
	private int apiBroadcast = 0;

	/**
	 * コンストラクタ
	 */
	public MessageCountDto() {
	}
}
