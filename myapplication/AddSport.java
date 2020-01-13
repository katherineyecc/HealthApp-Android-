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

public class AddSport extends AppCompatActivity {
    List<String>storeData = new ArrayList<String>();

    int textColor = 0;

    String sportData;

    String exportData_SportName;
    String exportData_SportCalorie;

    List<String> sportname_string = new ArrayList<>();
    List<Integer> sportcalorie_int = new ArrayList<>();

    TextView chooseSport = null;

    private LinearLayout AS_scrollviewLL;

    boolean ifDoneRead = false;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sport);

        Intent preIntent = getIntent();
        sportData = preIntent.getStringExtra("SportData");
        String account = preIntent.getStringExtra("Account");
        String userid = preIntent.getStringExtra("UserID");
        storeData.add(account);
        storeData.add(userid);

        final EditText et_duration = findViewById(R.id.AS_et_duration);

        AS_scrollviewLL = (LinearLayout)findViewById(R.id.AS_ll_layout);
        ConnectSQL();
        while(!ifDoneRead)
        {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        for(int i = 0; i < sportname_string.size(); i++)
            addViewItem(sportname_string.get(i), sportcalorie_int.get(i));

        findViewById(R.id.AS_bt_confirm).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String s_duration = et_duration.getText().toString();
                float d_duration = Float.parseFloat(s_duration) / 30;
                float d_calorie = Float.parseFloat(exportData_SportCalorie);
                float d_exportCalorie = d_calorie * d_duration;

                exportData_SportCalorie = String.format("%.2f", d_exportCalorie);

                sportData = "(" + exportData_SportName + ")" + exportData_SportCalorie + sportData;

                Intent Sport = new Intent(AddSport.this, Sport.class);
                Sport.putExtra("SportData", sportData);
                Sport.putExtra("Account", storeData.get(0));
                Sport.putExtra("UserID", storeData.get(1));
                AddSport.this.startActivity(Sport);
            }
        });
    }

    private void addViewItem(String sn, int sc) {
        final View AS_itemView = View.inflate(this, R.layout.sportitem, null);

        AS_scrollviewLL.addView(AS_itemView);

        TextView AStv_SportName = AS_itemView.findViewById(R.id.SI_tv_sportName);
        TextView AStv_calorie = AS_itemView.findViewById((R.id.SI_tv_calorie));

        String setSn = sn;

        AStv_SportName.setText(setSn);
        AStv_calorie.setText(Integer.toString(sc));

        AStv_SportName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                TextView current_SportName = AS_itemView.findViewById(R.id.SI_tv_sportName);
                TextView current_SportCalorie = AS_itemView.findViewById(R.id.SI_tv_calorie);
                exportData_SportName = (String)current_SportName.getText();
                exportData_SportCalorie = (String)current_SportCalorie.getText();
                System.out.println(exportData_SportCalorie);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                textColor = current_SportName.getCurrentTextColor();
                current_SportName.setTextColor(Color.rgb(0, 0, 255));
                if(chooseSport != null)
                {
                    chooseSport.setTextColor(textColor);
                }
                chooseSport = current_SportName;
            }
        });
    }

    public void ConnectSQL() {
        new Thread(new Runnable() {
            @Override
            public void run(){
                Connection cn = null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    try {
                        String searchSQLCommand;
                        Statement st;
                        ResultSet rs;

                        cn = DriverManager.getConnection("jdbc:mysql://172.20.10.3/health", "root", "root");

                        searchSQLCommand = "select * from sportconsumecalorieinfo";
                        st = (Statement) cn.createStatement();
                        rs = st.executeQuery(searchSQLCommand);

                        while (rs.next()) {
                            String s_sportname = rs.getString("sportName");
                            sportname_string.add(s_sportname);
                            String s_sportcalorie = rs.getString("consumingCaloriePerTimeUnit");
                            float float_sportcalorie = Float.parseFloat(s_sportcalorie);
                            int int_sportcalorie = (int)float_sportcalorie;
                            sportcalorie_int.add(int_sportcalorie);
                        }

                        cn.close();
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
