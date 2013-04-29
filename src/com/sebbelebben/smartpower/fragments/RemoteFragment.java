package com.sebbelebben.smartpower.fragments;

import java.util.ArrayList;
import java.util.List;

import com.sebbelebben.smartpower.PowerStrip;
import com.sebbelebben.smartpower.PowerStripActivity;
import com.sebbelebben.smartpower.R;
import com.sebbelebben.smartpower.Server.OnPowerStripReceiveListener;
import com.sebbelebben.smartpower.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class RemoteFragment extends Fragment {
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
		
		//Registers that this item has a contextMenu
		registerForContextMenu(mListView);
		
		return view;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, v.getId(), 0, "Change Name");
		menu.add(0, v.getId(), 0, "Group together with...");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getTitle() == "Change Name") {
			//function1(item.getItemId());
		} else if(item.getTitle() == "Group together with...") {
			//function2(item.getItemId());
		} else {
			return false;
		}
		return true;
	}
	
	private void function1(int id) {
		Toast.makeText(getActivity(), "Function1 was called", Toast.LENGTH_SHORT).show();
	}
	
	private void function2(int id) {
		Toast.makeText(getActivity(), "Function2 was called", Toast.LENGTH_SHORT).show();
	}
}
