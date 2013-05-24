package com.sebbelebben.smartpower.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.util.Log;
import com.actionbarsherlock.app.SherlockFragment;
import com.sebbelebben.smartpower.Consumption;
import com.sebbelebben.smartpower.GraphActivity;

import android.util.Log;
import com.actionbarsherlock.app.SherlockFragment;
import com.sebbelebben.smartpower.PowerStrip;
import com.sebbelebben.smartpower.PowerStripActivity;
import com.sebbelebben.smartpower.R;
import com.sebbelebben.smartpower.Server.*;
import com.sebbelebben.smartpower.User;

import android.content.Context;
import android.app.AlertDialog;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * Fragment to display {@link PowerStrip} and {@link com.sebbelebben.smartpower.PsSocket}.
 */
public class RemoteFragment extends SherlockFragment {
    private ExpandableListView mListView;
	private ArrayList<PowerStrip> mPowerStrips = new ArrayList<PowerStrip>();
	private ExpandablePowerStripAdapter mAdapter;

    /**
     * Creates a new instance of this fragment, using the provided {@link User} to list the {@link PowerStrip} and
     * {@link com.sebbelebben.smartpower.PsSocket}.
     *
     * @param user The User used to display information.
     * @return A new instance of RemoteFragment.
     */
	public static RemoteFragment newInstance(User user) {
		RemoteFragment f = new RemoteFragment();
		Bundle args = new Bundle();
		args.putSerializable("User", user);
		f.setArguments(args);
		return f;
	}

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    // Retrieve the user from the intent
	    User user = (User) getArguments().getSerializable("User");
	    
	    // If a user was provided, get the powerstrips
	    if(user != null) {
            user.getPowerStrips(new OnPowerStripReceiveListener() {
                @Override
                public void onPowerStripReceive(PowerStrip[] powerStrips) {
                    // Add the power strips to the list
                    for(int i=0; i < powerStrips.length; i++){
                        mPowerStrips.add(powerStrips[i]);
                    }
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void failed() {
                    Toast.makeText(getActivity(), "Failed to connect to the internet. Please check your internet " +
                            "connectivity.", Toast.LENGTH_SHORT).show();
                }
            });
	    }
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the view
		View view = inflater.inflate(R.layout.fragment_remote,
                container, false);

		mListView = (ExpandableListView) view.findViewById(R.id.listview);
		ProgressBar loadingView = (ProgressBar) view.findViewById(R.id.loading_progress);
		mListView.setEmptyView(loadingView);
		mAdapter = new ExpandablePowerStripAdapter(getActivity(),mPowerStrips);
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

    /**
     * Adapter for displaying a {@link PowerStrip}.
     */
	public class ExpandablePowerStripAdapter extends BaseExpandableListAdapter {
		private Context context;
		private ArrayList<PowerStrip> groups;
		public ExpandablePowerStripAdapter(Context context, ArrayList<PowerStrip> groups) {
			this.context = context;
			this.groups = groups;
		}

		public Object getChild(int groupPosition, int childPosition) {
			String sockets[] = groups.get(groupPosition).sockets;
			return sockets[childPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public View getChildView(int groupPos, int childPos, boolean isLastChild, View view, ViewGroup parent) {
			String child = (String) getChild(groupPos, childPos);
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.socket_item, null);
			}

			TextView tv = (TextView) view.findViewById(R.id.text);
			tv.setText(child);
			tv.setTextColor(Color.WHITE);
			
			// If the child is at top, set the background to the drawable with shadow.
			if(childPos == 0) {
                Log.i("SmartPower", child);
                view.setBackgroundResource(R.drawable.expandable_bg);
			}
			
			setupRemoteItem(view);
            setupOptionsItem(view);
			
			return view;
		}
		private void setupRemoteItem(View view) {
			ImageButton optionsButton = (ImageButton) view.findViewById(R.id.options_button);
			ImageButton backButton = (ImageButton) view.findViewById(R.id.back_btn);
			final ViewFlipper flipper = (ViewFlipper) view.findViewById(R.id.viewflipper);
			optionsButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
                    // Flip over to the Options view with the correct animations.
					flipper.setInAnimation(getActivity(), R.anim.slide_in_right);
					flipper.setOutAnimation(getActivity(), R.anim.slide_out_left);
					flipper.setDisplayedChild(1);
				}
			});
		    backButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
                    // Flip back to the Remote view with the correct animations.
					flipper.setInAnimation(getActivity(), R.anim.slide_in_left);
					flipper.setOutAnimation(getActivity(), R.anim.slide_out_right);
					flipper.setDisplayedChild(0);
				}
			});
		}

        private void setupOptionsItem(View view) {
            ImageButton renameButton = (ImageButton) view.findViewById(R.id.rename_btn);
            ImageButton favoriteButton = (ImageButton) view.findViewById(R.id.favorite_btn);
            ImageButton consumptionButton = (ImageButton) view.findViewById(R.id.consumption_btn);

            renameButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Rename", Toast.LENGTH_SHORT).show();
                }
            });

            favoriteButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            consumptionButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

		public int getChildrenCount(int groupPosition) {
			return groups.get(groupPosition).sockets.length;
		}

		public Object getGroup(int groupPosition) {
			return groups.get(groupPosition);
		}

		public int getGroupCount() {
			return groups.size();
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(final int groupPos, boolean isLastChild, View view,
				ViewGroup parent) {
			PowerStrip group = (PowerStrip) getGroup(groupPos);

            // If view is null, inflate it.
			if (view == null) {
				LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
				view = inf.inflate(R.layout.powerstrip_item, null);
			}

            // Set the text of the textview to the powerstrip name.
			TextView tv = (TextView) view.findViewById(R.id.text);
			tv.setText(group.getName());
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
                    // Expand or collapse group when textview is pressed.
					if(!mListView.isGroupExpanded(groupPos)) {
						mListView.expandGroup(groupPos);
					} else {
						mListView.collapseGroup(groupPos);
					}
				}
			});
			
			setupRemoteItem(view);
            setupOptionsItem(view);

			return view;
		}

		public boolean hasStableIds() {
			return true;
		}

		public boolean isChildSelectable(int arg0, int arg1) {
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
		View promptsView = li.inflate(R.layout.popup_group, null);
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
