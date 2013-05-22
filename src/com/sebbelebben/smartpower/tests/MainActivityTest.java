package com.sebbelebben.smartpower.tests;

import java.util.ArrayList;
import java.util.Random;

import junit.framework.Assert;

import com.jayway.android.robotium.solo.Solo;
import com.sebbelebben.smartpower.LoginActivity;
import com.sebbelebben.smartpower.MainActivity;

import android.R;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.sax.StartElementListener;
import android.test.ActivityInstrumentationTestCase2;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
	private Solo solo;
	
	public MainActivityTest() {
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
	
	// Can the user rename a power strip?
	// User story 48027353
	public void testRename() throws Exception {
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
		
		// Rename an outlet
		
		ArrayList<android.widget.TextView> listTexts = solo.clickInList(0);
		String oldName = (String) listTexts.get(0).getText();
		
		solo.clickOnButton("Rename");
		double rand = Math.random() * 10000;
		
		solo.enterText(0, "robotiumTest" + rand);
		solo.clickOnButton("OK");
		
		// Make sure the item is renamed
		solo.waitForText("robotiumTest" + rand, 1, 5000);
		solo.getText("robotiumTest" + rand);
		
		// Rename the outlet back to what it was
		solo.clickOnButton("Rename");
		solo.enterText(0, oldName);
		solo.clickOnButton("OK");
		
	}
	
	public void testRotateScreen() throws Exception {
		// Rotate to landscape
		solo.setActivityOrientation(Solo.LANDSCAPE);
		
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
		
		// Rotate back to portrait after logging in
		solo.setActivityOrientation(Solo.PORTRAIT);
		solo.clickInList(0);
		solo.waitForText("Button", 1, 5000, true);
	}
	
	// Does pressing the back button close the app?
	// User story 49184301
	/* This proved difficult, there is no straightforward way of
	 * checking if the app is closed. Commented out for now, but
	 * could be tested in the future
	 */
	/*
	public void testBackClosesApp() throws Exception {
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
		solo.sleep(1000);
		solo.goBack();
		solo.sleep(1000);
	}
	*/
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
		
	}
}