package com.pwdman;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class AES {
	private KeyGenerator keygen;
	private SecretKey key;
	private Cipher cipher;
	
	public AES(byte[] pwd){
		try {
			Logger.info("Creating AES class");
		//Create a key based on the password
		SecureRandom rand = new SecureRandom();
		rand.setSeed(pwd);
		
		keygen = KeyGenerator.getInstance("AES");
		
		keygen.init(128, rand);
		
		key = keygen.generateKey();
		cipher = Cipher.getInstance("AES");
	
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Encrypt a given byte array. Returns null if it fails
	public byte[] encrypt(byte[] data){
		Logger.info("Encrypting data: ");
		Logger.info(data);
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(data);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger.error("Data encryption failed");
		return null;
	}
	
	public byte[] decrypt(byte[] data){
		Logger.info("Decrypting data: ");
		Logger.info(data);
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(data);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Logger.error("Data encryption failed");
		return null;
	}
}
