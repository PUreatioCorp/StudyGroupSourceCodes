package com.pureatio.dto.line.out.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Flexメッセージ用DTO
 */
public class FlexMessageDto<T> {

	@Getter
	@Setter
	@JsonProperty("type")
	private String type = "flex";

	@Getter
	@Setter
	@JsonProperty("altText")
	private String altText = null;

	@Getter
	@Setter
	@JsonProperty("contents")
	private T contents = null;

	/**
	 * コンストラクタ
	 * 
	 * @param contents コンテンツ
	 */
	public FlexMessageDto(T contents) {
		this.altText = "第22回勉強会Flexメッセージ";
		this.contents = contents;
	}
}
