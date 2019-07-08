package com.kay.demo.push.testSender;

public class FCMException extends Exception{

	 private final int status;
	 public FCMException(int status){
		 super("Http status code is: "+status);
		 this.status = status;
	 }
}
