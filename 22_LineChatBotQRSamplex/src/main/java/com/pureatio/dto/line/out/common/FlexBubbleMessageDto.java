package com.pureatio.dto.line.out.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pureatio.dto.line.out.common.flex.VerticalBoxTypeDto;

import lombok.Getter;
import lombok.Setter;

/**
 * Flexメッセージ(Bubble)用DTO
 */
@JsonIgnoreProperties({ "hero" })
public class FlexBubbleMessageDto {

	@Getter
	@Setter
	@JsonProperty("type")
	private String type = "bubble";

	@Getter
	@Setter
	@JsonProperty("header")
	private VerticalBoxTypeDto header = null;

	@Getter
	@Setter
	@JsonProperty("body")
	private VerticalBoxTypeDto body = null;

	@Getter
	@Setter
	@JsonProperty("footer")
	private VerticalBoxTypeDto footer = null;

	/**
	 * コンストラクタ
	 * 
	 * @param header ヘッダー
	 * @param body   ボディー
	 * @param footer フッター
	 */
	public FlexBubbleMessageDto(VerticalBoxTypeDto header, VerticalBoxTypeDto body, VerticalBoxTypeDto footer) {
		this.header = header;
		this.body = body;
		this.footer = footer;
	}
}
