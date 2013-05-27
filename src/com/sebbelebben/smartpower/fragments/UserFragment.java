package com.sebbelebben.smartpower.fragments;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
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
import com.sebbelebben.smartpower.PsSocket;
import com.sebbelebben.smartpower.R;
import com.sebbelebben.smartpower.Server.GenericListener;
import com.sebbelebben.smartpower.User;
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
		f.setArguments(args);
		return f;
	}

	public UserFragment() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final User user = (User) getArguments().getSerializable("User");
		Resources res = getResources();
		View view = inflater.inflate(R.layout.fragment_user, container, false);
		listView = (ListView) view.findViewById(R.id.listView);

		String str = String.format(res.getString(R.string.userInfo), user.getUserName(), user.getPassword());
		((TextView) view.findViewById(R.id.textView)).setText(str);

		list = new ArrayList<PsSocket>();
		list.add(new PsSocket(0, "hennig", "apikey", true));
		list.add(new PsSocket(0, "hennigphan123456711111111111111111111111111111", "apikey", true));
		list.add(new PsSocket(0, "hennig2", "apikey", true));
		list.add(new PsSocket(0, "hennig3", "apikey", true));
		list.add(new PsSocket(0, "hennig4", "apikey", true));
		list.add(new PsSocket(0, "hennig5", "apikey", true));

		mAdapter = new SocketAdapter(getActivity(), R.layout.powerstrip_item, list);
		if(listView != null)
			listView.setAdapter(mAdapter);
		return view;
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
			if ( tb != null) {
				tb.setTextOn("its on");
				tb.setTextOff("its off");
				tb.setChecked(false);
			
			tb.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						tb.setChecked(!tb.isChecked());
						if ( tb.isChecked()) {
							socket.turnOff(new GenericListener() {
								
								@Override
								public void success() {
									tb.setChecked(false);
								}
								
								@Override
								public void failed() {
								}
							});
						}else {
							socket.turnOn(new GenericListener() {
								
								@Override
								public void success() {
									tb.setChecked(true);
									
								}
								
								@Override
								public void failed() {
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
