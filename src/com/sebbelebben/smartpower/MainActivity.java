package com.sebbelebben.smartpower;

import com.sebbelebben.smartpower.fragments.*;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	private ViewPager mPager;
	private MainPagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPager = (ViewPager) findViewById(R.id.viewpager);
		mAdapter = new MainPagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
	}

	private class MainPagerAdapter extends FragmentStatePagerAdapter {
		private static final int COUNT = 3;

		public MainPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;

		    	switch(position) {
			case 0:
				fragment = RemoteFragment.newInstance();
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

	}

}
