package com.sebbelebben.smartpower.tests;

import com.jayway.android.robotium.solo.Solo;
import com.sebbelebben.smartpower.LoginActivity;

import android.R;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.sax.StartElementListener;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ProgressBar;

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
	private Solo solo;
	
	public LoginActivityTest() {
		super(LoginActivity.class);
	}
	
	public void setUp() throws Exception {
		Instrumentation instrumentation = getInstrumentation();
	 
		// Make sure the preferences are cleared before test
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(instrumentation.getTargetContext());
	    sp.edit().clear().commit();
	    
		solo = new Solo(instrumentation, getActivity());
		super.setUp();
	}
	
	// Does log in work?
	public void testLogin() throws Exception {
		solo.assertCurrentActivity("wrong activity", LoginActivity.class);
		
		// No autologin should occur with cleared preferences
		View v = solo.getView(com.sebbelebben.smartpower.R.id.loading_progress); 
		assertFalse(v.getVisibility() == View.VISIBLE);
		
		// Enter login details for test account
		solo.clearEditText(0);
		solo.enterText(0, "android");
		solo.clearEditText(1);
		solo.enterText(1, "android");
		solo.clickOnImageButton(0);
		
		solo.waitForActivity("MainActivity");
		
		getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
		solo.assertCurrentActivity("wrong activity", LoginActivity.class);
		
		solo.waitForActivity("MainActivity");
	}
	
	// Does the log out function actually log the user out?
	// User story 48567259
	public void testLogout() throws Exception {
		solo.assertCurrentActivity("wrong activity", LoginActivity.class);
		
		// No autologin should occur with cleared preferences
		View v = solo.getView(com.sebbelebben.smartpower.R.id.loading_progress); 
		assertFalse(v.getVisibility() == View.VISIBLE);
		
		// Enter login details for test account
		solo.clearEditText(0);
		solo.enterText(0, "android");
		solo.clearEditText(1);
		solo.enterText(1, "android");
		solo.clickOnImageButton(0);
		
		solo.waitForActivity("MainActivity");
		
		getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
		solo.assertCurrentActivity("wrong activity", LoginActivity.class);
		
		solo.waitForActivity("MainActivity");
		
		solo.sendKey(Solo.MENU);
		solo.clickOnText("Log out");
		
		solo.waitForText("Username", 1, 5000); // This text found => username cleared
	}
	
	// Does the automatic login work after closing the app and reopening it?
	// Part of user story 46902313 (credentials remembered)
	public void testAutomaticLogin() throws Exception {
		solo.assertCurrentActivity("wrong activity", LoginActivity.class);
		
		// No autologin should occur with cleared preferences
		View v = solo.getView(com.sebbelebben.smartpower.R.id.loading_progress); 
		assertFalse(v.getVisibility() == View.VISIBLE);
		
		// Enter login details for test account
		solo.clearEditText(0);
		solo.enterText(0, "android");
		solo.clearEditText(1);
		solo.enterText(1, "android");
		solo.clickOnImageButton(0);
		
		solo.waitForActivity("MainActivity", 5000);
		
		// Close app and reopen
		solo.finishOpenedActivities();
		solo.sleep(1000);
		this.launchActivity("com.sebbelebben.smartpower", LoginActivity.class, null);
		solo.sleep(1000);
		solo.waitForActivity("MainActivity", 5000);
		
	}
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}