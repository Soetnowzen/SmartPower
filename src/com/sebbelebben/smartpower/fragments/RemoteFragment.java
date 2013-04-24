package com.sebbelebben.smartpower.fragments;

import com.sebbelebben.smartpower.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RemoteFragment extends Fragment {
	public static RemoteFragment newInstance() {
		RemoteFragment f = new RemoteFragment();
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
	}

	public RemoteFragment() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user, container, false);
		return view;
	}
}
