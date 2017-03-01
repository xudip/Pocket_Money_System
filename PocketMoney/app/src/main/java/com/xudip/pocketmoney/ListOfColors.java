package com.xudip.pocketmoney;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ListOfColors extends AppCompatActivity{

    ListView listOfColors;
    Button btncolor;

    private String TAG = ListOfColors.class.getSimpleName();
//    float initialX, finalX;

   public GestureDetectorCompat gestureDetector;


    static String selectedBackgroundColor;
    int x = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_colors);

        listOfColors = (ListView) findViewById(R.id.listOfColors);
//        btncolor = (Button) findViewById(R.id.btnColor);



        String []color = {"1", "2", "3","4", "5"};

        ListAdapter item = new CustomAdapterForColor(this,color);
        listOfColors.setAdapter(item);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_transactions);
            startActivity(new Intent(this, Home.class));
            this.finish();
    }

}



