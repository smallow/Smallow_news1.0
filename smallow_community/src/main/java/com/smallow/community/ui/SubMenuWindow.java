package com.smallow.community.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smallow.community.R;
import com.smallow.community.adapter.InnerBaseAdapter;

import java.util.List;

/**
 * Created by smallow on 2015/5/19.
 */
public class SubMenuWindow extends PopupWindow {

    private FrameLayout rootView;
    private ListView listview;
    private MenuAdapter mAdapter = new MenuAdapter();
    private int belowYInterval = 1;
    private int rightXOffset = 1;

    public SubMenuWindow(Context context) {
        super(context);
        setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        setTouchable(true);
        setOutsideTouchable(true);
        setFocusable(true);
        float density = context.getResources().getDisplayMetrics().density;
        belowYInterval = (int) (density * belowYInterval);
        rightXOffset = (int) (density * rightXOffset);
        rootView = (FrameLayout) LayoutInflater.from(context).inflate(
                R.layout.sub_menu_window, null);
        listview = (ListView) rootView.findViewById(R.id.sub_menu_window_listview);
        listview.setAdapter(mAdapter);
        setWidth(listview.getLayoutParams().width);
        setHeight(listview.getLayoutParams().height);
        setContentView(rootView);
    }


    private List<SubMenu> mMenuItemsArray;

    public void setMenuItem(List<SubMenu> data) {
        mMenuItemsArray = data;
        mAdapter.setData(mMenuItemsArray, true);
    }



    public void showBelowAndCenter(View referView) {
        int[] xy = new int[2];
        referView.getLocationOnScreen(xy);
        int y = xy[1] + referView.getHeight() + belowYInterval;
        showAtLocation(referView, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, y);
    }

    public void showBelowAndRight(View referView) {
        showBelowAndRight(referView, rightXOffset);
    }
    public void showBelowAndRight(View referView, int rightXOffset) {
        int[] xy = new int[2];
        referView.getLocationOnScreen(xy);
        int y = xy[1] + referView.getHeight() + belowYInterval;
        showAtLocation(referView, Gravity.RIGHT | Gravity.TOP, rightXOffset, y);
    }





    public interface SubMenu {
        public String getName();
    }

    class MenuAdapter extends InnerBaseAdapter<SubMenu> {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RelativeLayout relativeLayout = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_menu_window_item, parent, false);
            }
            relativeLayout = (RelativeLayout) convertView;
            SubMenu subMenu = getData(position);
            TextView textView = (TextView) relativeLayout.findViewById(R.id.sub_menu_window_item_tv);
            if (subMenu != null)
                textView.setText(subMenu.getName());
            return convertView;
        }
    }
}
