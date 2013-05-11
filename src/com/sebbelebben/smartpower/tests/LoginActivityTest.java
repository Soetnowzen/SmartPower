package com.sebbelebben.smartpower.tests;

import com.jayway.android.robotium.solo.Solo;
import com.sebbelebben.smartpower.LoginActivity;

import android.R;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ProgressBar;

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
	private Solo solo;
	
	public LoginActivityTest() {
		super(LoginActivity.class);
	}
	
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testLogin() throws Exception {
		solo.assertCurrentActivity("wrong activity", LoginActivity.class);
		
		View v = solo.getView(com.sebbelebben.smartpower.R.id.loading_progress); 
		if(v.getVisibility() == View.VISIBLE) {
			
		} else {
			solo.clearEditText(0);
			solo.enterText(0, "android");
			solo.clearEditText(1);
			solo.enterText(1, "android");
			solo.clickOnImageButton(0);
			solo.waitForActivity("MainActivity");
		}
	}
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}