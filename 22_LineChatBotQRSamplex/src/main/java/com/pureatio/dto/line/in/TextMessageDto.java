package com.pureatio.dto.line.in;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * テキストメッセージDTO
 */
public class TextMessageDto {

	@Getter
	@Setter
	@JsonProperty("type")
	private String type = null;

	@Getter
	@Setter
	@JsonProperty("id")
	private String id = null;

	@Getter
	@Setter
	@JsonProperty("text")
	private String text = null;
}
