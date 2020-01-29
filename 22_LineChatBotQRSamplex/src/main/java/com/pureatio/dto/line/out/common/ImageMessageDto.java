package com.pureatio.dto.line.out.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 画像メッセージ用DTO
 */
public class ImageMessageDto {

	@Getter
	@Setter
	@JsonProperty("type")
	private String type = "image";

	@Getter
	@Setter
	@JsonProperty("originalContentUrl")
	private String originalContentUrl = null;

	@Getter
	@Setter
	@JsonProperty("previewImageUrl")
	private String previewImageUrl = null;

	/**
	 * コンストラクタ
	 * 
	 * @param originalContentUrl 画像URL
	 * @param previewImageUrl    プレビューURL
	 */
	public ImageMessageDto(String originalContentUrl, String previewImageUrl) {
		this.originalContentUrl = originalContentUrl;
		this.previewImageUrl = previewImageUrl;
	}
}
