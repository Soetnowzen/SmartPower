package com.sebbelebben.smartpower.fragments;
import com.sebbelebben.smartpower.User;
import com.actionbarsherlock.app.SherlockFragment;
import com.sebbelebben.smartpower.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ProgressBar;

public class UserFragment extends SherlockFragment {
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
		View view = inflater.inflate(R.layout.fragment_user, container, false);
		view.findViewById(R.id.loading_progress).setVisibility(ProgressBar.GONE);
		return view;
	}
}
