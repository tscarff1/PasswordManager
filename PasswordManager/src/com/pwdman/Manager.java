package com.pwdman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.swing.JFrame;
import javax.swing.JTable;

public class Manager {
	private byte[] pass;
	private KeyGenerator keygen;
	private SecretKey key;
	private Cipher cipher;
	private AES aes;
	private File file = null;
	private MainFrame mf;
	private final String FILENAME = "PasswordManager" + File.separator + "pwdman.pd";
	private final String WINDOWFILE = System.getenv("APPDATA") + File.separator + FILENAME;
	private final String MACFILE = System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support"
			+ File.separator + FILENAME;
	private final String UNIXFILE = "/var" + File.separator + "lib" + File.separator + FILENAME;
	private FileOutputStream output;
	private FileInputStream input;

	private String[] accounts, users, passwords;
	private static final String DATADELIMITER = "[{??}]";

	private boolean isInit = false;
	private boolean fileCreated = false;
	private boolean fileExists = false;

	public Manager(byte[] p){
		aes = new AES(p);
		pass = aes.encrypt(p);
		p = null; // Probably not needed, but prevents the plaintext password from exisitng any longer than needed
		createFile();
		mf = new MainFrame(this);
	}

	public boolean isInit(){
		return isInit;
	}

	public boolean fileCreated(){
		return fileCreated;
	}

	//Compares the encrypted given password to the encrypted saved password
	public boolean verifyPassword(byte[] test){
		boolean res = false;
			test = aes.encrypt(test);
			if(Arrays.equals(test, pass))
				res = true;
		return res;
	}

	private void setStreamForWrite(){
		String fileStr = "error";

		if(OSValidator.isWindows()){
			fileStr = WINDOWFILE;
		}
		if(OSValidator.isMac()){
			fileStr = MACFILE;
		}

		if(OSValidator.isUnix()){
			fileStr = UNIXFILE;
		}
		try {	
			if(!fileStr.equals("error")){
				output = new FileOutputStream(fileStr);
			}
			else{
				//Should replace with logging function
				Logger.error("Operating System not found.");
			}
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setStreamForRead(){
		String fileStr = "error";

		if(OSValidator.isWindows()){
			fileStr = WINDOWFILE;
		}
		if(OSValidator.isMac()){
			fileStr = MACFILE;
		}

		if(OSValidator.isUnix()){
			fileStr = UNIXFILE;
		}
		try {	
			if(!fileStr.equals("error") && file.exists()){
				input = new FileInputStream(fileStr);
			}
			else{
				//Should replace with logging function
				Logger.error("OS not found");
			}
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createFile(){
		Logger.info("Creating file");
		if(OSValidator.isWindows()){
			Logger.info("OS is Windows");
			file = new File(WINDOWFILE);
			file.getParentFile().mkdirs();
		}
		if(OSValidator.isMac()){
			file = new File(MACFILE);
		}

		if(OSValidator.isUnix()){
			file = new File(UNIXFILE);
		}
		fileCreated = true;
		fileExists = file.exists();
		Logger.info("Created file at " + file.getAbsolutePath());
	}

	public void writeToFile(){
		setStreamForWrite();
		try {
			byte[] e = getEncryptedInfoString();
			Logger.info("Writing to file: " + e);
			System.out.println(e[0]);
			output.write(e);
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String getDataFromFile(){
		if(file.exists()){
			byte[] data = new byte[16];
			byte[] bytes = new byte[1];
			int bytesRead = 0;
			int totalBytesRead = 0;
			setStreamForRead();
			try {
				while((bytesRead =input.read(bytes)) > 0){
					totalBytesRead += bytesRead;
					for(int i = 0; i < bytesRead; i++){
						if(totalBytesRead > data.length){
							data = Arrays.copyOf(data, data.length + 16);
						}
						data[totalBytesRead-1] = bytes[i];
					}
				}
				if(bytesRead < 0){
					Logger.warn("Empty file!");
				}
				Logger.info("File contents");
				Logger.info(data);
				input.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//if(bytesRead >= 0){
			Logger.info(data);
			//}
			return new String(aes.decrypt(data));
		}
		return null;
	}


	public void startFrame(JFrame frame){
		frame.setVisible(true);
	}

	public void startMainFrame(){
		fillTable(mf.getTable());
		startFrame(mf);
	}

	private String getInfoString(){
		String info = "";
		for(int i = 0; i < accounts.length; i++){
			if(i < accounts.length - 1){
				info += accounts[i] + DATADELIMITER;
				info += users[i] + DATADELIMITER;
				info += passwords[i] + DATADELIMITER;
			}
			else{
				info += accounts[i] + DATADELIMITER;
				info += users[i] + DATADELIMITER;
				info += passwords[i];
			}
		}
		return info;
	}

	private byte[] getEncryptedInfoString(){
		byte[] einfo = null;
		String info = getInfoString();
		einfo = aes.encrypt(info.getBytes());
		Logger.info("Plain: Text" + info);
		Logger.info("Encrypted: " + einfo);
		Logger.info("Decrypted: " + aes.decrypt(einfo));
		return einfo;
	}

	public void fillTable(JTable table){
		String info = getDataFromFile();
		Logger.info("Filling table");
		if(info != null){
			String[] data = info.split(Pattern.quote("[{??}]"));
			int rows = data.length/3;
			for(int r = 0; r < rows; r++){
				for(int c = 0; c < 3; c++){
					table.getModel().setValueAt(data[3*r + c], r, c);
				}
			}
		}
		isInit = true;
	}

	public void setAccounts(String[] a, String[] u, String[] p){
		accounts = a;
		users = u;
		passwords = p;
	}

}
