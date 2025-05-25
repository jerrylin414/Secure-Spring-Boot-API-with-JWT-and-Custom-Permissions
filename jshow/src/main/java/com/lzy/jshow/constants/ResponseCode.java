package com.lzy.jshow.constants;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseCode {

	private String code;

	private String msg;
	
	private int timestamp;

	private Object data;
	
	private String log;
	
	public ResponseCode(ResultCode resultCode){
		this.code = resultCode.code();
		this.msg = resultCode.msg();
		this.timestamp = (int) (System.currentTimeMillis() / 1000);
		this.data = null;
	}
	
	public ResponseCode(ResultCode resultCode, Object data){
		this.code = resultCode.code();
		this.msg = resultCode.msg();
		this.timestamp = (int) (System.currentTimeMillis() / 1000);
		this.data = data;
	}
	
	public ResponseCode(ResultCode resultCode, Object data, String log){
		this.code = resultCode.code();
		this.msg = resultCode.msg();
		this.timestamp = (int) (System.currentTimeMillis() / 1000);
		this.data = data;
		this.log = log;
	}
	
	public ResponseCode(ResultCode resultCode, Object data, String log, String msg){
		this.code = resultCode.code();
		this.msg = msg;
		this.timestamp = (int) (System.currentTimeMillis() / 1000);
		this.data = data;
		this.log = log;
	}
	
	public ResponseCode(String code, Object data, String log, String msg){
		this.code = code;
		this.msg = msg;
		this.timestamp = (int) (System.currentTimeMillis() / 1000);
		this.data = data;
		this.log = log;
	}
	
}
