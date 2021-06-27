package com.pureatio.handler;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.AmazonTranslateClient;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
import com.amazonaws.util.Base64;

/**
 * 翻訳ハンドラー(Dockerコンテナイメージ)
 */
public class TranslateSentenceHandler implements RequestHandler<Map<String, Object>, String> {

	/**
	 * @see RequestHandler#handleRequest(Object, Context)
	 */
	@Override
	public String handleRequest(Map<String, Object> input, Context context) {

		context.getLogger().log("input" + input);
		String result = "PUreatioCorp. Translate sample Lambda functions.";

		try {
			// 入力文字列をBase64でデコードする。
			String text = new String(Base64.decode(input.get("text").toString()), StandardCharsets.UTF_8);
			// AWS Translateを利用して、受け取った文字列を翻訳する。
			AWSCredentialsProvider awsCreds = DefaultAWSCredentialsProviderChain.getInstance();

			AmazonTranslate translate = AmazonTranslateClient.builder()
					.withCredentials(new AWSStaticCredentialsProvider(awsCreds.getCredentials()))
					.withRegion("us-east-1").build();

			TranslateTextRequest request = new TranslateTextRequest().withText(text).withSourceLanguageCode("en")
					.withTargetLanguageCode("ja");
			TranslateTextResult translatedResult = translate.translateText(request);

			// 結果をBase64でエンコードする。
			result = Base64.encodeAsString(translatedResult.getTranslatedText().getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			context.getLogger().log(e.getMessage());
			e.printStackTrace();
			return e.getMessage();
		}

		return result;
	}
}
