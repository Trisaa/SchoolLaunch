package com.link.schoolunch.util;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class Common {

	public static boolean TOKEN_EXPIRED = false;
	private static int QR_WIDTH = 200, QR_HEIGHT = 200;
	
	public static void checkToken( JSONObject json ) {
		//The session has expired
		try {
			if( !json.has("message") ) {
				return;
			}
			String message = json.getString("message");
			if( message.contains("The session has expired") ) {
				TOKEN_EXPIRED = true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			
		}
	}
	
	public static Bitmap createQRImage(String url) {
		try {
			if (url == null || "".equals(url) || url.length() < 1) {
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			for (int y = 0; y < QR_HEIGHT; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QR_WIDTH + x] = 0xff000000;
					}
					else {
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			return bitmap;
		}
		catch (WriterException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
