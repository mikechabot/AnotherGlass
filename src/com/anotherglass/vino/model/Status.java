package com.anotherglass.vino.model;

import java.util.Arrays;

public class Status{
   	private String[] Messages;
   	private int ReturnCode;
   	
	public String getMessages() {
		return Arrays.toString(Messages);
	}
	public void setMessages(String[] messages) {
		this.Messages = messages;
	}
	public int getReturnCode() {
		return ReturnCode;
	}
	public void setReturnCode(int returnCode) {
		this.ReturnCode = returnCode;
	}
	
	@Override
	public String toString() {
		return "Status [messages=" + Arrays.toString(Messages)
				+ ", returnCode=" + ReturnCode + "]";
	}
	
}
