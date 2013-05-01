package com.sebbelebben.smartpower.fragments;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;
import com.sebbelebben.smartpower.PowerStrip;
import com.sebbelebben.smartpower.PowerStripActivity;
import com.sebbelebben.smartpower.R;
import com.sebbelebben.smartpower.Server.OnPowerStripReceiveListener;
import com.sebbelebben.smartpower.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class RemoteFragment extends SherlockFragment {
    	private ListView mListView;
	private List<PowerStrip> mPowerStrips = new ArrayList<PowerStrip>();
	private ArrayAdapter<PowerStrip> mAdapter;

	public static RemoteFragment newInstance(User user) {
		RemoteFragment f = new RemoteFragment();
		Bundle args = new Bundle();
		args.putSerializable("User", user);
		f.setArguments(args);
		return f;
	}

	public RemoteFragment() {
	}


	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    User user = (User) getArguments().getSerializable("User");
	    if(user != null) {
		user.getPowerStrips(new OnPowerStripReceiveListener() {
			@Override
			public void onPowerStripReceive(PowerStrip[] powerStrips) {
				for(int i=0; i < powerStrips.length; i++){
					mPowerStrips.add(powerStrips[i]);
				}
				mAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void failed() {
				// TODO Auto-generated method stub
			}
		});
	    }
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_remote, container, false);
		mListView = (ListView) view.findViewById(R.id.listview);
		ProgressBar loadingView = (ProgressBar) view.findViewById(R.id.loading_progress);
		mListView.setEmptyView(loadingView);
		mAdapter = new ArrayAdapter<PowerStrip>(getActivity(), android.R.layout.simple_list_item_1, mPowerStrips);
		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {
		        @Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				PowerStrip powerStrip = (PowerStrip) arg0.getItemAtPosition(arg2);
				Intent intent = new Intent(getActivity(), PowerStripActivity.class);
				intent.putExtra("PowerStrip", powerStrip);
				startActivity(intent);
			}
		});
		return view;
	}
}
