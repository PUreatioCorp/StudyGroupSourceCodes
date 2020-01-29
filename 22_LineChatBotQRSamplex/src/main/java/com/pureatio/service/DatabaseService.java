package com.pureatio.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;

/**
 * データベース用サービス
 */
public class DatabaseService {

	/** 接続URL */
	private static final String CONNECTION_STRING = "CONNECTION_STRING";
	/** ユーザーID */
	private static final String USER_ID = "USER_ID";
	/** パスワード */
	private static final String PASSWORD = "PASSWORD";

	/**
	 * QRコード情報を登録する。
	 * 
	 * @param qrContents QR情報
	 * @return 反映件数
	 * @throws SQLException
	 */
	public static int insertQRContents(String qrContents) throws SQLException {

		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setURL(System.getenv(CONNECTION_STRING));
		dataSource.setUser(System.getenv(USER_ID));
		dataSource.setPassword(System.getenv(PASSWORD));

		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement statement = conn.prepareStatement(String
					.format("INSERT INTO linechatbot.qrdata(contents, createdtime) VALUES('%s', now());", qrContents));
			int refCount = statement.executeUpdate();
			return refCount;
		}
	}
}
