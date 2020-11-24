package com.pureatio.lex.dto.lex.out;

import lombok.Getter;
import lombok.Setter;

/**
 * メッセージ構造DTO
 */
public class MessageDto {

	/** contentType */
	@Getter
	@Setter
	private String contentType = "PlainText";

	/** contentType */
	@Getter
	@Setter
	private String content = null;
}
