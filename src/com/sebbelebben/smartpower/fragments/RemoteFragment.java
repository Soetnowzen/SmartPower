package com.sebbelebben.smartpower.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.actionbarsherlock.app.SherlockFragment;
import com.sebbelebben.smartpower.Consumption;
import com.sebbelebben.smartpower.GraphActivity;
import com.sebbelebben.smartpower.PowerStrip;
import com.sebbelebben.smartpower.PowerStripActivity;
import com.sebbelebben.smartpower.R;
import com.sebbelebben.smartpower.Server.*;
import com.sebbelebben.smartpower.User;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

import android.app.Activity;
import android.content.Context;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class RemoteFragment extends SherlockFragment {
    	private ExpandableListView mListView;
	private ArrayList<PowerStrip> mPowerStrips = new ArrayList<PowerStrip>();
	private ExpandablePowerStripAdapter mAdapter;
	
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

		mUser = (User) getArguments().getSerializable("User");
		if(mUser != null) {
			mUser.getPowerStrips(new OnPowerStripReceiveListener() {

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
		mListView = (ExpandableListView) view.findViewById(R.id.listview);
		ProgressBar loadingView = (ProgressBar) view.findViewById(R.id.loading_progress);
		mListView.setEmptyView(loadingView);
		mAdapter = new ExpandablePowerStripAdapter(getActivity(), mPowerStrips);
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
	public class ExpandablePowerStripAdapter extends BaseExpandableListAdapter {
		private Context context;
		private ArrayList<PowerStrip> groups;
		public ExpandablePowerStripAdapter(Context context, ArrayList<PowerStrip> groups) {
			this.context = context;
			this.groups = groups;
		}
		/*
		public void addItem(PowerStrip item, ExpandListGroup group) {
			if (!groups.contains(group)) {
				groups.add(group);
			}
			int index = groups.indexOf(group);
			ArrayList<ExpandListChild> ch = groups.get(index).getItems();
			ch.add(item);
			groups.get(index).setItems(ch);
		}
		*/
		public Object getChild(int groupPosition, int childPosition) {
			String sockets[] = groups.get(groupPosition).sockets;
			return sockets[childPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
				ViewGroup parent) {
			String child = (String) getChild(groupPosition, childPosition);
			if (view == null) {
				LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
				view = infalInflater.inflate(R.layout.remote_item, null);
			}
			TextView tv = (TextView) view.findViewById(R.id.text);
			tv.setText(child);
			if(childPosition == 0) {
				tv.setBackgroundResource(R.drawable.expandable_bg);
			} else {
				tv.setBackgroundColor(0xFF1F2E33);
			}
			// TODO Auto-generated method stub
			return view;
		}

		public int getChildrenCount(int groupPosition) {

			return groups.get(groupPosition).sockets.length;

		}

		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return groups.get(groupPosition);
		}

		public int getGroupCount() {
			// TODO Auto-generated method stub
			return groups.size();
		}

		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		public View getGroupView(final int groupPosition, boolean isLastChild, View view,
				ViewGroup parent) {
			PowerStrip group = (PowerStrip) getGroup(groupPosition);
			if (view == null) {
				LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
				view = inf.inflate(R.layout.remote_item, null);
			}
			TextView tv = (TextView) view.findViewById(R.id.text);
			tv.setText(group.getName());
			tv.setOnClickListener(new OnClickListener() {
				
>>>>>>> Added initial code for expandablelistview.
				@Override
				public void onClick(View v) {
					if(!mListView.isGroupExpanded(groupPosition)) {
						mListView.expandGroup(groupPosition);
					} else {
						mListView.collapseGroup(groupPosition);
					}
				}
			});
			// TODO Auto-generated method stub
			return view;
		}

		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return true;
		}
	}
	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, v.getId(), 0, "Change Name");
		menu.add(0, v.getId(), 0, "Group together with...");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int position = info.position;
		//Long id = getListAdapter().getItemId(info.position);
		if(item.getTitle() == "Change Name") {
			changeName(position);
		} else if(item.getTitle() == "Group together with...") {
			groupOutlets(item.getItemId());
		} else {
			return false;
		}
		return true;
	}

	private void changeName(final int position) {
		// get prompts.xml view
		Context context = getActivity();
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView = li.inflate(R.layout.popup_rename, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

		final EditText result = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

		// set popup_rename.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		// set dialog message
		alertDialogBuilder.setCancelable(true).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// get user input and set it to mResult
				// edit text


			    	//String s = result.getText().toString();
			    	//mPowerStrips.get(position);
			    	mPowerStrips.get(position).setName(result.getText().toString(), new OnSetNameReceiveListener() {
						
						@Override
						public void onSetNameReceived(String name) {
							mAdapter.notifyDataSetChanged();
						}
						
						@Override
						public void failed() {
							mAdapter.notifyDataSetChanged();
							Toast.makeText(getActivity(), "Rename failed", Toast.LENGTH_SHORT).show();
						}
					});
					mAdapter.notifyDataSetChanged();
			    }
			  })
			.setNegativeButton("Cancel",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {

				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	private void groupOutlets(final int position) {
		Context context = getActivity();
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView = li.inflate(R.layout.popup_group,null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

		final EditText result = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

		//set popup_group.xml to alertDialog builder
		alertDialogBuilder.setView(promptsView);
		//set dialog message
		alertDialogBuilder.setCancelable(true).setPositiveButton("Add",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				mAdapter.notifyDataSetChanged();
			}
		})
		.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		Toast.makeText(getActivity(), "groupOutlets was called", Toast.LENGTH_SHORT).show();
	}
}
