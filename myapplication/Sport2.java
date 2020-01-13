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

public class Sport2 extends AppCompatActivity {
    List<String> storeData = new ArrayList<String>();

    private LinearLayout sv_sportLL;

    private Handler handler = null;

    List<String> sportName = new ArrayList<String>();

    String shareTime;
    String totalCalorie;
    String sportID;

    boolean changeViewDone = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport2);
        Intent preIntent = getIntent();
        String account = preIntent.getStringExtra("Account");
        String userid = preIntent.getStringExtra("UserID");
        storeData.add(account);
        storeData.add(userid);

        handler = new Handler();

        final Button bReturn = (Button)findViewById(R.id.Sport2_bt_return);
        final TextView tvAdd = (TextView)findViewById(R.id.Sport2_tv_addSport);

        bReturn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent SportIntent = new Intent(Sport2.this, UserAreaActivity.class);
                SportIntent.putExtra("Account", storeData.get(0));
                SportIntent.putExtra("UserID", storeData.get(1));
                Sport2.this.startActivity(SportIntent);
            }
        });

        tvAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent SportIntent = new Intent(Sport2.this, Sport.class);
                SportIntent.putExtra("Account", storeData.get(0));
                SportIntent.putExtra("UserID", storeData.get(1));
                Sport2.this.startActivity(SportIntent);
            }
        });

        sv_sportLL = findViewById(R.id.Sport2_ll_sportItemLayout);
        GetMySportInfo();
    }

    private void GetMySportInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection cn = null;
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    try{
                        String searchSQLCommand;
                        Statement st;
                        Statement st2;

                        ResultSet rs1;
                        ResultSet rs2;

                        cn = DriverManager.getConnection("jdbc:mysql://172.20.10.3/health","root","root");

                        st = (Statement)cn.createStatement();
                        st2 = (Statement)cn.createStatement();

                        int userid = Integer.parseInt(storeData.get(1));

                        searchSQLCommand = "select * from dailysport where userID = " + Integer.toString(userid) + ";";
                        rs1 = st.executeQuery(searchSQLCommand);

                        while(rs1.next()){
                            changeViewDone = false;

                            shareTime = rs1.getString("Cdate");
                            sportID = rs1.getString("sportId");
                            String sportNum = rs1.getString("sport");
                            String sportCalorie = rs1.getString("sportDuration");
                            double totalCalorieInt = Double.parseDouble(sportCalorie);
                            totalCalorie = String .format("%.2f", totalCalorieInt);

                            sportName.clear();

                            int head = 0;
                            int tail = 0;

                            while(head < sportNum.length())
                            {
                                if(sportNum.charAt(head) == '_')
                                {
                                    head++;
                                    continue;
                                }
                                tail = head + 1;
                                while(sportNum.charAt(tail) != '_')
                                {
                                    tail++;
                                }
                                sportName.add(sportNum.substring(head,tail));

                                head = tail + 1;
                            }

                            for(int i = 0; i < sportName.size(); i++)
                            {
                                searchSQLCommand = "select sportName from sportconsumecalorieinfo where sportID = " + sportName.get(i) + ";";
                                rs2 = st2.executeQuery(searchSQLCommand);
                                rs2.next();
                                sportName.set(i, rs2.getString("sportName"));
                                rs2.close();
                            }

                            handler.post(runnableUI);
                            while(!changeViewDone);
                        }
                        rs1.close();
                        cn.close();
                        st.close();
                    } catch (SQLException e){
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Runnable runnableUI = new Runnable(){
        @Override
        public void run(){
            final View manage_siView = View.inflate(Sport2.this,R.layout.managesportitem,null);

            TextView tv_si_sport = manage_siView.findViewById(R.id.MSI_tv_manageSport);
            TextView tv_si_shareTime = manage_siView.findViewById(R.id.MSI_tv_shareTime);
            TextView tv_si_calorie = manage_siView.findViewById(R.id.MSI_tv_manageCalorie);

            sv_sportLL.addView(manage_siView);

            String _sportName = "sport: ";
            for(int i = 0; i < sportName.size(); i++) {
                _sportName += sportName.get(i) + " ";
            }
            tv_si_sport.setText(_sportName);
            tv_si_shareTime.setText(shareTime);
            tv_si_calorie.setText(totalCalorie + " kCal");

            Button tv_si_remove = manage_siView.findViewById(R.id.MSI_bt_remove);
            tv_si_remove.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    sv_sportLL.removeView(manage_siView);
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

                        int sportID_int = Integer.parseInt(sportID);

                        searchSQLCommand2 = "delete from dailysport where sportId = " + sportID_int + ";";
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

    /*private void RemoveSQL() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection cn = null;
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    try{
                        String searchSQLCommand;
                        Statement st;
                        Statement st2;

                        ResultSet rs1;
                        ResultSet rs2;

                        cn = DriverManager.getConnection("jdbc:mysql://172.20.10.3/health","root","root");

                        st = (Statement)cn.createStatement();
                        st2 = (Statement)cn.createStatement();

                        int userid = Integer.parseInt(storeData.get(1));

                        searchSQLCommand = "select * from dailysport where userID = " + Integer.toString(userid) + ";";
                        rs1 = st.executeQuery(searchSQLCommand);

                        while(rs1.next()){
                            changeViewDone = false;

                            shareTime = rs1.getString("Cdate");
                            String sportNum = rs1.getString("sport");
                            String sportCalorie = rs1.getString("sportDuration");
                            double totalCalorieInt = Double.parseDouble(sportCalorie);
                            totalCalorie = String .format("%.2f", totalCalorieInt);

                            sportName.clear();

                            int head = 0;
                            int tail = 0;

                            while(head < sportNum.length())
                            {
                                if(sportNum.charAt(head) == '_')
                                {
                                    head++;
                                    continue;
                                }
                                tail = head + 1;
                                while(sportNum.charAt(tail) != '_')
                                {
                                    tail++;
                                }
                                sportName.add(sportNum.substring(head,tail));

                                head = tail + 1;
                            }

                            for(int i = 0; i < sportName.size(); i++)
                            {
                                searchSQLCommand = "select sportName from sportconsumecalorieinfo where sportID = " + sportName.get(i) + ";";
                                rs2 = st2.executeQuery(searchSQLCommand);
                                rs2.next();
                                sportName.set(i, rs2.getString("sportName"));
                                rs2.close();
                            }

                            handler.post(runnableUI);
                            while(!changeViewDone);
                        }
                        rs1.close();
                        cn.close();
                        st.close();
                    } catch (SQLException e){
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/


}
