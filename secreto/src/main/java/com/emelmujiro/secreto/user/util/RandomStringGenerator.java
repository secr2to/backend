package com.emelmujiro.secreto.user.util;

import java.util.Random;

public class RandomStringGenerator {

	private static final char[] characters =
		"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

	public static String generate(int length) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; ++i) {
			int index = random.nextInt(characters.length);
			sb.append(characters[index]);
		}
		return sb.toString();
	}
}
