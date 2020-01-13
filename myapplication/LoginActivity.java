package com.example.lenovo.myapplication;


import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    String Email,Password;
    EditText etEmail, etPassword;
    boolean if_Login_success = true;
    boolean if_login = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.Login_et_email);
        etPassword = (EditText) findViewById(R.id.Login_et_password);

        final Button bLogin = (Button) findViewById(R.id.Login_bt_login);
        final TextView RegisterLink = (TextView) findViewById(R.id.Login_tv_register);

        RegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        bLogin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email= etEmail.getText().toString();
                Password = etPassword.getText().toString();
                if_login = false;
                ConnectSQL();
                while(!if_login);
                if(!if_Login_success)
                {
                    switch (view.getId()) {
                    case R.id.Login_bt_login:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                        dialog.setTitle("AlertDialog");
                        dialog.setMessage("Login Failed !!");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Try Again ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        dialog.show();
                        break;
                    default:
                        break;
                }}
                if_Login_success = true;
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

                        searchSQLCommand = "select * from users where userEmail = '" + Email + "'";
                        st = (Statement) cn.createStatement();
                        rs = st.executeQuery(searchSQLCommand);

                        rs.next();
                        String s_userPassword = rs.getString("userPassword");
                        String s_userId = rs.getString("userID");

                        if (s_userPassword.equals(Password)) {
                            if_Login_success = true;
                            cn.close();//一定要关闭
                            st.close();
                            rs.close();

                            //用Intent来实现活动之间的跳转

                            Intent userareaIntent = new Intent(LoginActivity.this, UserAreaActivity.class);
                            userareaIntent.putExtra("Account",Email);
                            userareaIntent.putExtra("UserID",s_userId);
                            LoginActivity.this.startActivity(userareaIntent);
                        } else {
                            if_Login_success = false;
                            cn.close();//一定要关闭
                            st.close();
                            rs.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if_login = true;
            }
        }).start();
    }
}
