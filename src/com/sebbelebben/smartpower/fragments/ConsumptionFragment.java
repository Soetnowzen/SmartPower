package com.sebbelebben.smartpower.fragments;

import com.actionbarsherlock.app.SherlockFragment;
import com.sebbelebben.smartpower.R;
import com.sebbelebben.smartpower.User;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ConsumptionFragment extends SherlockFragment {
	
	public static ConsumptionFragment newInstance(User user) {
		ConsumptionFragment f = new ConsumptionFragment();
		Bundle args = new Bundle();
		args.putSerializable("User", user);
		f.setArguments(args);
		return f;
	}

	public ConsumptionFragment() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_consumption, container, false);
		return view;
	}
}
