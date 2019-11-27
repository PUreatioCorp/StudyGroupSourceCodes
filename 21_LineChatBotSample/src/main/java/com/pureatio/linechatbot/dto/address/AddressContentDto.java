package com.pureatio.linechatbot.dto.address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 住所取得内容DTO
 */
@JsonIgnoreProperties({ "prefcode", "zipcode" })
public class AddressContentDto {

	@Getter
	@Setter
	@JsonProperty("address1")
	private String address1 = null;

	@Getter
	@Setter
	@JsonProperty("address2")
	private String address2 = null;

	@Getter
	@Setter
	@JsonProperty("address3")
	private String address3 = null;

	@Getter
	@Setter
	@JsonProperty("kana1")
	private String kana1 = null;

	@Getter
	@Setter
	@JsonProperty("kana2")
	private String kana2 = null;

	@Getter
	@Setter
	@JsonProperty("kana3")
	private String kana3 = null;

	/**
	 * 住所を取得する
	 * 
	 * @return 住所
	 */
	public String getAddress() {
		return String.format("%s %s %s", this.address1, this.address2, this.address3);
	}

	/**
	 * 住所のカナを取得する
	 * 
	 * @return 住所のカナ
	 */
	public String getKana() {
		return String.format("%s %s %s", this.kana1, this.kana2, this.kana3);
	}
}
