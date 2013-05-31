package com.sebbelebben.smartpower.tests;

import android.test.AndroidTestCase;

import com.sebbelebben.smartpower.PowerStrip;
import com.sebbelebben.smartpower.PsSocket;
import com.sebbelebben.smartpower.Server.GenericListener;
import com.sebbelebben.smartpower.Server.GenericStringListener;
import com.sebbelebben.smartpower.Server.OnUpdateListener;

public class PowerStripTest extends AndroidTestCase {
	private PowerStrip powerStrip;

	public PowerStripTest(){
		super();
		this.powerStrip = new PowerStrip(5, "SN-ANDRO2", "apikey1011", "SN-ANDRO2", null, null);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		powerStrip.updatePowerStrip(new GenericListener() {
			
			@Override
			public void success() {
			}
			
			@Override
			public void failed() {
			}
		});
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void testId(){
		assertEquals(5, powerStrip.getId());
	}
	
	public void testSerialId(){
		assertEquals("SN-ANDRO2", powerStrip.toString());
	}
	
	public void testName() throws Exception {
		final String current_name = powerStrip.getName();
		final String test_name = "TestPowerStripName";
		powerStrip.setName(test_name, new GenericStringListener() {
			
			@Override
			public void success(String name) {
				assertEquals(test_name, powerStrip.getName());
			}
			
			@Override
			public void failed() {
				assertEquals(current_name, powerStrip.getName());
			}
		});
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		powerStrip.setName(current_name, new GenericStringListener() {
			
			@Override
			public void success(String name) {
				assertEquals(current_name, powerStrip.getName());
			}
			
			@Override
			public void failed() {
				assertEquals(test_name, powerStrip.getName());
			}
		});
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void testStatus(){
		powerStrip.updateStatus(new OnUpdateListener() {
			
			@Override
			public void onUpdateReceive(Boolean status) {
				assertEquals(status, powerStrip.getStatus());
			}
			
			@Override
			public void failed() {
				assertFalse(true);
			}
		});
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
