package com.sebbelebben.smartpower;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.sebbelebben.smartpower.fragments.*;
import com.viewpagerindicator.TitlePageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends SherlockFragmentActivity {
	private ViewPager mPager;
	private MainPagerAdapter mAdapter;
	private User mUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Get user from intent
		mUser = (User) getIntent().getSerializableExtra("User");

		mPager = (ViewPager) findViewById(R.id.viewpager);
		mAdapter = new MainPagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);

		//Bind the title indicator to the adapter
		TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.titles);
		titleIndicator.setViewPager(mPager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
			getSupportMenuInflater().inflate(R.menu.main, menu);
			return true;
	}

	private class MainPagerAdapter extends FragmentPagerAdapter {
		private final String[] CONTENT = { "Remote", "User", "Consumption" };
		private final int COUNT = 3;

		public MainPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;

		    	switch(position) {
			case 0:
				fragment = RemoteFragment.newInstance(mUser);
				break;
			case 1:
				fragment = UserFragment.newInstance();
				break;
			case 2:
				fragment = ConsumptionFragment.newInstance();
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return COUNT;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length];
		}

	}
}
