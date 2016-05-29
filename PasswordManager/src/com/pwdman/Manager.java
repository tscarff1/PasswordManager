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
	private Crypto aes;
	private File file = null;
	private MainFrame mf;
	

	private String[] accounts, users, passwords;
	private static final String DATADELIMITER = "[{??}]";

	private boolean isInit = false;

	private IO accountsIO;
	
	public Manager(byte[] p){
		aes = new Crypto(p);
		pass = aes.encrypt(p);
		p =new byte[12]; // Probably not needed, but prevents the plaintext password from exisitng any longer than needed
		accountsIO = new IO("pwdman.pd");
		accountsIO.createFile();
		mf = new MainFrame(this);
	}

	public boolean isInit(){
		return isInit;
	}


	//Compares the encrypted given password to the encrypted saved password
	public boolean verifyPassword(byte[] test){
		boolean res = false;
			test = aes.encrypt(test);
			if(Arrays.equals(test, pass))
				res = true;
		return res;
	}


	private String getDataFromFile(){
		byte[] data = accountsIO.readFromFile();
		if(data == null)
			return null;
		else{
			return new String(aes.decrypt(data));
		}
	}


	public void startFrame(JFrame frame){
		frame.setVisible(true);
	}

	public void startMainFrame(){
		fillTable(mf.getTable());
		startFrame(mf);
	}
	
	public void writeAccountsToFile(){
		accountsIO.writeToFile(aes.encrypt(getInfoString().getBytes()));
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

	public void fillTable(JTable table){
		String info = getDataFromFile();
		Logger.info("Filling table");
		if(info != null){
			String[] data = info.split(Pattern.quote("[{??}]"));
			int rows = data.length/3;
			for(int r = 0; r < rows; r++){
				String acc = data[3*r+0];
				if(acc.equals("null"))
					acc = "";
				String user = data[3*r + 1];
				if(user.equals("null"))
						user = "";
				char[] pass = data[3*r + 2].toCharArray();
				if(Arrays.equals(pass, "null".toCharArray()))
					pass = "".toCharArray();
				if(!acc.equals("") && !user.equals("") && !Arrays.equals(pass, "".toCharArray()))
					addAccount(acc, user, pass);
			}
		}
		isInit = true;
	}

	public void addAccount(String account, String user, char[] password){
		mf.addAccount(account, user, password);
	}
	
	public void enableMainFrame(){
		mf.setEnabled(true);
	}
	
	public void setAccounts(String[] a, String[] u, String[] p){
		accounts = a;
		users = u;
		passwords = p;
	}

}
