package com.pureatio.dto.line.out.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * エラーメッセージDTO
 */
@JsonIgnoreProperties("details")
public class ErrorMessageDto {

	@Getter
	@Setter
	@JsonProperty("message")
	private String message = null;

	/**
	 * デフォルトコンストラクタ
	 */
	public ErrorMessageDto() {
	}
}
