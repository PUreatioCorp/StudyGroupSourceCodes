package com.pureatio.service;

import java.nio.file.Path;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;

/**
 * AWS用サービス
 */
public class AwsService {

	/** S3バケット名 */
	private static final String S3_BUCKET_NAME = "S3_BUCKET_NAME";
	/** S3キーフォーマット */
	private static final String S3_KEY_FORMAT = "S3_KEY_FORMAT";

	/**
	 * AWS S3にファイルを配置する
	 */
	public static PutObjectResult putToS3(final Path putSrcPath) {

		final AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		String key = String.format(System.getenv(S3_KEY_FORMAT), putSrcPath.getFileName());
		PutObjectResult putResult = s3Client.putObject(System.getenv(S3_BUCKET_NAME), key, putSrcPath.toFile());

		return putResult;
	}
}
