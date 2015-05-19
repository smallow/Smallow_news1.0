package com.smallow.community;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;
import com.smallow.community.interfac.CommonTitleBarOnClickLinstener;
import com.smallow.community.ui.CommonTitleBar;
import com.smallow.community.ui.SubMenuWindow;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private List<SubMenuWindow.SubMenu> subMenuData = new ArrayList<SubMenuWindow.SubMenu>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initSubMenuData();
        final CommonTitleBar bar1= (CommonTitleBar) findViewById(R.id.titleBar1);
        bar1.setOnCommonTitleBarOnClickListener(new CommonTitleBarOnClickLinstener() {
            @Override
            public void leftOnclick() {
                Toast.makeText(MainActivity.this,"上-左边",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rightOnclick() {
               // Toast.makeText(MainActivity.this,"上-右边",Toast.LENGTH_SHORT).show();
               SubMenuWindow subMenuWindow=new SubMenuWindow(MainActivity.this);
                subMenuWindow.setMenuItem(subMenuData);
                subMenuWindow.showBelowAndRight(bar1);

            }
        });



    }


    private void initSubMenuData(){
        subMenuData.add(new MainSubMenuData("功能1"));
        subMenuData.add(new MainSubMenuData("功能2"));
        subMenuData.add(new MainSubMenuData("功能3"));
    }


    class MainSubMenuData implements SubMenuWindow.SubMenu{
        private String name;

        public MainSubMenuData(String name){
            this.name=name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

}
