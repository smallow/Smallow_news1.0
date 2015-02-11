package com.smallow.android.news.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BaseFragment extends Fragment {
	public final static String TAG = "BaseFragment";
    /** Fragment当前状态是否可见 */
    protected boolean isVisible;
	private View rootView;

	protected View inflaterView(int layoutId) {
		return LayoutInflater.from(getActivity()).inflate(layoutId, null);
	}

	protected <T extends View> T findViewById(int id) {
		return (T) rootView.findViewById(id);
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();
}
