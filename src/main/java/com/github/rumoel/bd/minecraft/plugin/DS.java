package com.github.rumoel.bd.minecraft.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DS extends ListenerAdapter {
	private DsConfig config = new DsConfig();
	private File configFile = new File("dsConfig.yml");
	Logger logger = LoggerFactory.getLogger(getClass());
	private JDA bot;

	public void enable() throws LoginException, InterruptedException, IOException {
		readConfig();
		bot = JDABuilder.createDefault(config.TOKEN).addEventListeners(this).build();
		bot.awaitReady();
	}

	class DsConfig {
		@Getter
		@Setter
		private String TOKEN = "token_here";
		@Getter
		@Setter
		private long channel = 1337L;
	}

	private void readConfig() throws IOException {
		if (!configFile.exists()) {
			logger.info("File {} is created:{}", configFile.getAbsolutePath(), configFile.createNewFile());
			HashedMap<String, String> confMap = new HashedMap<String, String>();
			confMap.put("token", config.getTOKEN());
			confMap.put("channel", Long.toString(config.getChannel()));

			for (Map.Entry<String, String> entry : confMap.entrySet()) {
				byte[] data = (entry.getKey() + "=" + entry.getValue() + '\n').getBytes(StandardCharsets.UTF_8);
				Files.write(configFile.toPath(), data, StandardOpenOption.APPEND);
			}
		} else {
			StringBuilder contentBuilder = new StringBuilder();
			List<String> data = Files.readAllLines(configFile.toPath(), StandardCharsets.UTF_8);
			for (String string : data) {
				if (string.startsWith("token")) {
					config.setTOKEN(StringUtils.substringAfter(string, "="));
				}
				if (string.startsWith("channel")) {
					config.setChannel(Long.parseLong(StringUtils.substringAfter(string, "=")));
				}
			}
		}
	}

	public void disable() {

	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message msg = event.getMessage();
		String message = msg.getContentRaw();
		MessageChannel channel = event.getChannel();
		long time = System.currentTimeMillis();

		System.err.println(channel.getId() + ":" + message);
		/* => RestAction<Message> */
		/* => Message */
		/*
		 * channel.sendMessage("Pong!") .queue(response -> {
		 * response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() -
		 * time).queue(); });
		 */
	}

}
