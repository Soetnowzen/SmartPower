package com.sebbelebben.smartpower.fragments;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;
import com.sebbelebben.smartpower.Consumption;
import com.sebbelebben.smartpower.PowerStrip;
import com.sebbelebben.smartpower.PowerStripActivity;
import com.sebbelebben.smartpower.R;
import com.sebbelebben.smartpower.Server.OnConsumptionReceiveListener;
import com.sebbelebben.smartpower.Server.OnPowerStripReceiveListener;
import com.sebbelebben.smartpower.User;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

import android.app.Activity;
import android.content.Context;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
		
		//Registers that this item has a contextMenu
		registerForContextMenu(mListView);
		
		return view;
	}
	
	public class PowerStripAdapter extends ArrayAdapter<PowerStrip>{
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
	    public View getView(final int position, View convertView, ViewGroup parent) {
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
					Toast.makeText(context, "Rename", Toast.LENGTH_SHORT).show();
					RemoteFragment.this.changeName(position);
				}
			});
	        holder.actionBButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "NEW ACTION B", Toast.LENGTH_SHORT).show();
					Date start = new Date(2013, 05, 9);
					Date end = new Date(2013, 05, 10);
					//input dialog
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					final EditText input = new EditText(context);
					alert.setView(input);
					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						  String value = input.getText().toString();
						  // Do something with value!
						  }
						});

						alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						  public void onClick(DialogInterface dialog, int whichButton) {
						    // Canceled.
						  }
						});
						alert.show();
					mPowerStrips.get(0).getConsumption(start, end, new OnConsumptionReceiveListener() {
						
						@Override
						public void onConsumptionReceive(Consumption[] consumption) {
							Toast.makeText(context,"OnConsumptionReceiveListener", Toast.LENGTH_SHORT).show();
							
						}
						
						@Override
						public void failed() {
							// TODO Auto-generated method stub
							Toast.makeText(context, "FAILED TO GET CONSUMPTION", Toast.LENGTH_SHORT).show();
						}
					});
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
			    	mPowerStrips.get(position).setName(result.getText().toString());
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
