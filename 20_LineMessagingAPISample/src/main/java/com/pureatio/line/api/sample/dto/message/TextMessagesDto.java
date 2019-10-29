package com.pureatio.line.api.sample.dto.message;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * テキストメッセージ用DTO
 */
public class TextMessagesDto {

	@Getter
	@Setter
	@JsonProperty("messages")
	private List<TextMessageDto> messages = null;

	/**
	 * コンストラクタ
	 * 
	 * @param messages 送信メッセージ
	 */
	public TextMessagesDto(List<TextMessageDto> messages) {
		this.messages = messages;
	}
}
