package com.sebbelebben.smartpower.tests;

import java.util.concurrent.CyclicBarrier;

import android.test.AndroidTestCase;

import com.sebbelebben.smartpower.PowerStrip;
import com.sebbelebben.smartpower.PsSocket;
import com.sebbelebben.smartpower.Server.GenericListener;
import com.sebbelebben.smartpower.Server.GenericStringListener;
import com.sebbelebben.smartpower.Server.OnUpdateListener;

public class PowerStripTest extends AndroidTestCase {
	private PowerStrip powerStrip;
	private CyclicBarrier barrier3 = new CyclicBarrier(3);
	private CyclicBarrier barrier2 = new CyclicBarrier(2);

	public PowerStripTest() throws Exception {
		super();
		this.powerStrip = new PowerStrip(5, "SN-ANDRO2", "apikey1011", "Grendosan 2", null, null);
	}
	
	public PowerStripTest(PowerStrip powerStrip){
		this.powerStrip = powerStrip;
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		powerStrip.updatePowerStrip(new GenericListener() {
			
			@Override
			public void success() {
				assertEquals(4, powerStrip.getSockets().length);
				try {
					barrier3.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void failed() {
				assertFalse(true);
				try {
					barrier3.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		powerStrip.updateStatus(new OnUpdateListener() {
			
			@Override
			public void onUpdateReceive(Boolean status) {
				assertEquals(status, powerStrip.getStatus());
				try {
					barrier3.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void failed() {
				assertFalse(true);
				try {
					barrier3.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		barrier3.await();
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
				try {
					barrier2.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void failed() {
				assertEquals(current_name, powerStrip.getName());
				try {
					barrier2.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		barrier2.await();
		powerStrip.setName(current_name, new GenericStringListener() {
			
			@Override
			public void success(String name) {
				assertEquals(current_name, powerStrip.getName());
				try {
					barrier2.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void failed() {
				assertEquals(test_name, powerStrip.getName());
				try {
					barrier2.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		barrier2.await();
	}
	
	public void testJSON(){
		String PsSocketsJSON = "";
		System.out.println(PsSocketsJSON);
		PsSocket[] psSockets = powerStrip.getSockets();
		for(int i = 0; i < psSockets.length; i++){
			if(i != 1){
				PsSocketsJSON.concat(",");
			}
			PsSocketsJSON.concat(psSockets[i].toJSON());
		}
		System.out.println(PsSocketsJSON);
		String JSON = 
			"{" +
				"\"id\":"+Integer.toString(powerStrip.getId())+"," +
				"\"serialId\":\""+powerStrip.toString()+"\"," +
				"\"name\":\""+powerStrip.getName()+"\"," +
				"\"pssockets\":["+PsSocketsJSON+"]" +
			"}";
		assertEquals(JSON, powerStrip.toJSON());
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
