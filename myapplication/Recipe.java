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

public class Recipe extends AppCompatActivity {
    List<String> storeData = new ArrayList<String>();

    private LinearLayout sv_recipeLL;

    private Handler handler=null;//在主线程和子线程之间传递消息

    List<String> breakfastName = new ArrayList<String>();
    List<String> lunchName = new ArrayList<String>();
    List<String> dinnerName = new ArrayList<String>();
    String shareTime;
    String totalCalorie;
    String recipeID;

    boolean changeViewDone = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Intent preIntent = getIntent();
        String account = preIntent.getStringExtra("Account");
        String userid = preIntent.getStringExtra("UserID");
        storeData.add(account);
        storeData.add(userid);

        handler=new Handler();

        final Button bReturn = (Button)findViewById(R.id.Recipe_bt_return);
        final TextView tvAdd = (TextView)findViewById(R.id.Recipe_tv_addRecipe);

        bReturn.setOnClickListener(new Button.OnClickListener(){
            @Override
            //按下return键之后就返回主页面（UserAreaActivity）
            public void onClick(View view) {
                Intent RecipeIntent = new Intent(Recipe.this,UserAreaActivity.class);
                RecipeIntent.putExtra("Account",storeData.get(0));
                RecipeIntent.putExtra("UserID",storeData.get(1));
                Recipe.this.startActivity(RecipeIntent);
            }
        });

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            //按下添加食谱键（add）之后，跳转到Recipe2页面
            public void onClick(View view) {
                Intent RecipeAddIntent = new Intent(Recipe.this,Recipe2.class);
                RecipeAddIntent.putExtra("Account",storeData.get(0));
                RecipeAddIntent.putExtra("UserID",storeData.get(1));
                Recipe.this.startActivity(RecipeAddIntent);
            }
        });
        sv_recipeLL = findViewById(R.id.Recipe_ll_foodItemLayout);
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
                        Statement st;
                        Statement st2;

                        ResultSet rs1;
                        ResultSet rs2;

                        cn = DriverManager.getConnection("jdbc:mysql://172.20.10.3/health", "root", "root");

                        st = (Statement) cn.createStatement();
                        st2 = (Statement) cn.createStatement();

                        int userid = Integer.parseInt(storeData.get(1));

                        searchSQLCommand = "select * from recipes where userID = " + Integer.toString(userid) + ";";
                        rs1 = st.executeQuery(searchSQLCommand);

                        while(rs1.next())
                        {
                            //从recipe表中获得userID对应的食谱，接收食谱中的各种数据
                            changeViewDone = false;

                            shareTime = rs1.getString("shareTime");
                            recipeID = rs1.getString("recipeID");
                            String breakfastNum = rs1.getString("breakfast");
                            String lunchNum = rs1.getString("lunch");
                            String dinnerNum = rs1.getString("dinner");

                            String breakfastServe = rs1.getString("breakfastServe");
                            String lunchServe = rs1.getString("lunchServe");
                            String dinnerServe = rs1.getString("dinnerServe");
                            double  totalCalorieInt = Double.parseDouble(breakfastServe) + Double.parseDouble(lunchServe) + Double.parseDouble(dinnerServe);
                            totalCalorie =  String.format("%.2f", totalCalorieInt);

                            breakfastName.clear();
                            lunchName.clear();
                            dinnerName.clear();


                            int head = 0;
                            int tail = 0;

                            while(head < breakfastNum.length())
                            {
                                if(breakfastNum.charAt(head) == '_')
                                {
                                    head++;
                                    continue;
                                }
                                tail = head + 1;
                                while(breakfastNum.charAt(tail) != '_')
                                {
                                    tail++;
                                }
                                breakfastName.add(breakfastNum.substring(head,tail));

                                head = tail + 1;
                            }

                            head = 0;
                            while(head < lunchNum.length())
                            {
                                if(lunchNum.charAt(head) == '_')
                                {
                                    head++;
                                    continue;
                                }
                                tail = head + 1;
                                while(lunchNum.charAt(tail) != '_')
                                {
                                    tail++;
                                }
                                lunchName.add(lunchNum.substring(head,tail));

                                head = tail + 1;
                            }

                            head = 0;
                            while(head < dinnerNum.length())
                            {
                                if(dinnerNum.charAt(head) == '_')
                                {
                                    head++;
                                    continue;
                                }
                                tail = head + 1;
                                while(dinnerNum.charAt(tail) != '_')
                                {
                                    tail++;
                                }
                                dinnerName.add(dinnerNum.substring(head,tail));

                                head = tail + 1;
                            }

                            //根据breakfastName、lunchName、dinnerName中的foodID ,跳转到foodcalorieinfo表中
                            //读取对应的foodname，这时候就读取完一张食谱中的所有信息了
                            for(int i = 0; i < breakfastName.size(); i++)
                            {
                                searchSQLCommand = "select foodname from foodcalorieinfo where foodID = " + breakfastName.get(i) +";";
                                rs2 = st2.executeQuery(searchSQLCommand);
                                rs2.next();
                                breakfastName.set(i,rs2.getString("foodname"));
                                rs2.close();
                            }

                            for(int i = 0; i < lunchName.size(); i++)
                            {
                                searchSQLCommand = "select foodname from foodcalorieinfo where foodID = " + lunchName.get(i) +";";
                                rs2 = st2.executeQuery(searchSQLCommand);
                                rs2.next();
                                lunchName.set(i,rs2.getString("foodname"));
                                rs2.close();
                            }

                            for(int i = 0; i < dinnerName.size(); i++)
                            {
                                searchSQLCommand = "select foodname from foodcalorieinfo where foodID = " + dinnerName.get(i) +";";
                                rs2 = st2.executeQuery(searchSQLCommand);
                                rs2.next();
                                dinnerName.set(i,rs2.getString("foodname"));
                                rs2.close();
                            }
                            handler.post(runnableUI);
                            while(!changeViewDone);
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
            final View manage_riView = View.inflate(Recipe.this,R.layout.managerecipeitem,null);
            //View.inflate是用来加载setContentView之外的xml文件中的layout
            //当前setContentView是activity_recipe，需要加载的布局是managerecipeitem

            TextView tv_ri_breakfast = manage_riView.findViewById(R.id.MRI_tv_manageBreakfast);
            TextView tv_ri_lunch = manage_riView.findViewById(R.id.MRI_tv_manageLunch);
            TextView tv_ri_dinner = manage_riView.findViewById(R.id.MRI_tv_manageDinner);
            TextView tv_ri_sharetime = manage_riView.findViewById(R.id.MRI_tv_shareTime);
            TextView tv_ri_calorie = manage_riView.findViewById(R.id.MRI_tv_manageCalorie);

            sv_recipeLL.addView(manage_riView);

            String _breakfastName = "breakfast: ";
            String _lunchName = "lunch: ";
            String _dinnerName = "dinner: ";
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
            tv_ri_breakfast.setText(_breakfastName);
            tv_ri_lunch.setText(_lunchName);
            tv_ri_dinner.setText(_dinnerName);
            tv_ri_sharetime.setText(shareTime);
            tv_ri_calorie.setText(totalCalorie + " J");

            Button tv_ri_remove = manage_riView.findViewById(R.id.MRI_bt_remove);
            tv_ri_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sv_recipeLL.removeView(manage_riView);
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

                        int recipeID_int = Integer.parseInt(recipeID);

                        searchSQLCommand2 = "delete from recipes where recipeID = " + recipeID_int + ";";
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
