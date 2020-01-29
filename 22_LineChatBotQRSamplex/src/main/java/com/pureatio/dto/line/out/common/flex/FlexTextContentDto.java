package com.pureatio.dto.line.out.common.flex;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * FlexメッセージのTextコンテンツDTO
 */
public class FlexTextContentDto {

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
	public FlexTextContentDto(String text) {
		this.text = text;
	}
}
