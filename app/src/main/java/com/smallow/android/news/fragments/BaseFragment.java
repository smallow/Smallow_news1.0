package com.smallow.android.news.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BaseFragment extends Fragment {
	public final static String TAG = "BaseFragment";
	private View rootView;

	protected View inflaterView(int layoutId) {
		return LayoutInflater.from(getActivity()).inflate(layoutId, null);
	}

	protected <T extends View> T findViewById(int id) {
		return (T) rootView.findViewById(id);
	}
	 

	@Override
	public void onViewCreated(View view,  Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		rootView = view;
		onInitWidgets(view, savedInstanceState);
	}

	protected abstract void onInitWidgets(View rootView,
			Bundle savedInstanceState);
}
