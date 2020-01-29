package com.pureatio.dto.line.in;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * イベント発生元情報DTO
 */
public class EventSourceDto {

	@Getter
	@Setter
	@JsonProperty("userId")
	private String userId = null;

	@Getter
	@Setter
	@JsonProperty("type")
	private String type = null;
}
