package com.pureatio.lex.dto.lex.out;

import lombok.Getter;
import lombok.Setter;

/**
 * dialogAction構造DTO
 */
public class DialogActionDto {

	/** type */
	@Getter
	@Setter
	private String type = "Close";

	/** fulfillmentState */
	@Getter
	@Setter
	private String fulfillmentState = null;

	/** message */
	@Getter
	@Setter
	private MessageDto message = null;
}
