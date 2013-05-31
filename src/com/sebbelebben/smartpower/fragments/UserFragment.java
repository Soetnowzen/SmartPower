package com.sebbelebben.smartpower.fragments;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.sebbelebben.smartpower.*;
import com.sebbelebben.smartpower.Server.GenericListener;

/**
 * Fragment to display user information, 
 * users favorite sockets {com.sebbelebben.smartpower.PsSocket}
 * and a graph of your consumption
 * @author henning
 *
 */
public class UserFragment extends SherlockFragment {
	ArrayList<PsSocket> list;
	SocketAdapter mAdapter;
	ListView listView;
    GraphView graphView;
    User mUser;
	
    /**
     * Creates a new instance of this fragment, using the provided {@link User} to 
     * list the {@link com.sebbelebben.smartpower.PsSocket}
     *
     * @param user The User used to display information.
     * @return A new instance of UserFragment.
     */
	public static UserFragment newInstance(User user) {
		UserFragment f = new UserFragment();
		Bundle args = new Bundle();
		args.putSerializable("User", user);
        f.setRetainInstance(true);
		f.setArguments(args);
		return f;
	}

	public UserFragment() {
	}
	public void FavoriteChanged(){
		list.clear();
		list.addAll(mUser.getFavorite(getActivity()));
		mAdapter.notifyDataSetChanged();
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mUser = (User) getArguments().getSerializable("User");

        final List<Consumption> data = new ArrayList<Consumption>();
        mUser.getConsumption(Duration.HOUR,12,new Server.OnConsumptionReceiveListener() {
            @Override
            public void onConsumptionReceive(Consumption[] consumption) {
                Collections.addAll(data, consumption);
                GraphActivity.display(data, graphView);
            }

            @Override
            public void failed() {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        
		Resources res = getResources();
		View view = inflater.inflate(R.layout.fragment_user, container, false);
		graphView = (GraphView) view.findViewById(R.id.graphview);
		String header = res.getString(R.string.favorites);
		SpannableString spanString = new SpannableString(header);
		spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
		spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
		((TextView) view.findViewById(R.id.text)).setText(spanString);
		//String str = String.format(res.getString(R.string.userInfo), user.getUserName(), user.getPassword());
		
		listView = (ListView) view.findViewById(R.id.listView);

//		String str = String.format(res.getString(R.string.userInfo), user.getUserName(), user.getPassword());
//		((TextView) view.findViewById(R.id.textView)).setText(str);

		list = new ArrayList<PsSocket>();
		list = mUser.getFavorite(getActivity());
		/*list.add(new PsSocket(13, "hennig", "apikey1011", true));
		list.add(new PsSocket(13, "hennigphan123456711111111111111111111111111111", "apikey", true));
		list.add(new PsSocket(13, "hennig2", "apikey1011", true));
		list.add(new PsSocket(13, "hennig3", "apikey1011", true));
		list.add(new PsSocket(13, "hennig4", "apikey1011", true));
		list.add(new PsSocket(13, "hennig5", "apikey1011", true));*/
		Log.d("bug", "confirmation of nothing");
		mAdapter = new SocketAdapter(getActivity(), R.layout.powerstrip_item, list);
		if(listView != null)
			listView.setAdapter(mAdapter);
		return view;
	}

    /**
     *  Gets the maximum watt from the list of data points.
     *
     * @param data The list of data points.
     * @return The value of the consumption with the highest value.
     */
    private int getMaxWatt(List<GraphView.Point> data) {
        int maxWatt = 0;
        for(GraphView.Point p : data) {
            if(p.y > maxWatt) {
                maxWatt = (int) p.y;
            }
        }

        return maxWatt;
    }
	/**
	 * Adapter that specifically displays {@link com.sebbelebben.smartpower.PsSocket}
	 * 
	 */
	public class SocketAdapter extends BaseAdapter{
		private ArrayList<PsSocket> objects;
		private Context context;
		public SocketAdapter(Context context, final int textViewResourceId, ArrayList<PsSocket> objects){
			this.context= context;
			this.objects = objects; 
		}
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if( v == null)	{
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.socket_item1, null);
			}

			final PsSocket socket = objects.get(position);
			TextView tv = (TextView) v.findViewById(R.id.text);
			final ToggleButton tb = (ToggleButton) v.findViewById(R.id.toggle_button);
			if ( tv != null) tv.setText(socket.getName());
			/*
			 * sets the onclicklistener to the togglebutton
			 * The toggling is delayed until a confirmation from server arrives
			 */
			if ( tb !=  null  ) {
				tb.setChecked(socket.getStatus());
			tb.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
                        getSherlockActivity().setProgressBarIndeterminateVisibility(false);
						Log.d("bug", "onclick");
						tb.setChecked(!tb.isChecked());
						if ( tb.isChecked()) {
							socket.turnOff(new GenericListener() {
								
								@Override
								public void success() {
                                    getSherlockActivity().setProgressBarIndeterminateVisibility(true);
									tb.setChecked(false);
								}
								
								@Override
								public void failed() {
                                    getSherlockActivity().setProgressBarIndeterminateVisibility(true);
									Toast.makeText(getActivity(), "Failed turning socket off", Toast.LENGTH_SHORT);
								}
							});
						}else {
							socket.turnOn(new GenericListener() {
								
								@Override
								public void success() {
                                    getSherlockActivity().setProgressBarIndeterminateVisibility(true);
									tb.setChecked(true);
									
								}
								
								@Override
								public void failed() {
                                    getSherlockActivity().setProgressBarIndeterminateVisibility(true);
									Toast.makeText(getActivity(), "Failed turning socket on", Toast.LENGTH_SHORT);
								}
							});
						}
					}
				});
			}
			return v;
		}
		@Override
		public int getCount() {
			return objects.size();
		}
		@Override
		public Object getItem(int arg0) {
			return objects.get(arg0);
		}
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

	}
}
