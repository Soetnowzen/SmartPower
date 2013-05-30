package com.sebbelebben.smartpower.tests;

import android.test.AndroidTestCase;

import com.sebbelebben.smartpower.PsSocket;
import com.sebbelebben.smartpower.Server.GenericListener;
import com.sebbelebben.smartpower.Server.GenericStringListener;

public class PsSocketTest extends AndroidTestCase {
	private PsSocket socket;
	private Boolean current_status;
	
	public PsSocketTest(){
		super();
	}
	
	public PsSocketTest(PsSocket socket) {
		super();
		this.socket = socket;	
	}

	protected void setUp() throws Exception {
		super.setUp();
		if(socket == null){
			socket = new PsSocket(17,"PsTestStart","apikey1011",false);
		}
	}
	
	public void testSetName(){
		
		socket.setName("PsTestChange", new GenericStringListener() {
			
			@Override
			public void success(String name) {
				assertEquals("PsTestChange", socket.getName());
			}
			
			@Override
			public void failed() {
				assertEquals("PsTestStart", socket.getName());
			}
		});
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void testTurnOn(){
		current_status = socket.getStatus();
		socket.turnOn(new GenericListener() {
			
			@Override
			public void success() {
				assertEquals(Boolean.TRUE,socket.getStatus());
			}
			
			@Override
			public void failed() {
				assertEquals(current_status, socket.getStatus());
			}
		});
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void testTurnOff(){
		current_status = socket.getStatus();
		socket.turnOff(new GenericListener() {
			
			@Override
			public void success() {
				assertEquals(Boolean.FALSE,socket.getStatus());
			}
			
			@Override
			public void failed() {
				assertEquals(current_status, socket.getStatus());
			}
		});
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void testToJSON() {
		String JSON = "{\"id\":17,\"name\":\""+socket.getName()+"\",\"apikey\":\"apikey1011\",\"status\":"+socket.getStatus()+"}";
		assertEquals(JSON, socket.toJSON());
	}
	
	public void testId(){
		assertEquals(17, socket.getId());
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	

}
