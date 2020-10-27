package com.pureatio.lex.dto.address;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 住所取得結果DTO
 */
public class AddressResultDto {

	@Getter
	@Setter
	@JsonProperty("message")
	private String message = null;

	@Getter
	@Setter
	@JsonProperty("results")
	private List<AddressContentDto> result = null;

	@Getter
	@Setter
	@JsonProperty("status")
	private String status = null;

	@Getter
	@Setter
	@JsonProperty("timestamp")
	private Long timestamp = null;
}
