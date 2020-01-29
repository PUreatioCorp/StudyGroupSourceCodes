package com.pureatio.dto.line.in;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * イベントDTO
 */
public class EventDto {

	@Getter
	@Setter
	@JsonProperty("type")
	private String type = null;

	@Getter
	@Setter
	@JsonProperty("replyToken")
	private String replyToken = null;

	@Getter
	@Setter
	@JsonProperty("source")
	private EventSourceDto source = null;

	@Getter
	@Setter
	@JsonProperty("timestamp")
	private Long timestamp = 0l;
	
	@Getter
	@Setter
	@JsonProperty("mode")
	private String mode = null;

	@Getter
	@Setter
	@JsonProperty("message")
	private Map<String, Object> message = null;
}
