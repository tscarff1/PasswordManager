package com.pwdman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;



public class IO {
	
	private final String FILENAME;
	private final String WINDOWFILE;
	private final String MACFILE;
	private final String UNIXFILE;
	
	private FileOutputStream output;
	private FileInputStream input;
	
	private File file;
	public boolean fileCreated;
	
	public IO(String filename){
		FILENAME = "PasswordManager" + File.separator + filename;
		WINDOWFILE = System.getenv("APPDATA") + File.separator + FILENAME;
		MACFILE = System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support"
				+ File.separator + FILENAME;
		UNIXFILE = "/var" + File.separator + "lib" + File.separator + FILENAME;
		createFile();
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
	
	public void writeToFile(byte[] data){
		setStreamForWrite();
		try {
			Logger.info("Writing to file: ");
			Logger.debug(data);
			output.write(data);
			output.close();
		} catch (IOException e) {
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
	
	public byte[] readFromFile(){
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
			return data;
		}
		//return null if the file does not exist
		return null;
	}
	
	private void createFile(){
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
		Logger.info("Created file at " + file.getAbsolutePath());
	}
	
	public boolean fileExists(){
		return file.exists();
	}
}
