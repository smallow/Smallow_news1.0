package com.smallow.android.news.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smallow.android.news.R;
import com.smallow.android.news.SystemConst;
import com.smallow.android.news.utils.network.NetSpirit;
import com.smallow.android.news.utils.network.RequestReceiver;

/**
 * Created by smallow on 2015/2/12.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {

    private EditText et_serverIp;
    private EditText getEt_serverPort;
    private Button btnConnectionTest;
    private Button btnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setting_layout, container, false);
    }

    @Override
    protected void onInitWidgets(View rootView, Bundle savedInstanceState) {
        et_serverIp = findViewById(R.id.et_serverIp);
        getEt_serverPort = findViewById(R.id.et_serverPort);
        btnConnectionTest = findViewById(R.id.connectionTest);
        btnSave = findViewById(R.id.saveSetting);
        btnConnectionTest.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    protected void lazyLoad() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connectionTest:
                connectionTest();
                break;
            case R.id.saveSetting:
                saveSetting();
                break;
        }
    }

    private void saveSetting() {
    }

    private void connectionTest() {
        String serverIp = et_serverIp.getText().toString();
        String port=getEt_serverPort.getText().toString();
        SystemConst.prefix_data="http://"+serverIp+":"+port+"/";
        NetSpirit.getInstance().httpGet(SystemConst.prefix_data+"connectionTest.do",999,new RequestReceiver() {
            @Override
            public void onResult(int resultCode, int reqId, Object tag, String resp) {
                if(resp!=null && !"".equals(resp)){
                    JSONObject map= (JSONObject)JSON.parse(resp);
                    String result=map.getString("result");
                    if(result!=null && result.equals("success")){
                        new AlertDialog.Builder(getActivity()).setTitle("信息").setMessage("服务器连接成功!")
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                         dialog.dismiss();
                                    }
                                }).show();
                    }
                }else{
                    new AlertDialog.Builder(getActivity()).setTitle("信息").setMessage("未能连接到指定服务器!")
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                     dialog.dismiss();
                                }
                            }).show();
                }
            }

            @Override
            public void onRequestCanceled(int reqId, Object tag) {

            }
        });
    }
}


