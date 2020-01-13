package com.example.lenovo.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Experience2 extends AppCompatActivity {
    List<String> storeData = new ArrayList<String>();

    boolean readDone;

    String getTitle;
    String getContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience2);

        Intent preIntent = getIntent();
        String account = preIntent.getStringExtra("Account");
        String userid = preIntent.getStringExtra("UserID");

        storeData.add(account);
        storeData.add(userid);

        final Button bReturn = (Button)findViewById(R.id.Experience2_bt_return);
        final TextView tvMan = (TextView)findViewById(R.id.Experience2_tv_manageExperience);

        final EditText etTitle = (EditText)findViewById(R.id.Experience2_et_editTitle);
        final EditText etContent = (EditText)findViewById(R.id.Experience2_et_editContent);

        final Button bConfirm = (Button)findViewById(R.id.Experience2_bt_confirm);

        bReturn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent ExperienceIntent = new Intent(Experience2.this,UserAreaActivity.class);
                ExperienceIntent.putExtra("Account",storeData.get(0));
                ExperienceIntent.putExtra("UserID",storeData.get(1));
                Experience2.this.startActivity(ExperienceIntent);
            }
        });

        tvMan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent ExperienceManIntent = new Intent(Experience2.this,Experience.class);
                ExperienceManIntent.putExtra("Account",storeData.get(0));
                ExperienceManIntent.putExtra("UserID",storeData.get(1));
                Experience2.this.startActivity(ExperienceManIntent);
            }
        });

        etTitle.setOnKeyListener(new EditText.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                getTitle = etTitle.getText().toString();
                return false;
            }
        });

        etContent.setOnKeyListener(new EditText.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                getContent = etContent.getText().toString();
                return false;
            }
        });

        bConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                shareExperience();
                Intent ExperienceIntent = new Intent (Experience2.this, Experience.class);
                ExperienceIntent.putExtra("Account", storeData.get(0));
                ExperienceIntent.putExtra("UserID", storeData.get(1));
                Experience2.this.startActivity(ExperienceIntent);
            }
        });

    }

    private void shareExperience()
    {
        readDone = false;
        ConnectSQL_ToSharerExperience();
        while(!readDone)
        {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void ConnectSQL_ToSharerExperience()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection cn = null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    try{
                        String searchSQLCommand;
                        Statement st;
                        ResultSet rs;
                        PreparedStatement pstmt;

                        cn = DriverManager.getConnection("jdbc:mysql://172.20.10.3/health","root","root");
                        st = (Statement)cn.createStatement();


                        //get the experience ID
                        searchSQLCommand = "SELECT id FROM maxid WHERE name = 'maxexperienceid'";
                        rs = st.executeQuery(searchSQLCommand);
                        rs.next();
                        String s_maxexperienceid = rs.getString("id");
                        int i_maxexperienceid = Integer.parseInt(s_maxexperienceid);

                        //get the share time
                        Date nowTime = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        String shareTime = dateFormat.format(nowTime);

                        //insert into database
                        String insertSQLCommand = "insert into experience(experienceID,userID,publishTime,title,content)" +
                                " values ('" + s_maxexperienceid + "','" + storeData.get(1) + "','" + shareTime + "','" +
                                getTitle + "','" + getContent + "')";
                        pstmt = (PreparedStatement)cn.prepareStatement(insertSQLCommand);
                        pstmt.executeUpdate();

                        //update max experience id
                        i_maxexperienceid += 1;
                        String modifySQLCommand = "update maxid set id = " + Integer.toString(i_maxexperienceid) +
                                " where name = 'maxexperienceid'";
                        pstmt = (PreparedStatement)cn.prepareStatement(modifySQLCommand);
                        pstmt.executeUpdate();

                        pstmt.close();
                        cn.close();
                        st.close();

                        readDone = true;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
