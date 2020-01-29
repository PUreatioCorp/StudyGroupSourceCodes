package com.pureatio.dto.line.in;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 画像メッセージDTO<br>
 * LINEから画像が送信されることを前提とする。
 */
@JsonIgnoreProperties({ "contentProvider" })
public class ImageMessageDto {

	@Getter
	@Setter
	@JsonProperty("type")
	private String type = null;

	@Getter
	@Setter
	@JsonProperty("id")
	private String id = null;
}
