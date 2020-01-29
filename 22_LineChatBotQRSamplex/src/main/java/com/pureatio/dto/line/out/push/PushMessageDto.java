package com.pureatio.dto.line.out.push;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Pushメッセージ用DTO
 */
public class PushMessageDto<T> {

	@Getter
	@Setter
	@JsonProperty("to")
	private String to = null;
	
	@Getter
	@Setter
	@JsonProperty("messages")
	private List<T> messages = null;

	@Getter
	@Setter
	@JsonProperty("notificationDisabled")
	private boolean notificationDisabled = false;
}
