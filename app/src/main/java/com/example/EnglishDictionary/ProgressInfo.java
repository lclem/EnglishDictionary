package com.example.EnglishDictionary;

import android.widget.Toast;

public class ProgressInfo {
	
	Integer n;
	String status;
	boolean toast = false;
	int howLong = Toast.LENGTH_SHORT;
	
	public ProgressInfo(int n) {
		this.n = n;
	}
	
	public ProgressInfo(String status) {
		this.status = status;
	}
	
	public ProgressInfo(String status, boolean toast) {
		this.status = status;
		this.toast = toast;
	}
	
	public ProgressInfo(String status, boolean toast, int howLong) {
		this.status = status;
		this.toast = toast;
		this.howLong = howLong;
	}
	
}