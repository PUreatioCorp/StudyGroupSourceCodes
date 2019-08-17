package com.pureatio;

/**
 * メンバークラス
 */
public class Member {

	/** ID */
	private String id = null;
	/** 名前 */
	private String name = null;
	/** 年齢 */
	private int age = 0;

	/**
	 * IDを取得する
	 * 
	 * @return ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * IDを設定する
	 * 
	 * @param id ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 名前を取得する
	 * 
	 * @return 名前
	 */
	public String getName() {
		return name;
	}

	/**
	 * 名前を設定する
	 * 
	 * @param name 名前
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 年齢を取得する
	 * 
	 * @return 年齢
	 */
	public int getAge() {
		return age;
	}

	/**
	 * 年齢を設定する
	 * 
	 * @param age 年齢
	 */
	public void setAge(int age) {
		this.age = age;
	}
}