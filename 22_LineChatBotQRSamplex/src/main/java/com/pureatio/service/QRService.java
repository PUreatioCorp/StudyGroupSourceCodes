package com.pureatio.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * QRコード用サービス
 */
public class QRService {

	/**
	 * QRコード用のMatrixを取得する
	 * 
	 * @param contents QRコードの内容
	 * @return Matrix
	 * @throws WriterException
	 */
	public static BitMatrix getQRCodeMatrix(final String contents) throws WriterException {

		ConcurrentHashMap<EncodeHintType, Object> hintMap = new ConcurrentHashMap<>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		hintMap.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
		hintMap.put(EncodeHintType.MARGIN, 0);

		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix matrix = writer.encode(contents, BarcodeFormat.QR_CODE, 160, 160, hintMap);

		return matrix;
	}

	/**
	 * QRコードを読み取り、中身のテキストを取得する
	 * 
	 * @param contentImage QRコード画像
	 * @return QRコード内容テキスト
	 * @throws IOException
	 * @throws NotFoundException
	 * @throws ChecksumException
	 * @throws FormatException
	 */
	public static String getQRContents(final BufferedImage contentImage)
			throws IOException, NotFoundException, ChecksumException, FormatException {

		QRCodeReader reader = new QRCodeReader();
		LuminanceSource qrcodeSource = new BufferedImageLuminanceSource(contentImage);
		BinaryBitmap qrcodeBitmap = new BinaryBitmap(new HybridBinarizer(qrcodeSource));
		Result decodeResult = reader.decode(qrcodeBitmap);

		return decodeResult.getText();
	}
}
