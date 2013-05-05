package com.sebbelebben.smartpower.fragments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;
import com.sebbelebben.smartpower.PowerStrip;
import com.sebbelebben.smartpower.PowerStripActivity;
import com.sebbelebben.smartpower.R;
import com.sebbelebben.smartpower.Server.OnPowerStripReceiveListener;
import com.sebbelebben.smartpower.User;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
		mAdapter = new PowerStripAdapter(getActivity(), R.layout.remote_item, mPowerStrips);
		mListView.setAdapter(new SlideExpandableListAdapter(mAdapter, R.id.text, R.id.expandable));

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
	
	public static class PowerStripAdapter extends ArrayAdapter<PowerStrip>{
	    Context context; 
	    int layoutResourceId;    
	    List<PowerStrip> data = null;
	    
	    public PowerStripAdapter(Context context, int layoutResourceId, List<PowerStrip> data) {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        PowerStripHolder holder = null;
	        
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new PowerStripHolder();
	            holder.txtTitle = (TextView)row.findViewById(R.id.text);
	            holder.toggleButton = (Button)row.findViewById(R.id.toggle_button);
	            holder.actionAButton = (Button)row.findViewById(R.id.action_a_button);
	            holder.actionBButton = (Button)row.findViewById(R.id.action_b_button);
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (PowerStripHolder)row.getTag();
	        }
	        
	        PowerStrip powerStrip = data.get(position);
	        holder.txtTitle.setText(powerStrip.toString());
	        
	        
	        holder.toggleButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "TOGGLE", Toast.LENGTH_SHORT).show();
				}
			});
	        holder.actionAButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "ACTION A", Toast.LENGTH_SHORT).show();
				}
			});
	        holder.actionBButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "ACTION B", Toast.LENGTH_SHORT).show();
				}
			});
	        
	        return row;
	    }
	}
	
	static class PowerStripHolder
    {
        TextView txtTitle;
        Button toggleButton;
        Button actionAButton;
        Button actionBButton;
    }
}
