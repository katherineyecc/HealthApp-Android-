package com.example.lenovo.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ShareRecipe extends AppCompatActivity {
    List<String> storeData = new ArrayList<String>();
    private LinearLayout sv_shareRecipeLL;

    private Handler handler=null;
    String userID;
    List<String> breakfastName = new ArrayList<String>();
    List<String> lunchName = new ArrayList<String>();
    List<String> dinnerName = new ArrayList<String>();
    List<String> sportName = new ArrayList<String>();
    String shareTime;
    String sportShareTime;
    String totalCalorie;

    Date Date1;
    Date Date2;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    boolean changeViewDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_recipe);
        handler=new Handler();
        Intent preIntent = getIntent();
        String account = preIntent.getStringExtra("Account");
        String userid = preIntent.getStringExtra("UserID");
        userID = userid;
        storeData.add(account);
        storeData.add(userid);

        final Button bReturn = (Button)findViewById(R.id.SR_bt_shareReturn);

        bReturn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent RecipeIntent = new Intent(ShareRecipe.this,UserAreaActivity.class);
                RecipeIntent.putExtra("Account",storeData.get(0));
                RecipeIntent.putExtra("UserID",storeData.get(1));
                ShareRecipe.this.startActivity(RecipeIntent);
            }
        });
        sv_shareRecipeLL = findViewById(R.id.SR_ll_shareRecipe);
        GetMyRecipeInfo();
    }

    private void GetMyRecipeInfo()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection cn = null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    try {
                        String searchSQLCommand;
                        String searchSQLCommand2;//To save SQL sentences relating to sport
                        Statement st;
                        Statement st2;
                        Statement st3;//To execute sport query

                        ResultSet rs1;//To get food information
                        ResultSet rs2;
                        ResultSet rs3;//To get sport information

                        cn = DriverManager.getConnection("jdbc:mysql://172.20.10.3/health", "root", "root");

                        st = (Statement) cn.createStatement();
                        st2 = (Statement) cn.createStatement();
                        st3 = (Statement) cn.createStatement();//for sport

                        searchSQLCommand = "select * from recipes where userID = " + userID + ";";//是把当前用户的食谱都搜索出来，所以要根据userid来搜索dailysport数据库，找到对应用户的运动数据
                        //searchSQLCommand2 = "select * from dailysport;";

                        rs1 = st.executeQuery(searchSQLCommand);
                        //rs2 = st3.executeQuery(searchSQLCommand2);

                        while(rs1.next())
                        {
                            changeViewDone = false;

                            //userid = rs1.getString("userID");
                            shareTime = rs1.getString("shareTime");
                            try {
                                Date1 = format.parse(shareTime);
                                //sportShareTime = rs2.getString("Cdate")
                                searchSQLCommand2 = "select * from dailysport where userID = " + userID + ";";
                                rs2 = st3.executeQuery(searchSQLCommand2);
                                Float totalSportCalorie = 0.0f;
                                String sportNum;

                                int head = 0;
                                int tail = 0;

                                while (rs2.next()) {
                                    sportShareTime = rs2.getString("Cdate");
                                    Date2 = format.parse(sportShareTime);
                                    if (Date1.compareTo(Date2) == 0) {
                                        //如果进行运动的日期和食谱日期一致（即在同一天既有进食增加的卡路里，也有运动燃烧的卡路里，就要综合计算
                                        String sportCalorie = rs2.getString("sportDuration");
                                        totalSportCalorie = Float.parseFloat(sportCalorie);
                                        sportNum = rs2.getString("sport");

                                        sportName.clear();

                                        while (head < sportNum.length()) {
                                            if (sportNum.charAt(head) == '_') {
                                                head++;
                                                continue;
                                            }
                                            tail = head + 1;
                                            while (sportNum.charAt(tail) != '_') {
                                                tail++;
                                            }
                                            sportName.add(sportNum.substring(head, tail));

                                            head = tail + 1;
                                        }

                                        break;
                                    } else {
                                        totalSportCalorie = 0.0f;
                                        continue;
                                    }
                                }
                                rs2.close();

                                String breakfastNum = rs1.getString("breakfast");
                                String lunchNum = rs1.getString("lunch");
                                String dinnerNum = rs1.getString("dinner");

                                String breakfastServe = rs1.getString("breakfastServe");
                                String lunchServe = rs1.getString("lunchServe");
                                String dinnerServe = rs1.getString("dinnerServe");
                                double totalCalorieInt = Double.parseDouble(breakfastServe) + Double.parseDouble(lunchServe) + Double.parseDouble(dinnerServe)
                                        - totalSportCalorie;
                                totalCalorie = String.format("%.2f", totalCalorieInt);

                                breakfastName.clear();
                                lunchName.clear();
                                dinnerName.clear();

                                //从recipe表中获得userID对应的食谱，接收食谱中的各种数据
                                head = 0;
                                tail = 0;

                                while (head < breakfastNum.length()) {
                                    if (breakfastNum.charAt(head) == '_') {
                                        head++;
                                        continue;
                                    }
                                    tail = head + 1;
                                    while (breakfastNum.charAt(tail) != '_') {
                                        tail++;
                                    }
                                    breakfastName.add(breakfastNum.substring(head, tail));

                                    head = tail + 1;
                                }

                                head = 0;
                                while (head < lunchNum.length()) {
                                    if (lunchNum.charAt(head) == '_') {
                                        head++;
                                        continue;
                                    }
                                    tail = head + 1;
                                    while (lunchNum.charAt(tail) != '_') {
                                        tail++;
                                    }
                                    lunchName.add(lunchNum.substring(head, tail));

                                    head = tail + 1;
                                }

                                head = 0;
                                while (head < dinnerNum.length()) {
                                    if (dinnerNum.charAt(head) == '_') {
                                        head++;
                                        continue;
                                    }
                                    tail = head + 1;
                                    while (dinnerNum.charAt(tail) != '_') {
                                        tail++;
                                    }
                                    dinnerName.add(dinnerNum.substring(head, tail));

                                    head = tail + 1;
                                }

                                head = 0;


                                //根据breakfastName、lunchName、dinnerName中的foodID ,跳转到foodcalorieinfo表中
                                //读取对应的foodname，这时候就读取完一张食谱中的所有信息了
                                for (int i = 0; i < breakfastName.size(); i++) {
                                    searchSQLCommand = "select foodname from foodcalorieinfo where foodID = " + breakfastName.get(i) + ";";
                                    rs2 = st2.executeQuery(searchSQLCommand);
                                    rs2.next();
                                    breakfastName.set(i, rs2.getString("foodname"));
                                    rs2.close();
                                }

                                for (int i = 0; i < lunchName.size(); i++) {
                                    searchSQLCommand = "select foodname from foodcalorieinfo where foodID = " + lunchName.get(i) + ";";
                                    rs2 = st2.executeQuery(searchSQLCommand);
                                    rs2.next();
                                    lunchName.set(i, rs2.getString("foodname"));
                                    rs2.close();
                                }

                                for (int i = 0; i < dinnerName.size(); i++) {
                                    searchSQLCommand = "select foodname from foodcalorieinfo where foodID = " + dinnerName.get(i) + ";";
                                    rs2 = st2.executeQuery(searchSQLCommand);
                                    rs2.next();
                                    dinnerName.set(i, rs2.getString("foodname"));
                                    rs2.close();
                                }

                                for (int i = 0; i < sportName.size(); i++) {
                                    searchSQLCommand = "select sportName from sportconsumecalorieinfo where sportID = " + sportName.get(i) + ";";
                                    rs2 = st2.executeQuery(searchSQLCommand);
                                    rs2.next();
                                    sportName.set(i, rs2.getString("sportName"));
                                    rs2.close();
                                }
                                handler.post(runnableUI);
                                while (!changeViewDone) ;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        rs1.close();

                        cn.close();//一定要关闭
                        st.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private Runnable runnableUI = new Runnable() {
        @Override
        public void run() {
            final View sharerecipeView = View.inflate(ShareRecipe.this,R.layout.sharerecipeitem,null);
            TextView tv_share_breakfast = sharerecipeView.findViewById(R.id.SRI_tv_breakfast);
            TextView tv_share_lunch = sharerecipeView.findViewById(R.id.SRI_tv_lunch);
            TextView tv_share_dinner = sharerecipeView.findViewById(R.id.SRI_tv_dinner);
            TextView tv_share_sharetime = sharerecipeView.findViewById(R.id.SRI_tv_shareTime);
            TextView tv_share_calorie = sharerecipeView.findViewById(R.id.SRI_tv_calorie);
            TextView tv_share_account = sharerecipeView.findViewById(R.id.SRI_tv_account);

            TextView tv_share_sport = sharerecipeView.findViewById(R.id.SRI_tv_sport);

            sv_shareRecipeLL.addView(sharerecipeView);

            String _breakfastName = "breakfast: ";
            String _lunchName = "lunch: ";
            String _dinnerName = "dinner: ";
            String _sport = "sport: ";
            for(int i = 0; i< breakfastName.size(); i++)
            {
                _breakfastName += breakfastName.get(i) + " ";
            }
            for(int i = 0; i< lunchName.size(); i++)
            {
                _lunchName += lunchName.get(i) + " ";
            }
            for(int i = 0; i< dinnerName.size(); i++)
            {
                _dinnerName += dinnerName.get(i) + " ";
            }
            for(int i = 0; i< sportName.size(); i++)
            {
                _sport += sportName.get(i) + " ";
            }

            tv_share_breakfast.setText(_breakfastName);
            tv_share_lunch.setText(_lunchName);
            tv_share_dinner.setText(_dinnerName);
            tv_share_sport.setText(_sport);
            tv_share_sharetime.setText(shareTime);
            tv_share_calorie.setText(totalCalorie + " kcal");
            tv_share_account.setText("Account: " + userID);
            changeViewDone = true;
        }
    };
}

