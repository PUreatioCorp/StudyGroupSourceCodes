package com.example.demo.dto;

/**
 * �����o�[�N���X
 */
public class Member {

	/** ID */
	private String id = null;
	/** ���O */
	private String name = null;
	/** �N�� */
	private int age = 0;

	/**
	 * ID���擾����
	 * 
	 * @return ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * ID��ݒ肷��
	 * 
	 * @param id ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * ���O���擾����
	 * 
	 * @return ���O
	 */
	public String getName() {
		return name;
	}

	/**
	 * ���O��ݒ肷��
	 * 
	 * @param name ���O
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * �N����擾����
	 * 
	 * @return �N��
	 */
	public int getAge() {
		return age;
	}

	/**
	 * �N���ݒ肷��
	 * 
	 * @param age �N��
	 */
	public void setAge(int age) {
		this.age = age;
	}
}