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
		view.findViewById(R.id.loading_progress).setVisibility(ProgressBar.GONE);
		String str = String.format("name: %s\npwd: %s", user.getUserName(), user.getPassword());
		((TextView) view.findViewById(R.id.textView)).setText(str);
		
		return view;
	}
	public class SocketAdapter extends ArrayAdapter<PsSocket>{
		private ArrayList<PsSocket> objects;
		
		public SocketAdapter(Context context, int textViewResourceId, ArrayList<PsSocket> objects){
			super(context, textViewResourceId, objects);
			this.objects = objects;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if( v == null)	{
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.powerstrip_item, null);
			}
			PsSocket socket = objects.get(position);
			TextView tv = (TextView) v.findViewById(R.id.text);
			ToggleButton tb = (ToggleButton) v.findViewById(R.id.toggle_button);
			if ( tv != null) tv.setText(socket.getName());
			if (tb != null) tb.setChecked(true);
			return v;
		}
	
	}
}
