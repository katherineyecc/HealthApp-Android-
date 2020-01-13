package com.example.lenovo.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddFood extends AppCompatActivity {
    List<String> storeData = new ArrayList<String>();

    int textColor = 0;

    String recipeData;

    String exportData_FoodName;
    String exportData_FoodCalorie;

    List<String> foodname_string = new ArrayList<>();
    List<Integer> foodcalorie_int = new ArrayList<>();

    TextView chooseFood = null;

    private LinearLayout AF_scrollviewLL;

    boolean ifDoneRead = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        Intent preIntent = getIntent();

        recipeData =  preIntent.getStringExtra("RecipeData");
        String account = preIntent.getStringExtra("Account");
        String userid = preIntent.getStringExtra("UserID");
        storeData.add(account);
        storeData.add(userid);

        final int MealTime = preIntent.getIntExtra("MealTime", 0);

        final EditText et_weight = findViewById(R.id.AF_et_weight);

        AF_scrollviewLL = (LinearLayout) findViewById(R.id.AF_ll_layout);
        ConnectSQL();
        while(!ifDoneRead)
        {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        for(int i = 0; i < foodname_string.size(); i++)
            addViewItem(foodname_string.get(i), foodcalorie_int.get(i));

        findViewById(R.id.AF_bt_confirm).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String s_weight = et_weight.getText().toString();
                float d_weight = Float.parseFloat(s_weight) / 100;

                float d_calorie = Float.parseFloat(exportData_FoodCalorie);
                float d_exportCalorie = d_calorie * d_weight;

                exportData_FoodCalorie = String.format("%.2f", d_exportCalorie);

                if(MealTime == 0)
                {
                    recipeData = "(" + exportData_FoodName + ")" + exportData_FoodCalorie + recipeData;
                }
                else if(MealTime == 1)
                {
                    String headString;
                    String tailString;

                    int counter = 1;

                    for(int i = 0; i < recipeData.length(); i++)
                    {
                        if(recipeData.charAt(i) == ';')
                        {
                            if(counter == 1)
                            {
                                headString = recipeData.substring(0,i+1);
                                tailString = recipeData.substring(i+1,recipeData.length());
                                recipeData = headString + "(" + exportData_FoodName + ")" + exportData_FoodCalorie + tailString;
                                break;
                            }
                            else
                            {
                                counter++;
                            }
                        }
                    }
                }
                else if(MealTime == 2)
                {
                    String headString;
                    String tailString;

                    int counter = 1;

                    for(int i = 0; i < recipeData.length(); i++)
                    {
                        if(recipeData.charAt(i) == ';')
                        {
                            if(counter == 2)
                            {
                                headString = recipeData.substring(0,i+1);
                                tailString = recipeData.substring(i+1,recipeData.length());
                                recipeData = headString + "(" + exportData_FoodName + ")" + exportData_FoodCalorie + tailString;
                                break;
                            }
                            else
                            {
                                counter++;
                            }
                        }
                    }
                }
                Intent Recipe2 = new Intent(AddFood.this,Recipe2.class);
                Recipe2.putExtra("RecipeData", recipeData);
                Recipe2.putExtra("Account",storeData.get(0));
                Recipe2.putExtra("UserID",storeData.get(1));
                AddFood.this.startActivity(Recipe2);
            }
        });
    }

    private void addViewItem(String fn, int fc){
        final View AF_itemView = View.inflate(this, R.layout.fooditem, null);

        AF_scrollviewLL.addView(AF_itemView);

        TextView AFtv_FoodName = AF_itemView.findViewById(R.id.FI_tv_foodName);
        TextView AFtv_calorie = AF_itemView.findViewById(R.id.FI_tv_calorie);

        String setFn = fn;

        AFtv_FoodName.setText(setFn);
        AFtv_calorie.setText(Integer.toString(fc));

        AFtv_FoodName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                TextView current_FoodName = AF_itemView.findViewById(R.id.FI_tv_foodName);
                TextView current_FoodCalorie = AF_itemView.findViewById(R.id.FI_tv_calorie);
                exportData_FoodName = (String)current_FoodName.getText();

                exportData_FoodCalorie = (String)current_FoodCalorie.getText();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                textColor = current_FoodName.getCurrentTextColor();
                current_FoodName.setTextColor(Color.rgb(0, 0, 255));
                if(chooseFood != null)
                {
                    chooseFood.setTextColor(textColor);
                }
                chooseFood = current_FoodName;
            }
        });
    }

    public void ConnectSQL() {
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

                        cn = DriverManager.getConnection("jdbc:mysql://172.20.10.3/health", "root", "root");
                        searchSQLCommand = "select * from foodcalorieinfo ";
                        st = (Statement) cn.createStatement();
                        rs = st.executeQuery(searchSQLCommand);
                        while(rs.next())
                        {
                            String s_foodname = rs.getString("foodname");
                            foodname_string.add(s_foodname);
                            String s_foodcalorie = rs.getString("caloriePerServe");
                            float float_foodcalorie =   Float.parseFloat(s_foodcalorie);
                            int int_foodcalorie = (int)float_foodcalorie;
                            foodcalorie_int.add(int_foodcalorie);
                        }
                        cn.close();//一定要关闭
                        st.close();
                        rs.close();

                        ifDoneRead = true;
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
