package com.pureatio.linechatbot.dto.line.out.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * テキストメッセージ用DTO
 */
public class TextMessageDto {

	@Getter
	@Setter
	@JsonProperty("type")
	private String type = "text";

	@Getter
	@Setter
	@JsonProperty("text")
	private String text = null;

	/**
	 * コンストラクタ
	 * 
	 * @param text メッセージテキスト
	 */
	public TextMessageDto(String text) {
		this.text = text;
	}
}
