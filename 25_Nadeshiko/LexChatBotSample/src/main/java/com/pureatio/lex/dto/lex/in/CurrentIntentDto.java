package com.pureatio.lex.dto.lex.in;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * currentIntent構造
 */
@JsonIgnoreProperties({ "name", "slotDetails", "confirmationStatus", "nluIntentConfidenceScore" })
public class CurrentIntentDto {

	/** slots */
	@Getter
	@Setter
	@JsonProperty("slots")
	private Map<String, Object> slots = null;
}
