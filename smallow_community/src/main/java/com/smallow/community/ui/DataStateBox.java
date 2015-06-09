package com.smallow.community.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smallow.common.utils.Utils;
import com.smallow.community.R;

/**
 * Created by smallow on 2015/6/9.
 */
public class DataStateBox extends FrameLayout {

    public enum State {
        INIT_LOADING,//正在加载中
        EMPTY_DATA,//没有数据
        LOAD_ERROR,//发生错误
        HIDE;//隐藏
    }


    private View stateBoxView;
    private ProgressBar stateLoadingView;
    private TextView stateTextView;
    private View contentView;

    //private CustomClipLoading stateLoadingView2;


    public DataStateBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }
    @Override
    public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
        if (child.getId() == R.id.data_state_ui_block) {
            index = getChildCount() - 1;
        } else {
            contentView = child;
            index = 0;
        }
        super.addView(child, index, params);
    }
    private void initUI() {
        LayoutInflater.from(getContext()).inflate(R.layout.state_box, this, true);
        stateBoxView=findViewById(R.id.data_state_ui_block);
        stateLoadingView= (ProgressBar) findViewById(R.id.data_state_progress);
        stateTextView= (TextView) findViewById(R.id.data_state_text);
    }


    private State curState = State.HIDE;

    public void setState(State state) {
        this.curState = state;
        updateStateUI();
    }

    private void updateStateUI() {
        if (curState == null) {
            curState = State.HIDE;
        }
        switch (curState){
            case HIDE:
                Utils.checkVisibility(stateBoxView, View.GONE);
                Utils.checkVisibility(contentView, View.VISIBLE);
                break;
            case EMPTY_DATA:
                Utils.checkVisibility(contentView, View.INVISIBLE);
                Utils.checkVisibility(stateBoxView, View.VISIBLE);
                Utils.checkVisibility(stateLoadingView, View.GONE);
                stateTextView.setText("没有数据,点击刷新");
                break;
            case LOAD_ERROR:
                break;
            case INIT_LOADING:
                Utils.checkVisibility(contentView, View.INVISIBLE);
                Utils.checkVisibility(stateBoxView, View.VISIBLE);
                Utils.checkVisibility(stateLoadingView, View.VISIBLE);
                stateTextView.setText("加载中");
                break;

        }
    }


}
