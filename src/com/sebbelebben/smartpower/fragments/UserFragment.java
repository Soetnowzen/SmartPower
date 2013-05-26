package com.sebbelebben.smartpower.fragments;
import java.util.ArrayList;

import com.sebbelebben.smartpower.PsSocket;
import com.sebbelebben.smartpower.User;
import com.actionbarsherlock.app.SherlockFragment;
import com.sebbelebben.smartpower.R;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class UserFragment extends SherlockFragment {
	ArrayList<PsSocket> list;
	SocketAdapter mAdapter;
	ListView listView;
	
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
		View view = inflater.inflate(R.layout.fragment_user, container, false);
		listView = (ListView) view.findViewById(R.id.listView);

		String str = String.format("name: %s\npwd: %s", user.getUserName(), user.getPassword());
		((TextView) view.findViewById(R.id.textView)).setText(str);
		
		list = new ArrayList<PsSocket>();
		list.add(new PsSocket(0, "hennig", "apikey"));
		list.add(new PsSocket(0, "hennigphan123456711111111111111111111111111111", "apikey"));
		list.add(new PsSocket(0, "hennig2", "apikey"));
		list.add(new PsSocket(0, "hennig3", "apikey"));
		list.add(new PsSocket(0, "hennig4", "apikey"));
		list.add(new PsSocket(0, "hennig5", "apikey"));

		mAdapter = new SocketAdapter(getActivity(), R.layout.powerstrip_item, list);
		if(listView != null)
            listView.setAdapter(mAdapter);
		return view;
	}
	public class SocketAdapter extends ArrayAdapter<PsSocket>{
		private ArrayList<PsSocket> objects;
		
		public SocketAdapter(Context context, int textViewResourceId, ArrayList<PsSocket> objects){
			super(context, textViewResourceId, objects);
			this.objects = objects; 
		}
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if( v == null)	{
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.socket_item1, null);

			}
			
			PsSocket socket = objects.get(position);
			TextView tv = (TextView) v.findViewById(R.id.text);
			final ToggleButton tb = (ToggleButton) v.findViewById(R.id.toggle_button);
			if ( tv != null) tv.setText(socket.getName());
			if ( tb != null) {
				tb.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if ( tb.isChecked()); //send turn on power
						if ( !tb.isChecked()); //send turn off power
						
					}
				});
			}
			
			return v;
		}
	
	}
}
