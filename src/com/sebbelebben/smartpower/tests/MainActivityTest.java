package com.sebbelebben.smartpower.tests;

import com.jayway.android.robotium.solo.Solo;
import com.sebbelebben.smartpower.MainActivity;

import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;
	
	public MainActivityTest() {
		super(MainActivity.class);
	}
	
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testActivity() throws Exception {
		solo.assertCurrentActivity("wrong activity", MainActivity.class);
	}
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}