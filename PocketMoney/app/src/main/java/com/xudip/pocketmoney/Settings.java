package com.xudip.pocketmoney;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {


    LinearLayout framebackgroundColor,
            framebuttonColor,
            framesetTimeToNotify,
            framesetToDefault,
            framebackUpData;

    String backgroundColor = "WHITE";
    String buttonColor = "WHITE";
    String choosenColor;
    String getBackgroundColor;
    String getButtonColor;

    Dialog customDialog;
    AlertDialog.Builder builder;


    int backgrouncColorCount = 0, buttonColorCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        customDialog = new Dialog(this);
        builder = new AlertDialog.Builder(this);


        framebackgroundColor = (LinearLayout) findViewById(R.id.gridBackgroundColor);
        framebuttonColor = (LinearLayout) findViewById(R.id.gridButtonColor);
        framebackUpData = (LinearLayout) findViewById(R.id.gridBackUpData);
        framesetTimeToNotify = (LinearLayout) findViewById(R.id.gridSetTimeToNotify);
        getSavedSettings();
        View root = framebackUpData.getRootView();
        try{
            root.setBackgroundColor(Color.parseColor(getBackgroundColor));
            framebackgroundColor.setBackgroundColor(Color.parseColor(getBackgroundColor));
            framebuttonColor.setBackgroundColor(Color.parseColor(getButtonColor));
        }catch(java.lang.StringIndexOutOfBoundsException e){
//            Toast.makeText(this, "Error in loading settings.\n Restoring Default Colors", Toast.LENGTH_SHORT).show();
        }


        registerForContextMenu(framebuttonColor);
        registerForContextMenu(framebackgroundColor);

        framesetTimeToNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getterSetterAddIncome.getUnderConstruction(customDialog);
            }
        });

        framebackUpData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getterSetterAddIncome.getUnderConstruction(customDialog);
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("CHOOSE A COLOR:");
        menu.add(0, v.getId(), 0, "DEFAULT");
        menu.add(0, v.getId(), 0, "RED");
        menu.add(0, v.getId(), 0, "GREEN");
        menu.add(0, v.getId(), 0, "BLUE");
        menu.add(0, v.getId(), 0, "YELLOW");
        menu.add(0, v.getId(), 0, "GRAY");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        choosenColor = String.valueOf(item.getTitle());
        if(choosenColor.equals("DEFAULT")){
            choosenColor ="WHITE";
        }
        if(backgrouncColorCount == 1){
            backgroundColor = choosenColor;

            framebackgroundColor.setBackgroundColor(Color.parseColor(backgroundColor));
            backgrouncColorCount = 0;
        }else{
            backgroundColor = getBackgroundColor;

        }

        if(buttonColorCount == 1){
            buttonColor = choosenColor;
            framebuttonColor.setBackgroundColor(Color.parseColor(buttonColor));
            buttonColorCount = 0;
        }else{
            buttonColor = getButtonColor;
        }

//        Toast.makeText(this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
        return true;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_transactions);
            startActivity(new Intent(this, Home.class));
            this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itemSave) {
          getAlertDialog("Ready To Save?");
        }

        if(id == R.id.itemRestoreToDefault){
            getAlertDialog("Restore To Default?");
        }

        return super.onOptionsItemSelected(item);
    }

    private void getAlertDialog(final String s) {
        builder.setMessage(s);
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                switch (s) {
                    case "Ready To Save?":
                        saveSettings();
                        Toast.makeText(Settings.this, "YES! :D \nSAVED :D", Toast.LENGTH_SHORT).show();
                        break;
                    case "Restore To Default?":
                        backgroundColor = "WHITE";
                        buttonColor = "WHITE";
                        saveSettings();
                        Toast.makeText(Settings.this,"YES! :D \nRESTORED DEFAULT",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(Settings.this,"CANCELLED!",Toast.LENGTH_SHORT).show();
                        break;
                    }



            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        AlertDialog confirmDelete = builder.create();
        confirmDelete.show();
    }


    public void actionBackgroundColor(View v){
//        framebackgroundColor.setBackgroundColor(Color.BLUE);
//        framebackgroundColor.getDividerDrawable();
//        Toast.makeText(this,"Background Color Frame Clicked", Toast.LENGTH_SHORT).show();
        backgrouncColorCount = 1;
        openContextMenu(framebackgroundColor);

//        startActivity(new Intent(this,ListOfColors.class));
//        this.finish();
    }

    public void actionButtonColor(View v1){
        buttonColorCount = 1;
        openContextMenu(framebuttonColor);
    }

    public void saveSettings(){
        SharedPreferences set = getSharedPreferences("xud", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = set.edit();
        editor.putString("backgroundColor", backgroundColor);
        editor.putString("buttonColor", buttonColor);
        editor.apply();
    }

    public void getSavedSettings(){
        SharedPreferences get = getSharedPreferences("xud", Context.MODE_PRIVATE);
        getBackgroundColor = get.getString("backgroundColor", "");
        getButtonColor = get.getString("buttonColor","");
//        Toast.makeText(this,getBackgroundColor,Toast.LENGTH_SHORT).show();
    }

}

