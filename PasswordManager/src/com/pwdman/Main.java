package com.pwdman;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.JButton;

public class Main {

	private static JButton startButton, cancelButton;
	
	public static void main(String[] args) {
		
		try {
			byte[] password = "!Deoxys#386!".getBytes("UTF8");
			Manager man = new Manager(password);
			while(!man.fileCreated());
			StartFrame startFrame = new StartFrame(man);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
}
