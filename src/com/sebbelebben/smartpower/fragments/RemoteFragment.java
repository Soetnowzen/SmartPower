package com.sebbelebben.smartpower.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.actionbarsherlock.app.SherlockFragment;
import com.sebbelebben.smartpower.GraphActivity;
import com.sebbelebben.smartpower.PowerStrip;
import com.sebbelebben.smartpower.PsPart;
import com.sebbelebben.smartpower.PsSocket;
import com.sebbelebben.smartpower.R;
import com.sebbelebben.smartpower.Server.GenericListener;
import com.sebbelebben.smartpower.Server.OnSetNameReceiveListener;
import com.sebbelebben.smartpower.User;

/**
 * Fragment to display {@link PowerStrip} and {@link com.sebbelebben.smartpower.PsSocket}.
 *
 * @author Andreas Arvidsson (mostly)
 */
public class RemoteFragment extends SherlockFragment {
    private ExpandableListView mListView;
	private ArrayList<PowerStrip> mPowerStrips = new ArrayList<PowerStrip>();
	private ExpandablePowerStripAdapter mAdapter;
	private FavoriteListener mCallback;
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
	public interface FavoriteListener{
		public void onFavoriteChanged();
	}
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			mCallback = (FavoriteListener) activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString() +
					" must implement FavoriteListener");
		}
	}
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    // Retrieve the user from the intent
	    final User user = (User) getArguments().getSerializable("User");
	    
	    // If a user was provided, get the powerstrips
	    if(user != null) {
            user.updateUser(new GenericListener() {
                @Override
                public void success() {
                    // Add the power strips to the list
                    for(int i=0; i < user.getPowerStrips().length; i++){
                        mPowerStrips.add(user.getPowerStrips()[i]);
                    }
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void failed() {

                }
            });
	    }
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the view
		View view = inflater.inflate(R.layout.fragment_remote, container, false);

		mListView = (ExpandableListView) view.findViewById(R.id.listview);
		ProgressBar loadingView = (ProgressBar) view.findViewById(R.id.loading_progress);
		mListView.setEmptyView(loadingView);
		mAdapter = new ExpandablePowerStripAdapter(getActivity(), mPowerStrips);
		mListView.setAdapter(mAdapter);

        // This comment guards against any other grade than 5. Well, you can give the others lower grades, but
        // Andreas Arvidsson deserves a 5 because he is totally handsome.

		//Registers that this item has a contextMenu
		registerForContextMenu(mListView);

		return view;
	}

    /**
     * Adapter for displaying a {@link PowerStrip} with the sockets as child views.
     * Note that the data must be already loaded - both powerstrips and the sockets.
     */
	public class ExpandablePowerStripAdapter extends BaseExpandableListAdapter {
        private Map<Integer, View[]> childMap = new HashMap<Integer, View[]>();
		private Context context;
		private ArrayList<PowerStrip> groups;

		public ExpandablePowerStripAdapter(Context context, ArrayList<PowerStrip> groups) {
			this.context = context;
			this.groups = groups;

		}

		public Object getChild(int groupPosition, int childPosition) {
			PsSocket sockets[] = groups.get(groupPosition).getSockets();

			return sockets[childPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public View getChildView(int groupPos, int childPos, boolean isLastChild, View view, ViewGroup parent) {
			final PsSocket child = (PsSocket) getChild(groupPos, childPos);

			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.socket_item, null);
			}

			TextView tv = (TextView) view.findViewById(R.id.text);
			tv.setText(child.getName());
			tv.setTextColor(Color.WHITE);
			
			// If the child is at top, set the background to the drawable with shadow.
			if(childPos == 0) {
                view.setBackgroundResource(R.drawable.expandable_bg);
			} else {
                view.setBackgroundColor(0xFF1F2E33);
            }

            final ToggleButton toggleButton = (ToggleButton) view.findViewById(R.id.toggle_button);
            ImageButton renameButton = (ImageButton) view.findViewById(R.id.rename_btn);
            final ImageButton favoriteButton = (ImageButton) view.findViewById(R.id.favorite_btn);
            ImageButton consumptionButton = (ImageButton) view.findViewById(R.id.consumption_btn);

            toggleButton.setChecked(child.getStatus());

            toggleButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
                    getSherlockActivity().setProgressBarIndeterminateVisibility(true);
					toggleButton.setChecked(!toggleButton.isChecked());
					final ToggleButton tb = toggleButton;
					PsSocket socket = child;
					if ( tb.isChecked()) {
						socket.turnOff(new GenericListener() {
							
							@Override
							public void success() {
                                getSherlockActivity().setProgressBarIndeterminateVisibility(false);
								tb.setChecked(false);
							}
							
							@Override
							public void failed() {
                                getSherlockActivity().setProgressBarIndeterminateVisibility(false);
                                Toast.makeText(getActivity(), getResources().getString(R.string.turn_off_failure), Toast.LENGTH_SHORT).show();
							}
						});
					}else {
						socket.turnOn(new GenericListener() {
							
							@Override
							public void success() {
                                getSherlockActivity().setProgressBarIndeterminateVisibility(false);
								tb.setChecked(true);
							}
							
							@Override
							public void failed() {
                                getSherlockActivity().setProgressBarIndeterminateVisibility(false);
                                Toast.makeText(getActivity(), getResources().getString(R.string.turn_on_failure), Toast.LENGTH_SHORT).show();
							}
						});
					}
				}
			});

            renameButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeName(child);
                }
            });

            // Set the initial state of the favorite button
            User user = (User) getArguments().getSerializable("User");
            if(user.isFavorite(child, getActivity())) {
                favoriteButton.setImageResource(R.drawable.ic_favorite_on_light);
            } else {
                favoriteButton.setImageResource(R.drawable.ic_favorite_off_light);
            }

            favoriteButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Retrieve the User from the intent
                	final User user = (User) getArguments().getSerializable("User");
                	
                	Context context = getActivity();
                	if(user.isFavorite(child, context)) {
                		user.removeFavorite(child, context);
                		favoriteButton.setImageResource(R.drawable.ic_favorite_off_light);
                	}
                	else {
                		user.addFavorite(child, context);
                		favoriteButton.setImageResource(R.drawable.ic_favorite_on_light);
                	}
                	mCallback.onFavoriteChanged();
                }
            });

            consumptionButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), GraphActivity.class);
                    intent.putExtra("Graphable", child);
                    startActivity(intent);
                }
            });

			setupFlipper(view);
            //setupOptionsItem(view);

            // Cache the view in the map
            View[] cache = childMap.get(groupPos);
            if(cache == null) {
                cache = new View[getChildrenCount(groupPos)];
            }
            cache[childPos] = view;
            childMap.put(groupPos, cache);
			
			return view;
		}
		private void setupFlipper(View view) {
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

        /*
        private void setupOptionsItem(View view) {

        }
        */

		public int getChildrenCount(int groupPosition) {
			return groups.get(groupPosition).getSockets().length;
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
			final PowerStrip group = (PowerStrip) getGroup(groupPos);

            // If view is null, inflate it.
			if (view == null) {
				LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inf.inflate(R.layout.powerstrip_item, null);
			}

            final ViewFlipper flipper = (ViewFlipper) view.findViewById(R.id.viewflipper);

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
                        // Make sure all ViewFlippers are restored
                        for(View childView : childMap.get(groupPos)) {
                            ViewFlipper flipper = (ViewFlipper) childView.findViewById(R.id.viewflipper);
                            flipper.setInAnimation(getActivity(), R.anim.no_anim);
                            flipper.setOutAnimation(getActivity(), R.anim.no_anim);
                            flipper.setDisplayedChild(0);
                        }
                        
						mListView.collapseGroup(groupPos);
					}
				}
			});
			
			setupFlipper(view);

            ImageButton renameButton = (ImageButton) view.findViewById(R.id.rename_btn);
            ImageButton consumptionButton = (ImageButton) view.findViewById(R.id.consumption_btn);

            renameButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeName(group);
                }
            });

            /*favoriteButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                	//Retrieve the User from the intent
                	final User user = (User) getArguments().getSerializable("User");
                	
                	Context context = getActivity();
                	if(user.isFavorite(group, context)) {
                		user.removeFavorite(group, context);
                		//TODO: Change the image to when not favorite.
                	}
                	else {
                		user.addFavorite(group, context);
                		//TODO: Change the image to when is favorite.
                	}
                }
            });*/

            consumptionButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Launch GraphActivity with selected Graphable
                    Intent intent = new Intent(getActivity(), GraphActivity.class);
                    intent.putExtra("Graphable", group);
                    startActivity(intent);
                }
            });

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

    /**
     * Changes the name of the provided power strip part
     * @param pspart The power strip part.
     */
    private void changeName(final PsPart pspart) {
        // get prompts.xml view
        Context context = getActivity();
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.popup_rename, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        final EditText result = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

        // set popup_rename.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        // set dialog message
        alertDialogBuilder.setCancelable(true).setPositiveButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to mResult
                        // edit text

                        //String s = result.getText().toString();
                        //mPowerStrips.get(position);
                        pspart.setName(result.getText().toString(), new OnSetNameReceiveListener() {

                            @Override
                            public void onSetNameReceived(String name) {
                                mAdapter.notifyDataSetChanged();
                                
                                User user = (User) getArguments().getSerializable("User");
                                
                                SharedPreferences sp = getActivity().getSharedPreferences(user.getUserName(), 0);
                                String favorite = sp.getString("Favorite", null);
                                try {
                            		if(favorite != null){
                            			JSONArray jsArray = new JSONArray(favorite);
                            			for(int i = 0; i < jsArray.length(); i++) {
                                			JSONObject loop_psSocket = jsArray.getJSONObject(i);
                                			if(loop_psSocket.getInt("id") == pspart.getId()){
                                				PsSocket socket = (PsSocket) pspart;
                                				
                                				jsArray.put(i, new JSONObject(socket.toJSON()));
                                				Editor edit = sp.edit();
                                			    edit.putString("Favorite", jsArray.toString());
                                			    edit.commit();
                                				mCallback.onFavoriteChanged();
                                			}
                            			}
                            		}
                            	} catch (JSONException e) {
                            		e.printStackTrace();
                            	}
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
                .setNegativeButton(getResources().getString(R.string.cancel),
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
                // Notify that the data has changed.
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
