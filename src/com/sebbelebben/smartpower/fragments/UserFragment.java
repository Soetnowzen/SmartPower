package com.sebbelebben.smartpower.fragments;

import com.actionbarsherlock.app.SherlockFragment;
import com.sebbelebben.smartpower.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UserFragment extends SherlockFragment {
	public static UserFragment newInstance() {
		UserFragment f = new UserFragment();
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
	}

	public UserFragment() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user, container, false);
		return view;
	}
}
