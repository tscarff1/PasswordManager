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

import org.mindrot.BCrypt;

public class Crypto {
	private KeyGenerator keygen;
	private SecretKey key;
	private Cipher cipher;
	
	public Crypto(byte[] pwd){
		try {
			testBCrypt();
			Logger.info("Creating Crypto class");
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
	
	public static String hashPassword(String pw){
		String salt = BCrypt.gensalt(12);
		return BCrypt.hashpw(pw, salt);
	}
	
	public static boolean checkPassword(String test, String hashed){
		Logger.info(hashed);
		return BCrypt.checkpw(test, hashPassword("!Deoxys#386!"));
	}
	
	private void testBCrypt(){
		String salt = BCrypt.gensalt(12);
		Logger.debug("Salt: " + salt);
		String hashed = BCrypt.hashpw("T3stP@ssw0rd", salt);
		salt = BCrypt.gensalt(12);
		if(BCrypt.checkpw("T3stP@ssw0rd",hashed)){
			Logger.debug("Passwords match");
		}
		else{
			Logger.debug("Not match");
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
