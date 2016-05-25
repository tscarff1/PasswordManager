package com.pwdman;

public class Logger {
	
	public Logger(){
		
	}
	
	public static void info(String info){
		System.out.println("[INFO]" + info);
	}
	
	public static void info(byte info){
		info(info + "");
	}
	
	public static void info(byte[] info){
		System.out.print("[INFO] ");
		for(int i = 0; i < info.length; i++){
			System.out.print(info[i]);
		}
		System.out.println();
	}
	
	public static void warn(String warn){
		System.out.println("[WARN]" + warn);
	}
	
	public static void error(String err){
		System.out.println("[ERROR]" + err);
	}
}
