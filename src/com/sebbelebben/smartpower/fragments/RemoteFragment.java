package com.sebbelebben.smartpower.fragments;

import java.util.ArrayList;
import java.util.List;

import com.sebbelebben.smartpower.PowerStrip;
import com.sebbelebben.smartpower.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class RemoteFragment extends Fragment {
    	private ListView mListView;
	private List<PowerStrip> mPowerStrips = new ArrayList<PowerStrip>();

	public static RemoteFragment newInstance() {
		RemoteFragment f = new RemoteFragment();
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
	}

	public RemoteFragment() {
	}


	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    mUser.getPowerStrips(new OnPowerStrip)

	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_remote, container, false);
		mListView = (ListView) view.findViewById(R.id.listview);
		mAdapter = new ArrayAdapter<PowerStrip>(getActivity(), android.R.layout.simple_list_item_1, mPowerStrips);
		return view;
	}
}
