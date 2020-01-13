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

public class Recipe2 extends AppCompatActivity {
    List<String> storeData = new ArrayList<String>();


    //ADDDDDDDDD
    boolean readDone;
///

    private LinearLayout scrollviewLL;
    private LinearLayout scrollviewLL2;
    private LinearLayout scrollviewLL3;

    List<String> breakfastNames = new ArrayList<String>();
    List<String> breakfastCalorie = new ArrayList<String>();
    List<String> lunchNames = new ArrayList<String>();
    List<String> lunchCalorie = new ArrayList<String>();
    List<String> dinnerNames = new ArrayList<String>();
    List<String> dinnerCalorie = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe2);

        scrollviewLL = (LinearLayout) findViewById(R.id.Recipe2_ll_foodItemLayout1);
        scrollviewLL2 = (LinearLayout) findViewById(R.id.Recipe2_ll_foodItemLayout2);
        scrollviewLL3 = (LinearLayout) findViewById(R.id.Recipe2_ll_foodItemLayout3);

        Intent preIntent = getIntent();
        String recipeData =  preIntent.getStringExtra("RecipeData");
        String account = preIntent.getStringExtra("Account");
        String userid = preIntent.getStringExtra("UserID");
        storeData.add(account);
        storeData.add(userid);

        final Button bReturn = (Button)findViewById(R.id.Recipe2_bt_return);
        final TextView tvMan = (TextView)findViewById(R.id.Recipe2_tv_manageRecipe);

        final TextView add1 = (TextView)findViewById(R.id.Recipe2_tv_add1);
        final TextView add2 = (TextView)findViewById(R.id.Recipe2_tv_add2);
        final TextView add3 = (TextView)findViewById(R.id.Recipe2_tv_add3);

        final Button bConfirm = (Button)findViewById(R.id.Recipe2_bt_confirm);

        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addfoodIntent = new Intent(Recipe2.this,AddFood.class);
                String exportData = exportRecipe();
                System.out.println(exportData);
                addfoodIntent.putExtra("RecipeData", exportData);
                addfoodIntent.putExtra("MealTime",0);
                addfoodIntent.putExtra("Account",storeData.get(0));
                addfoodIntent.putExtra("UserID",storeData.get(1));
                Recipe2.this.startActivity(addfoodIntent);
            }
        });

        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addfoodIntent = new Intent(Recipe2.this,AddFood.class);
                String exportData = exportRecipe();
                addfoodIntent.putExtra("RecipeData", exportData);
                addfoodIntent.putExtra("MealTime",1);
                addfoodIntent.putExtra("Account",storeData.get(0));
                addfoodIntent.putExtra("UserID",storeData.get(1));
                Recipe2.this.startActivity(addfoodIntent);
            }
        });

        add3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addfoodIntent = new Intent(Recipe2.this,AddFood.class);
                String exportData = exportRecipe();
                addfoodIntent.putExtra("RecipeData", exportData);
                addfoodIntent.putExtra("MealTime",2);
                addfoodIntent.putExtra("Account",storeData.get(0));
                addfoodIntent.putExtra("UserID",storeData.get(1));
                Recipe2.this.startActivity(addfoodIntent);
            }
        });

        bReturn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent RecipeIntent = new Intent(Recipe2.this,UserAreaActivity.class);
                RecipeIntent.putExtra("Account",storeData.get(0));
                RecipeIntent.putExtra("UserID",storeData.get(1));
                Recipe2.this.startActivity(RecipeIntent);
            }
        });

        tvMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RecipeManIntent = new Intent(Recipe2.this,Recipe.class);
                RecipeManIntent.putExtra("Account",storeData.get(0));
                RecipeManIntent.putExtra("UserID",storeData.get(1));
                Recipe2.this.startActivity(RecipeManIntent);
            }
        });

        bConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {



                shareRecipe();
                //ADD
                Intent RecipeIntent = new Intent (Recipe2.this, Recipe.class);
                RecipeIntent.putExtra("Account", storeData.get(0));
                RecipeIntent.putExtra("UserID", storeData.get(1));
                Recipe2.this.startActivity(RecipeIntent);
///////////////////////////////





            }
        });

        if(recipeData != null)
        {
            analystData(recipeData);
            importRecipe();
        }
    }

    private void analystData(String recipeData)
    {
        int counter_1;
        int counter_head = 0;
        int counter_tail;
        for(counter_1 = 0; counter_1 < 3;)
        {
            if(counter_head >= recipeData.length())
                break;
            if(recipeData.charAt(counter_head) == ';')
            {
                counter_head++;
                counter_1++;
                continue;
            }
            if(recipeData.charAt(counter_head) == '(')
            {
                counter_head++;
                counter_tail = counter_head + 1;
                while(true)
                {
                    char tailC = recipeData.charAt(counter_tail);
                    if(tailC == '(' || tailC == ')' || tailC == ';')
                        break;
                    counter_tail++;
                }
                String currentFoodName = recipeData.substring(counter_head, counter_tail);
                counter_head = counter_tail;
                if(counter_1 == 0)
                    breakfastNames.add(currentFoodName);
                else if(counter_1 == 1)
                    lunchNames.add(currentFoodName);
                else if(counter_1 == 2)
                    dinnerNames.add(currentFoodName);
                continue;
            }
            if(recipeData.charAt(counter_head) == ')')
            {
                counter_head++;
                counter_tail = counter_head + 1;
                while(true)
                {
                    char tailC = recipeData.charAt(counter_tail);
                    if(tailC == '(' || tailC == ')' || tailC == ';')
                        break;
                    counter_tail++;
                }
                String currentFoodCalorie = recipeData.substring(counter_head, counter_tail);
                counter_head = counter_tail;
                if(counter_1 == 0)
                    breakfastCalorie.add(currentFoodCalorie);
                else if(counter_1 == 1)
                    lunchCalorie.add(currentFoodCalorie);
                else if(counter_1 == 2)
                    dinnerCalorie.add(currentFoodCalorie);
                continue;
            }
        }
    }

    private void importRecipe()
    {
        for(int i = 0; i < breakfastNames.size(); i++)
            addViewItem(breakfastNames.get(i), breakfastCalorie.get(i));
        for(int i = 0; i < lunchNames.size(); i++)
            addViewItem2(lunchNames.get(i), lunchCalorie.get(i));
        for(int i = 0; i < dinnerNames.size(); i++)
            addViewItem3(dinnerNames.get(i), dinnerCalorie.get(i));
    }
///  RI_ll_addRecipeItem  RI_tv_foodName  RI_tv_foodDeal  RI_tv_foodDeal2  RI_tv_gram  RI_bt_addFood


    private void addViewItem(String foodName, String foodCalorie)
    {
        final View recipeitemView = View.inflate(this, R.layout.recipeitem, null);
        Button b_remove = (Button) recipeitemView.findViewById(R.id.RI_bt_addFood);
        b_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollviewLL.removeView(recipeitemView);
            }
        });
        TextView tv_name = recipeitemView.findViewById(R.id.RI_tv_foodName);
        tv_name.setText(foodName);
        TextView tv_calorie = recipeitemView.findViewById(R.id.RI_tv_foodDeal2);
        tv_calorie.setText(foodCalorie);
        scrollviewLL.addView(recipeitemView);
    }

    private void addViewItem2(String foodName, String foodCalorie){
        final View recipeitemView = View.inflate(this, R.layout.recipeitem, null);
        Button b_remove = (Button) recipeitemView.findViewById(R.id.RI_bt_addFood);
        b_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollviewLL2.removeView(recipeitemView);
            }
        });
        TextView tv_name = recipeitemView.findViewById(R.id.RI_tv_foodName);
        tv_name.setText(foodName);
        TextView tv_calorie = recipeitemView.findViewById(R.id.RI_tv_foodDeal2);
        tv_calorie.setText(foodCalorie);
        scrollviewLL2.addView(recipeitemView);
    }

    private void addViewItem3(String foodName, String foodCalorie){
        final View recipeitemView = View.inflate(this, R.layout.recipeitem, null);
        Button b_remove = (Button) recipeitemView.findViewById(R.id.RI_bt_addFood);
        b_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollviewLL3.removeView(recipeitemView);
            }
        });
        TextView tv_name = recipeitemView.findViewById(R.id.RI_tv_foodName);
        tv_name.setText(foodName);
        TextView tv_calorie = recipeitemView.findViewById(R.id.RI_tv_foodDeal2);
        tv_calorie.setText(foodCalorie);
        scrollviewLL3.addView(recipeitemView);
    }

    private String exportRecipe() //将三餐的食谱信息分别整合在一起
    {
        breakfastNames.clear();
        breakfastCalorie.clear();
        lunchNames.clear();
        lunchCalorie.clear();
        dinnerNames.clear();
        dinnerCalorie.clear();

        String exportData = "";

        for(int i = 0; i < scrollviewLL.getChildCount(); i++){
            View ChildAt = scrollviewLL.getChildAt(i);
            TextView foodName = ChildAt.findViewById(R.id.RI_tv_foodName);
            exportData = exportData + "(" + foodName.getText();
            TextView foodCalorie = ChildAt.findViewById(R.id.RI_tv_foodDeal2);
            exportData = exportData + ")" + foodCalorie.getText();
        }
        exportData = exportData + ";";

        for(int i = 0; i < scrollviewLL2.getChildCount(); i++)
        {
            View ChildAt = scrollviewLL2.getChildAt(i);
            TextView foodName = ChildAt.findViewById(R.id.RI_tv_foodName);
            exportData = exportData + "(" + foodName.getText();
            TextView foodCalorie = ChildAt.findViewById(R.id.RI_tv_foodDeal2);
            exportData = exportData + ")" + foodCalorie.getText();
        }
        exportData = exportData + ";";

        for(int i = 0; i < scrollviewLL3.getChildCount(); i++)
        {
            View ChildAt = scrollviewLL3.getChildAt(i);
            TextView foodName = ChildAt.findViewById(R.id.RI_tv_foodName);
            exportData = exportData + "(" + foodName.getText();
            TextView foodCalorie = ChildAt.findViewById(R.id.RI_tv_foodDeal2);
            exportData = exportData + ")" + foodCalorie.getText();
        }
        exportData = exportData + ";";

        return exportData;
    }

    private void shareRecipe()
    {
        breakfastNames.clear();
        breakfastCalorie.clear();
        lunchNames.clear();
        lunchCalorie.clear();
        dinnerNames.clear();
        dinnerCalorie.clear();

        for(int i = 0; i < scrollviewLL.getChildCount(); i++) {
            View ChildAt = scrollviewLL.getChildAt(i);
            TextView foodName = ChildAt.findViewById(R.id.RI_tv_foodName);
            breakfastNames.add(foodName.getText().toString());
            TextView foodCalorie = ChildAt.findViewById(R.id.RI_tv_foodDeal2);
            breakfastCalorie.add(foodCalorie.getText().toString());
        }
        for(int i = 0; i < scrollviewLL2.getChildCount(); i++) {
            View ChildAt = scrollviewLL2.getChildAt(i);
            TextView foodName = ChildAt.findViewById(R.id.RI_tv_foodName);
            lunchNames.add(foodName.getText().toString());
            TextView foodCalorie = ChildAt.findViewById(R.id.RI_tv_foodDeal2);
            lunchCalorie.add(foodCalorie.getText().toString());
        }
        for(int i = 0; i < scrollviewLL3.getChildCount(); i++) {
            View ChildAt = scrollviewLL3.getChildAt(i);
            TextView foodName = ChildAt.findViewById(R.id.RI_tv_foodName);
            dinnerNames.add(foodName.getText().toString());
            TextView foodCalorie = ChildAt.findViewById(R.id.RI_tv_foodDeal2);
            dinnerCalorie.add(foodCalorie.getText().toString());
        }

        //ADDDDDDDDDDDDDDDDDD
        readDone = false;
        //////////////////////

        ConnectSQL_ToSharerRecipe();

////////////////////////////
        while(!readDone)
        {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ////////////////////////////////
    }


    public void ConnectSQL_ToSharerRecipe() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection cn = null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    try {
                        String searchSQLCommand;
                        Statement st;
                        ResultSet rs;
                        PreparedStatement pstmt;

                        List<String> breakfastID = new ArrayList<String>();
                        List<String> lunchID = new ArrayList<String>();
                        List<String> dinnerID = new ArrayList<String>();

                        cn = DriverManager.getConnection("jdbc:mysql://172.20.10.3/health", "root", "root");

                        st = (Statement) cn.createStatement();

                        /*Get the breakfast / lunch / dinner food ID*/
                        for(int i = 0; i < breakfastNames.size(); i++)
                        {
                            searchSQLCommand = "select foodID from foodcalorieinfo where foodname = '" + breakfastNames.get(i) + "'";
                            rs = st.executeQuery(searchSQLCommand);
                            rs.next();
                            String currentFoodID = rs.getString("foodID");
                            breakfastID.add(currentFoodID);
                            rs.close();
                        }

                        for(int i = 0; i < lunchNames.size(); i++)
                        {
                            searchSQLCommand = "select foodID from foodcalorieinfo where foodname = '" + lunchNames.get(i) + "'";
                            rs = st.executeQuery(searchSQLCommand);
                            rs.next();
                            String currentFoodID = rs.getString("foodID");
                            lunchID.add(currentFoodID);
                            rs.close();
                        }

                        for(int i = 0; i < dinnerNames.size(); i++)
                        {
                            searchSQLCommand = "select foodID from foodcalorieinfo where foodname = '" + dinnerNames.get(i) + "'";
                            rs = st.executeQuery(searchSQLCommand);
                            rs.next();
                            String currentFoodID = rs.getString("foodID");
                            dinnerID.add(currentFoodID);
                            rs.close();
                        }

                        /*Get the recipe ID*/
                        searchSQLCommand = "SELECT id FROM maxid WHERE name = 'maxrecipeid'";
                        rs = st.executeQuery(searchSQLCommand);
                        rs.next();
                        String s_maxrecipeid = rs.getString("id");
                        int i_maxrecipeid =   Integer.parseInt(s_maxrecipeid);

                        /*Get the share time*/
                        Date nowTime = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");//可以方便地修改日期格式
                        String shareTime = dateFormat.format(nowTime);

                        /*Get the breakfast / lunch / dinner Serve*/
                        float breakfastServe = 0;
                        float lunchServe = 0;
                        float dinnerServe = 0;
                        for(int i = 0; i < breakfastCalorie.size(); i++)
                        {
                            breakfastServe += Float.parseFloat(breakfastCalorie.get(i));
                        }
                        for(int i = 0; i < lunchCalorie.size(); i++)
                        {
                            lunchServe += Float.parseFloat(lunchCalorie.get(i));
                        }
                        for(int i = 0; i < dinnerCalorie.size(); i++)
                        {
                            dinnerServe += Float.parseFloat(dinnerCalorie.get(i));
                        }

                        String s_breakfast = "";
                        String s_lunch = "";
                        String s_dinner = "";

                        for(int i = 0; i < breakfastID.size(); i++)
                        {
                            s_breakfast = s_breakfast + breakfastID.get(i) + "_";
                        }
                        for(int i = 0; i < lunchID.size(); i++)
                        {
                            s_lunch = s_lunch + lunchID.get(i) + "_";
                        }
                        for(int i = 0; i < dinnerID.size(); i++)
                        {
                            s_dinner = s_dinner + dinnerID.get(i) + "_";
                        }

                        String insertSQLCommand = "insert into recipes (recipeID,userID,shareTime,breakfast,lunch,dinner,breakfastServe," +
                                "lunchServe,dinnerServe) values (" + s_maxrecipeid + "," + storeData.get(1) + ",'" + shareTime +"','"
                        + s_breakfast + "','" + s_lunch + "','"+ s_dinner + "','" + Float.toString(breakfastServe) + "','" +
                                Float.toString(lunchServe) + "','" + Float.toString(dinnerServe) + "')";
                        pstmt = (PreparedStatement) cn.prepareStatement(insertSQLCommand);
                        pstmt.executeUpdate();

                        i_maxrecipeid += 1;
                        String modifySQLCommand = "update maxid set id = " + Integer.toString(i_maxrecipeid) + " where name = 'maxrecipeid'";
                        pstmt = (PreparedStatement) cn.prepareStatement(modifySQLCommand);
                        pstmt.executeUpdate();

                        pstmt.close();
                        cn.close();//一定要关闭
                        st.close();

                        //ADDDDDDDD
                        readDone = true;
//////////////////////////////////////////////////



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
