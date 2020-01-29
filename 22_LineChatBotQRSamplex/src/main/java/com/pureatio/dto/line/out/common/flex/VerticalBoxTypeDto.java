package com.pureatio.dto.line.out.common.flex;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * FlexメッセージのBox、layoutがverticalのDTO
 */
public class VerticalBoxTypeDto {

	@Getter
	@Setter
	@JsonProperty("type")
	private String type = "box";

	@Getter
	@Setter
	@JsonProperty("layout")
	private String layout = "vertical";

	@Getter
	@Setter
	@JsonProperty("contents")
	private List<FlexTextContentDto> contents = null;

	/**
	 * コンストラクタ
	 * 
	 * @param contents コンテンツ
	 */
	public VerticalBoxTypeDto(List<FlexTextContentDto> contents) {
		this.contents = contents;
	}
}
