package com.pureatio.dto.line.in;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 入力イベントDTO
 */
public class LineInputDto {

	@Getter
	@Setter
	@JsonProperty("events")
	private List<EventDto> events = null;

	@Getter
	@Setter
	@JsonProperty("destination")
	private String destination = null;
}
