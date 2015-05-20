package com.smallow.community.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.smallow.community.R;

/**
 * Created by smallow on 2015/5/20.
 */
public class CommonSearchInput extends LinearLayout {
    private ImageView leftImg;
    private EditText searchText;

    private Drawable leftImgSrc;
    private String defaultText;
    private float defaultTextSize;
    private int defaultTextColor;
    private RelativeLayout.LayoutParams leftImgParams;
    private RelativeLayout.LayoutParams searchTextParams;


    public CommonSearchInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonSearchInput);
        leftImgSrc = typedArray.getDrawable(R.styleable.CommonSearchInput_leftImgSrc1);
        leftImg = new ImageView(context);
        if (leftImgSrc != null)
            leftImg.setImageDrawable(leftImgSrc);
        defaultText = typedArray.getString(R.styleable.CommonSearchInput_defaultText);
        searchText = new EditText(context);
        if (defaultText != null)
            searchText.setText(defaultText);

        defaultTextSize=typedArray.getDimension(R.styleable.CommonSearchInput_defaultTextSize,12);
        if (defaultTextSize!=0)
            searchText.setTextSize(defaultTextSize);

        defaultTextColor=typedArray.getColor(R.styleable.CommonSearchInput_defaultTextColor, Color.GRAY);
        searchText.setTextColor(defaultTextColor);

        searchText.setGravity(Gravity.CENTER);
        searchText.setPadding(0,5,5,0);
        searchText.setBackground(null);
        searchText.setHint("点击搜索你所需要的优惠信息");



        leftImgParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        addView(leftImg, leftImgParams);

        searchTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        addView(searchText, searchTextParams);
    }


}
