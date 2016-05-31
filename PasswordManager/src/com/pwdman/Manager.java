package com.pwdman;

import java.util.Arrays;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JTable;

/*
 * This should handle as much of the program's logic as possible. Other classes will serve
 * as helpers, but this class will call those methods.
 */

public class Manager {
	private byte[] pass;
	private Crypto crypto;
	private MainFrame mf;
	

	private String[] accounts, users, passwords;
	private static final String DATADELIMITER = "[{??}]";

	private IO accountsIO, passwordIO;
	
	public Manager(){
		//Start off by initializing two instances of the IO class
		// The first will store information on the accounts, the second will store the hashed password
		accountsIO = new IO("pwdman.pd");
		passwordIO = new IO("pwdman.cfg");
		//Check to see if a file exists which contains the hashed password.
		//This will see if the program has been run before.
		if(!passwordIO.fileExists()){
			//If the file doesn't exist, load the NewPasswordFrame. This will allow us
			//to create a save (hashed) an admin password
			Logger.debug("No configuration file found.");
			new NewPasswordFrame(this);
		}
		else{
			//If the file does exist, load the hashed password
			//Initialize a startframe so that we can have the user enter a password to see
			//if it matches the admin password
			Logger.debug("Configuration file found");
			pass = passwordIO.readFromFile();
			new StartFrame(this);
		}
		//Create a MainFrame, won't be shown until a password is set or verified
		mf = new MainFrame(this);
	}

	//Hash the new admin password and write it to a configuration file.
	//Called by NewPasswordFrame
	public void init(char[] p){
		Logger.info("Configuring program");
		//We use the plaintext password as the basis for encoding that account data
		//Honestly I should probably change that to use the hashed password
		crypto = new Crypto(new String(p).getBytes());
		pass = Crypto.hashPassword(new String(p)).getBytes();
		passwordIO.writeToFile(pass);
		
		p =new char[12]; // Probably not needed, but prevents the plaintext password from exisitng any longer than needed
		startMainFrame();
	}
	
	
	//Called by startframe
	//returns whether the password given matches the saved hashed password
	//If successful, it also initializes crypto and starts the mainframe
	public boolean attemptLogin(byte[] test){
		boolean res = Crypto.checkPassword(new String(test), new String(pass));
		if(res){
			crypto = new Crypto(test);
			startMainFrame();
		}
		return res;
	}

	//Specifically, returns the decrypted data from the accounts file,
	//or returns null if no file is found
	private String getDataFromFile(){
		byte[] data = accountsIO.readFromFile();
		if(data == null)
			return null;
		else{
			return new String(crypto.decrypt(data));
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
		accountsIO.writeToFile(crypto.encrypt(getInfoString().getBytes()));
	}

	//Get a String representation of the data in the table
	//DATADELIMITER is a string i chose to separate data that I hope will never be in the data itself
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
		//Start by checking to make sure we actually got data from the file
		if(info != null){
			//Get a string array of the data by splitting at each instance of the DATADELIMITER
			String[] data = info.split(Pattern.quote(DATADELIMITER));
			
			//Since we have three parts to each row, the number of rows is the number of
			//elements in total divided by 3
			int rows = data.length/3;
			
			for(int r = 0; r < rows; r++){
				//for every set of the three...
				//First is the account
				String acc = data[3*r+0];
				if(acc.equals("null"))
					acc = "";
				//Second is the username
				String user = data[3*r + 1];
				if(user.equals("null"))
						user = "";
				//third is the password
				char[] pass = data[3*r + 2].toCharArray();
				if(Arrays.equals(pass, "null".toCharArray()))
					pass = "".toCharArray();
				if(!acc.equals("") && !user.equals("") && !Arrays.equals(pass, "".toCharArray()))
					//makes a call to MainFrame to add a row to the table
					addAccount(acc, user, pass);
			}
		}
	}

	public void addAccount(String account, String user, char[] password){
		mf.addAccount(account, user, password);
	}
	
	public void enableMainFrame(){
		mf.setEnabled(true);
	}
	
	public void deleteRow(int row){
		mf.deleteRow(row);
		enableMainFrame();
	}
	
	public void setAccounts(String[] a, String[] u, String[] p){
		accounts = a;
		users = u;
		passwords = p;
	}

}
