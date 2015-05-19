package com.smallow.community.ui;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smallow.community.R;
import com.smallow.community.interfac.CommonTitleBarOnClickLinstener;

/**
 * Created by smallow on 2015/5/14.
 */
public class CommonTitleBar extends RelativeLayout {

    private TextView tvTitle;
    private ImageView leftImg;
    private TextView leftTv;
    private ImageView rightImg;
    private TextView rightTv;



    private String title;
    private float titleSize;
    private int titleColor;
    private Drawable leftImgSrc;
    private String leftTvText;
    private float leftTvSize;
    private int leftTvColor;
    private Drawable rightImgSrc;
    private String rightTvText;
    private float rightTvSize;
    private int rightTvColor;





    private LayoutParams titleParams;
    private LayoutParams leftImgParams;
    private LayoutParams leftTextParams;
    private LayoutParams rightImgParams;
    private LayoutParams rightTextParams;

    public CommonTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.CommonTitleBar);
        title=typedArray.getString(R.styleable.CommonTitleBar_titleText);
        titleSize=typedArray.getDimension(R.styleable.CommonTitleBar_titleTextSize, 0);
        titleColor=typedArray.getColor(R.styleable.CommonTitleBar_titleColor, Color.WHITE);
        leftImgSrc=typedArray.getDrawable(R.styleable.CommonTitleBar_leftImgSrc);
        leftTvText=typedArray.getString(R.styleable.CommonTitleBar_leftTvText);
        leftTvSize=typedArray.getDimension(R.styleable.CommonTitleBar_leftTvTextSize, 0);
        leftTvColor=typedArray.getColor(R.styleable.CommonTitleBar_leftTvTextColor, Color.WHITE);

        rightImgSrc=typedArray.getDrawable(R.styleable.CommonTitleBar_rightImgSrc);
        rightTvText=typedArray.getString(R.styleable.CommonTitleBar_rightTvText);
        rightTvSize=typedArray.getDimension(R.styleable.CommonTitleBar_rightTvTextSize, 0);
        rightTvColor=typedArray.getColor(R.styleable.CommonTitleBar_rightTvTextColor, Color.WHITE);



        typedArray.recycle();


        tvTitle=new TextView(context);
        tvTitle.setText(title);
        tvTitle.setTextSize(titleSize);
        tvTitle.setTextColor(titleColor);
        tvTitle.setGravity(Gravity.CENTER);

        leftTv=new TextView(context);
        if(leftTvText!=null){
            leftTv.setText(leftTvText);
            leftTv.setTextColor(leftTvColor);
            leftTv.setTextSize(leftTvSize);
        }

        leftImg=new ImageView(context);
        if(leftImgSrc!=null){
            leftImg.setImageDrawable(leftImgSrc);
            leftImg.setId(11);
        }


        leftTv=new TextView(context);
        if(leftTvText!=null){
            leftTv.setText(leftTvText);
            leftTv.setTextColor(leftTvColor);
            leftTv.setTextSize(leftTvSize);
        }

        rightImg=new ImageView(context);
        if(rightImgSrc!=null){
            rightImg.setImageDrawable(rightImgSrc);
            //rightImg.setId(12);
        }

        rightTv=new TextView(context);
        if(rightTvText!=null){
            rightTv.setText(rightTvText);
            rightTv.setTextColor(rightTvColor);
            rightTv.setTextSize(rightTvSize);
        }



        titleParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_VERTICAL);
        addView(tvTitle, titleParams);

        leftImgParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        leftImgParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        addView(leftImg,leftImgParams);

        leftTextParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        leftTextParams.addRule(RelativeLayout.RIGHT_OF,11);
        leftTextParams.addRule(RelativeLayout.CENTER_VERTICAL);
        addView(leftTv,leftTextParams);


        rightImgParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        rightImgParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        if(rightImg!=null)
            addView(rightImg,rightImgParams);

        rightTextParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        rightTextParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        if(rightTv!=null)
            addView(rightTv,rightTextParams);

        leftTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftOnclick();
            }
        });

        leftImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftOnclick();
            }
        });

        if(rightTv!=null){
            rightTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.rightOnclick();
                }
            });
        }


        if(rightImg!=null){
            rightImg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.rightOnclick();
                }
            });
        }





    }



    public void setOnCommonTitleBarOnClickListener(CommonTitleBarOnClickLinstener listener){
        this.listener=listener;
    }



    private CommonTitleBarOnClickLinstener listener;


    public TextView getTvTitle() {
        return tvTitle;
    }

    public ImageView getRightImg() {
        return rightImg;
    }

    public TextView getRightTv() {
        return rightTv;
    }
}
