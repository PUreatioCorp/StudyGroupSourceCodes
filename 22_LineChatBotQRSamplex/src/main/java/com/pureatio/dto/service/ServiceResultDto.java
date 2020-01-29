package com.pureatio.dto.service;

import lombok.Getter;
import lombok.Setter;

/**
 * LINEサービス結果用DTO
 */
public class ServiceResultDto<T> {

	// 結果
	@Getter
	@Setter
	private boolean isSuccess = false;

	// HTTPステータス
	@Getter
	@Setter
	private int statusCode = 0;

	// エラー理由
	@Getter
	@Setter
	private String errorReason = null;
	
	@Getter
	@Setter
	private T value = null;
}
