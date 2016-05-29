package com.example.x14_httpclient.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

	public static String getTextFromStream(InputStream is) {
		byte[] b = new byte[1024];
		int len = 0;
		// �����ֽ��������������ȡ���������ı�����ʱ��ͬ��������д�����������
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len);
			}
			// ���ֽ�����������������ת�����ֽ�����
			String text = new String(baos.toString());
			return text;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
