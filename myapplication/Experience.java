package com.example.lenovo.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysql.jdbc.PreparedStatement;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Experience extends AppCompatActivity {
    List<String> storeData = new ArrayList<String>();

    private LinearLayout sv_experienceLL;

    private Handler handler = null;

    //List<String> expTitle = new ArrayList<String>();//存放心得的标题
    String publishTime;

    String _expNum;
    String _expTitle;
    String _content;

    String experienceID;

    boolean changeViewDone = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience);
        Intent preIntent = getIntent();
        String account = preIntent.getStringExtra("Account");
        String userid = preIntent.getStringExtra("UserID");
        storeData.add(account);
        storeData.add(userid);

        handler = new Handler();

        final Button bReturn = (Button)findViewById(R.id.Experience_bt_return);
        final TextView tvAdd = (TextView)findViewById(R.id.Experience_tv_addExperience);

        bReturn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent ExperienceIntent = new Intent(Experience.this,UserAreaActivity.class);
                ExperienceIntent.putExtra("Account",storeData.get(0));
                ExperienceIntent.putExtra("UserID",storeData.get(1));
                Experience.this.startActivity(ExperienceIntent);
            }
        });

        tvAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent ExperienceAddIntent = new Intent(Experience.this,Experience2.class);
                ExperienceAddIntent.putExtra("Account",storeData.get(0));
                ExperienceAddIntent.putExtra("UserID",storeData.get(1));
                Experience.this.startActivity(ExperienceAddIntent);
            }
        });
        sv_experienceLL = findViewById(R.id.Experience_ll_experienceTitleLayout);
        GetMyExperienceInfo();
    }

    private void GetMyExperienceInfo()
    {
        new Thread(new Runnable(){
            @Override
            public void run(){
                Connection cn = null;
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    try{
                        String searchSQLCommand;
                        Statement st;
                        Statement st2;

                        ResultSet rs1;
                        //ResultSet rs2;
                        cn = DriverManager.getConnection("jdbc:mysql://172.20.10.3/health","root","root");

                        st = (Statement)cn.createStatement();
                        st2 = (Statement)cn.createStatement();

                        int userid = Integer.parseInt(storeData.get(1));

                        searchSQLCommand = "select * from experience where userID = " + Integer.toString(userid) + ";";
                        rs1 = st.executeQuery(searchSQLCommand);

                        while(rs1.next())
                        {
                            changeViewDone = false;

                            publishTime = rs1.getString("publishTime");
                            experienceID = rs1.getString("experienceID");
                            String expNum = rs1.getString("experienceID");//心得编号
                            String expTitle = rs1.getString("title");//心得标题
                            String content = rs1.getString("content");//心得内容

                            _content = content;
                            _expNum = expNum;
                            _expTitle = expTitle;

                            handler.post(runnableUI);
                            while(!changeViewDone);

                            _content = "";
                            _expNum = "";
                            _expTitle = "";
                        }

                        //要加载当前userid下的所有心得标题到页面上
                        //**************************************************************8

                        rs1.close();
                        cn.close();
                        st.close();
                    } catch (SQLException e){
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Runnable runnableUI = new Runnable(){
        @Override
        public void run(){
            final View manage_etView = View.inflate(Experience.this,R.layout.experiencetitle,null);
            TextView tv_et_content = manage_etView.findViewById(R.id.MET_tv_manageExperienceContent);
            TextView tv_et_title = manage_etView.findViewById(R.id.MET_tv_manageExperienceTitle);
            TextView tv_et_sharetime = manage_etView.findViewById(R.id.MET_tv_shareTime);

            sv_experienceLL.addView(manage_etView);

            tv_et_content.setText(_content);
            tv_et_title.setText(_expTitle);
            tv_et_sharetime.setText(publishTime);

            //如果有需要，可以在每个textView里面加上中文的"标题"、"内容"等

            Button tv_ri_remove = manage_etView.findViewById(R.id.MET_bt_remove);
            tv_ri_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sv_experienceLL.removeView(manage_etView);
                    RemoveData();
                }
            });

            changeViewDone = true;
        }
    };

    private void RemoveData()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection cn2 = null;
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    try{
                        String searchSQLCommand2;
                        Statement st3;
                        ResultSet rs3;
                        PreparedStatement pstmt;

                        cn2 = DriverManager.getConnection("jdbc:mysql://172.20.10.3/health","root","root");

                        st3 = (Statement)cn2.createStatement();

                        int experienceID_int = Integer.parseInt(experienceID);

                        searchSQLCommand2 = "delete from experience where experienceID = " + experienceID_int + ";";
                        pstmt = (PreparedStatement)cn2.prepareStatement(searchSQLCommand2);
                        pstmt.executeUpdate();

                        pstmt.close();
                        cn2.close();
                        st3.close();
                    } catch (SQLException e){
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
