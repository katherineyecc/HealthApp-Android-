package com.example.lenovo.myapplication;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UserAreaActivity extends AppCompatActivity {
     List<String> storeData = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        Intent preIntent = getIntent();
        String account = preIntent.getStringExtra("Account");
        String userid = preIntent.getStringExtra("UserID");
        TextView tv = (TextView) findViewById(R.id.tEmail);
        tv.setText(account);

        storeData.add(account);
        storeData.add(userid);

        final TextView RecipeLink = (TextView)findViewById(R.id.tvRecipe);
        final TextView SportLink = (TextView)findViewById(R.id.tvSport);
        final TextView ShareLink = (TextView)findViewById(R.id.tvShare);
        final TextView ExperienceLink = (TextView)findViewById(R.id.tvExperience);

        RecipeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recipeIntent = new Intent(UserAreaActivity.this,Recipe.class);
                recipeIntent.putExtra("Account",storeData.get(0));
                recipeIntent.putExtra("UserID",storeData.get(1));
                UserAreaActivity.this.startActivity(recipeIntent);
            }
        });

        SportLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent sportIntent = new Intent(UserAreaActivity.this,Sport2.class);
                sportIntent.putExtra("Account",storeData.get(0));
                sportIntent.putExtra("UserID",storeData.get(1));
                UserAreaActivity.this.startActivity(sportIntent);
            }
        });

        ShareLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent shareIntent = new Intent(UserAreaActivity.this,ShareRecipe.class);
                shareIntent.putExtra("Account",storeData.get(0));
                shareIntent.putExtra("UserID",storeData.get(1));
                UserAreaActivity.this.startActivity(shareIntent);
            }
        });

        ExperienceLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent experienceIntent = new Intent(UserAreaActivity.this, Experience.class);
                experienceIntent.putExtra("Account", storeData.get(0));
                experienceIntent.putExtra("UserID", storeData.get(1));
                UserAreaActivity.this.startActivity(experienceIntent);
            }
        });


    }
}
