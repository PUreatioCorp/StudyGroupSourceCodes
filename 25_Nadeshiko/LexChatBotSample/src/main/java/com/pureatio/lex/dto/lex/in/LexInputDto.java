package com.pureatio.lex.dto.lex.in;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * AWS Lexの入力イベントDTO
 */
@JsonIgnoreProperties({ "messageVersion", "invocationSource", "userId", "requestAttributes", "bot", "outputDialogMode",
		"alternativeIntents", "inputTranscript", "recentIntentSummaryView", "sentimentResponse", "kendraResponse",
		"activeContexts" })
public class LexInputDto {

	/** sessionAttributes */
	@Getter
	@Setter
	@JsonProperty("sessionAttributes")
	private Map<String, Object> sessionAttributes = null;

	/** currentIntent */
	@Getter
	@Setter
	@JsonProperty("currentIntent")
	private CurrentIntentDto currentIntent = null;
}
