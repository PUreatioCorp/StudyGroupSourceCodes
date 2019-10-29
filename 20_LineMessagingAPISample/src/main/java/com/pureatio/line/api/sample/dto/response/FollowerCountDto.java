package com.pureatio.line.api.sample.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * フォロワー人数カウント用DTO
 */
@JsonIgnoreProperties({ "targetedReaches" })
public class FollowerCountDto {

	@Getter
	@Setter
	@JsonProperty("status")
	private String status = null;

	@Getter
	@Setter
	@JsonProperty("followers")
	private int followers = 0;

	@Getter
	@Setter
	@JsonProperty("blocks")
	private int blocks = 0;

	/**
	 * コンストラクタ
	 */
	public FollowerCountDto() {
	}
}
