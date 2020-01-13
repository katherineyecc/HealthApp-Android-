package com.example.lenovo.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Sport extends AppCompatActivity {
    List<String> storeData = new ArrayList<String>();

    boolean readDone;

    private LinearLayout scrollviewLL;

    List<String> sportNames = new ArrayList<String>();
    List<String> sportCalorie = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        scrollviewLL = (LinearLayout) findViewById(R.id.Sport_ll_sportItemLayout1);

        Intent preIntent = getIntent();
        String sportData = preIntent.getStringExtra("SportData");
        String account = preIntent.getStringExtra("Account");
        String userid = preIntent.getStringExtra("UserID");
        storeData.add(account);
        storeData.add(userid);

        final Button bReturn = (Button)findViewById(R.id.Sport_bt_return);
        final TextView add = (TextView)findViewById(R.id.Sport_tv_add);
        final TextView man = (TextView)findViewById(R.id.Sport_tv_manageSport);
        final Button bConfirm = (Button)findViewById(R.id.Sport_bt_confirm);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addsportIntent = new Intent(Sport.this,AddSport.class);
                String exportData = exportSport();
                System.out.println(exportData);
                addsportIntent.putExtra("SportData", exportData);
                addsportIntent.putExtra("Account",storeData.get(0));
                addsportIntent.putExtra("UserID",storeData.get(1));
                Sport.this.startActivity(addsportIntent);
            }
        });

        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent SportIntent = new Intent(Sport.this, Sport2.class);
                SportIntent.putExtra("Account",storeData.get(0));
                SportIntent.putExtra("UserID",storeData.get(1));
                Sport.this.startActivity(SportIntent);
            }
        });

        bReturn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent SportIntent = new Intent(Sport.this,UserAreaActivity.class);
                SportIntent.putExtra("Account",storeData.get(0));
                SportIntent.putExtra("UserID",storeData.get(1));
                Sport.this.startActivity(SportIntent);
            }
        });

        bConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                shareSport();

                Intent SportIntent = new Intent (Sport.this, Sport2.class);
                SportIntent.putExtra("Account", storeData.get(0));
                SportIntent.putExtra("UserID", storeData.get(1));
                Sport.this.startActivity(SportIntent);
            }
        });

        if(sportData != null){
            analystData(sportData);
            importSport();
        }
    }

    private void analystData(String sportData){//counter_1代表早餐午餐还是晚餐，所以这里不需要，看一下怎么改
        //int counter_1;
        int counter_head = 0;
        int counter_tail;

        while(counter_head < sportData.length())
        {
            if(sportData.charAt(counter_head) == ';')
            {
                counter_head++;
                break;
            }
            if(sportData.charAt(counter_head) == '(')
            {
                counter_head++;
                counter_tail = counter_head + 1;
                while(true)
                {
                    char tailC = sportData.charAt(counter_tail);
                    if(tailC == '(' || tailC == ')' || tailC == ';')
                        break;
                    counter_tail++;
                }
                String currentSportName = sportData.substring(counter_head, counter_tail);
                counter_head = counter_tail;
                sportNames.add(currentSportName);
                continue;
            }
            if(sportData.charAt(counter_head) == ')')
            {
                counter_head++;
                counter_tail = counter_head + 1;
                while(true)
                {
                    char tailC = sportData.charAt(counter_tail);
                    if(tailC == '(' || tailC == ')' || tailC == ';')
                        break;
                    counter_tail++;
                }
                String currentSportCalorie = sportData.substring(counter_head, counter_tail);
                counter_head = counter_tail;
                sportCalorie.add(currentSportCalorie);
                continue;
            }
        }
    }

    private void importSport()
    {
        for(int i = 0; i < sportNames.size(); i++)
        {
            addViewItem(sportNames.get(i), sportCalorie.get(i));
        }
    }

    private void addViewItem(String sportName, String sportCalorie)
    {
        final View sportitemView = View.inflate(this, R.layout.sportitem, null);
        //Button b_remove = (Button)sportitemView.findViewById(R.id.SI_bt_addSport);

        /*
        b_remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                scrollviewLL.removeView(sportitemView);
            }
        });
        */

        TextView tv_name = sportitemView.findViewById(R.id.SI_tv_sportName);
        tv_name.setText(sportName);
        TextView tv_calorie = sportitemView.findViewById(R.id.SI_tv_calorie);
        tv_calorie.setText(sportCalorie);
        scrollviewLL.addView(sportitemView);
    }

    private String exportSport()
    {
        sportNames.clear();

        String exportData = "";

        for(int i = 0; i < scrollviewLL.getChildCount(); i++)
        {
            View ChildAt = scrollviewLL.getChildAt(i);
            TextView sportName = ChildAt.findViewById(R.id.SI_tv_sportName);
            exportData = exportData + "(" + sportName.getText();
            TextView sportCalorie = ChildAt.findViewById(R.id.SI_tv_calorie);
            exportData = exportData + ")" + sportCalorie.getText();
        }
        exportData = exportData + ";";

        return exportData;
    }

    private void shareSport()
    {
        sportNames.clear();

        for(int i = 0; i < scrollviewLL.getChildCount(); i++)
        {
            View ChildAt = scrollviewLL.getChildAt(i);
            TextView sportName = ChildAt.findViewById(R.id.SI_tv_sportName);
            sportNames.add(sportName.getText().toString());
            TextView t_sportCalorie = ChildAt.findViewById(R.id.SI_tv_calorie);

            /////////////////////////////////////////////////////////////////////////
            sportCalorie.add(t_sportCalorie.getText().toString());
            //////////////////////////////////////////////////////////////////////
        }

        readDone = false;

        ConnectSQL_ToShareSport();

        while(!readDone)
        {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void ConnectSQL_ToShareSport(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                Connection cn = null;
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    try{
                        String searchSQLCommand;
                        Statement st;
                        ResultSet rs;
                        PreparedStatement pstmt;

                        List<String> sportID = new ArrayList<String>();

                        cn = DriverManager.getConnection("jdbc:mysql://172.20.10.3/health","root","root");

                        st = (Statement) cn.createStatement();

                        for(int i = 0; i < sportNames.size(); i++)
                        {
                            searchSQLCommand = "select sportID from sportconsumecalorieinfo where sportName = '" +
                                    sportNames.get(i) + "'";

                            rs = st.executeQuery(searchSQLCommand);
                            rs.next();
                            String currentSportID = rs.getString("sportID");
                            sportID.add(currentSportID);
                            rs.close();
                        }

                        searchSQLCommand = "select id from maxid where name = 'maxsportid'";
                        rs = st.executeQuery(searchSQLCommand);
                        rs.next();
                        String s_maxsportid = rs.getString("id");
                        int i_maxsportid = Integer.parseInt(s_maxsportid);

                        //Get share time
                        Date nowTime = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        String shareTime = dateFormat.format(nowTime);

                        float totalSportCalorie = 0;
                        for(int i = 0; i < sportCalorie.size(); i++)
                        {
                            totalSportCalorie += Float.parseFloat(sportCalorie.get(i));
                        }

                        String s_sport = "";

                        for(int i = 0; i < sportID.size(); i++)
                        {
                            s_sport = s_sport + sportID.get(i) + "_";
                        }

                        String insertSQLCommand = "insert into dailysport (sportid, Cdate, userID, sport, sportDuration) " +
                                "values ( '" +  s_maxsportid + "','" + shareTime + "','" + storeData.get(1) + "','"+
                                s_sport + "','" + Float.toString(totalSportCalorie) + "')";
                        pstmt = (PreparedStatement)cn.prepareStatement(insertSQLCommand);
                        pstmt.executeUpdate();

                        i_maxsportid += 1;
                        String modifySQLCommand = "update maxid set id =" + Integer.toString(i_maxsportid) +
                                " where name = 'maxsportid'";
                        pstmt = (PreparedStatement)cn.prepareStatement(modifySQLCommand);
                        pstmt.executeUpdate();

                        pstmt.close();
                        cn.close();
                        st.close();

                        readDone = true;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
