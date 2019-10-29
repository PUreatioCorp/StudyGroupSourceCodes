package com.pureatio.line.api.sample.dto.message;

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
	 * @param text テキスト
	 */
	public TextMessageDto(String text) {
		this.text = text;
	}
}
