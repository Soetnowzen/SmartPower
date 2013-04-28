package com.sebbelebben.smartpower.fragments;

import com.sebbelebben.smartpower.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ConsumptionFragment extends Fragment {
	public static ConsumptionFragment newInstance() {
		ConsumptionFragment f = new ConsumptionFragment();
		Bundle args = new Bundle();
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