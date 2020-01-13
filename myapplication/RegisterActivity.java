package com.example.lenovo.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class    RegisterActivity extends AppCompatActivity {
    String Age,Email,Password,Height,Weight,Sex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etAge = (EditText) findViewById(R.id.Register_et_age);
        final EditText etEmail = (EditText) findViewById(R.id.Register_et_eamil);
        final EditText etPassword = (EditText) findViewById(R.id.Register_et_Password);
        final EditText etHeight = (EditText) findViewById(R.id.Register_et_height);
        final EditText etWeight = (EditText) findViewById(R.id.Register_et_weight);
        final EditText etSex = (EditText) findViewById(R.id.Register_et_sex);

        final Button bRegister = (Button) findViewById(R.id.Register_bt_register);

        bRegister.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Age = etAge.getText().toString();
                Email = etEmail.getText().toString();
                Password = etPassword.getText().toString();
                Height = etHeight.getText().toString();
                Weight = etWeight.getText().toString();
                Sex = etSex.getText().toString();

                ConnectSQL();

                switch (view.getId()){
                    case R.id.Register_bt_register:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                        dialog.setTitle("AlertDialog");
                        dialog.setMessage("Register Success!");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent okIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                                RegisterActivity.this.startActivity(okIntent);
                            }
                        });
                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        dialog.show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void ConnectSQL() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> datas = new ArrayList<>();
                Connection cn = null;
                int operationResult = 0;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    try {
                        String searchSQLCommand;
                        String insertSQLCommand;
                        String modifySQLCommand;
                        PreparedStatement pstmt;
                        Statement st;
                        ResultSet rs;

                        cn = DriverManager.getConnection("jdbc:mysql://172.20.10.3/health","root","root");
                        searchSQLCommand = "SELECT id FROM maxid WHERE name = 'maxuserid'";
                        st = (Statement)cn.createStatement();
                        rs = st.executeQuery(searchSQLCommand);

                        rs.next();
                        String s_maxuserid = rs.getString("id");

                        int i_maxuserid =   Integer.parseInt(s_maxuserid);

                        insertSQLCommand = "insert into users (userID,userEmail,userPassword,userType) values (" +
                                Integer.toString(i_maxuserid) + ",'" + Email +"','" + Password + "','user')";
                        pstmt = (PreparedStatement) cn.prepareStatement(insertSQLCommand);
                        pstmt.executeUpdate();

                        insertSQLCommand = "insert into ordinaryuser (userID,userPassword,height,weight,age,sex,userType) values (" +
                                Integer.toString(i_maxuserid) + ",'" + Password + "',"+ Height +"," + Weight+ "," + Age +",'" + Sex +"','user')";
                        pstmt = (PreparedStatement) cn.prepareStatement(insertSQLCommand);
                        pstmt.executeUpdate();

                        i_maxuserid += 1;
                        modifySQLCommand = "update maxid set id = " + Integer.toString(i_maxuserid) + " where name = 'maxuserid'";

                        pstmt = (PreparedStatement) cn.prepareStatement(modifySQLCommand);
                        pstmt.executeUpdate();

                        pstmt.close();
                        cn.close();//一定要关闭
                        st.close();
                        rs.close();

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
