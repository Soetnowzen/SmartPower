package com.sebbelebben.smartpower;

import android.util.Log;
import com.actionbarsherlock.view.Window;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.sebbelebben.smartpower.fragments.*;
import com.sebbelebben.smartpower.fragments.RemoteFragment.FavoriteListener;
import com.viewpagerindicator.TitlePageIndicator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends SherlockFragmentActivity implements FavoriteListener{
	private ViewPager mPager;
	private MainPagerAdapter mAdapter;
	private User mUser;
	private UserFragment userFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
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

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_logout:
	        	// Create a new Intent so that history is cleared
	        	Intent loginIntent = new Intent(this, LoginActivity.class);
	        	loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	mUser.logOut();
	        	// Write empty user details to SharedPreferences
	        	saveUserToPreferences();
	        	startActivity(loginIntent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private class MainPagerAdapter extends FragmentPagerAdapter {
		private final String[] CONTENT = {
                getResources().getString(R.string.remotefragment_title),
                getResources().getString(R.string.userfragment_title),
        };
		private final int COUNT = 2;

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
                Log.i("SmartPower", "CREATING FRAGMENT");
				fragment = UserFragment.newInstance(mUser);
				userFragment = (UserFragment) fragment;
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
	
	private void saveUserToPreferences() {
		JSONObject jUser = new JSONObject();
		try {
			jUser.put("Username", mUser.getUserName());
			jUser.put("Password", mUser.getPassword());
			jUser.put("Logged in", mUser.loginStatus());
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    		Editor edit = sp.edit();
    		edit.putString("USER", jUser.toString());
    		edit.commit();
		} catch (JSONException e) {
			e.printStackTrace();
		}	
	}

	@Override
    /**
     * Called whenever favorites has changed.
     *
     * This method is totally a hack. There is no guarantee that viewpager fragments are stored with the used tag in
     * future android versions. Also, if the fragment layout is changed in the viewpager,
     * then this will probably not function properly. Use with caution, and party hard.
     */
	public void notifyFavoriteChanged() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentByTag("android:switcher:"+R.id.viewpager+":1");

        if(f != null && f instanceof UserFragment) {
            ((UserFragment) f).updateFavorites();
        }
	}
}
