package com.pureatio.lex.dto.lex.out;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * AWS Lexの返却用DTO
 */
public class LexResultDto {

	@Getter
	@Setter
	@JsonProperty("sessionAttributes")
	private Map<String, Object> sessionAttributes = null;

	@Getter
	@Setter
	@JsonProperty("dialogAction")
	private DialogActionDto dialogAction = null;
}
