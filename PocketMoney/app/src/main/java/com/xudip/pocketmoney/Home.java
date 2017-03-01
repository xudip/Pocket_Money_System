package com.xudip.pocketmoney;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.ContextMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ResourceBundle;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NotificationCompat.Builder notification;
    private static final int uniqueId = 123;
    public static String NOTIFICATION = "notification";


    String getBackgroundColor;
    String getButtonColor;

    Dialog customDialog;


    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        customDialog = new Dialog(this);

        Button btnAddIncome = (Button) findViewById(R.id.btnAddIncome);
        Button btnAddExpense = (Button) findViewById(R.id.btnAddExpense);
        Button btnExit = (Button) findViewById(R.id.btnExit);
        Button btnTransactions = (Button) findViewById(R.id.btnTransactions);
        Button btnSetting = (Button) findViewById(R.id.btnSettings);
        Button btnViewReport = (Button) findViewById(R.id.btnViewReport);

        getSavedSettings();
        try {
            View root = btnAddExpense.getRootView();
            root.setBackgroundColor(Color.parseColor(getBackgroundColor));
            btnAddIncome.setBackgroundColor(Color.parseColor(getButtonColor));
            btnAddExpense.setBackgroundColor(Color.parseColor(getButtonColor));
            btnExit.setBackgroundColor(Color.parseColor(getButtonColor));
            btnTransactions.setBackgroundColor(Color.parseColor(getButtonColor));
            btnSetting.setBackgroundColor(Color.parseColor(getButtonColor));
            btnViewReport.setBackgroundColor(Color.parseColor(getButtonColor));
        }catch(java.lang.StringIndexOutOfBoundsException e){
//            Toast.makeText(this, "Error in loading settings.\n Restoring Default Colors", Toast.LENGTH_SHORT).show();
        }

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        DBHandler remaining = new DBHandler(this, null, null, 1);
        remaining.getBalanceDirect();
        TextView savingAccount = (TextView) findViewById(R.id.SavingAccount);
        TextView expendableAccount = (TextView) findViewById(R.id.ExpendableAccount);

        savingAccount.setText(getterSetterAddIncome.getSavings());
        expendableAccount.setText(getterSetterAddIncome.getExpendables());

        btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                getaddIncome();
            }
        });

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                getactionAddExpense();
            }
        });

        btnTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                getactionTransactions();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                getactionExit();
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view3) {
              getactionSettings();
            }
        });


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Toast.makeText(this, "You are in main Screen", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.addIncome) {
            // Handle the camera action
            startActivity(new Intent(this, AddIncome.class));
            this.finish();

        } else if (id == R.id.addExpense) {
            startActivity(new Intent(this, AddExpenses.class));
            this.finish();
        } else if (id == R.id.home) {
            startActivity(new Intent(this, Home.class));
            this.finish();
        } else if (id == R.id.viewAllTransactions) {
            this.finish();
            startActivity(new Intent(this, Transactions.class));
        } else if (id == R.id.exit) {
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void getaddIncome() {
        startActivity(new Intent(this, AddIncome.class));
        this.finish();
    }

    public void getactionAddExpense() {
        startActivity(new Intent(this, AddExpenses.class));
        this.finish();
    }

    public void getactionExit() {
        this.finish();
    }

    public void getactionTransactions() {
        startActivity(new Intent(this, Transactions.class));
        this.finish();
    }

    public void getactionSettings(){
        startActivity(new Intent(this,Settings.class));
        this.finish();
    }


    public void actionViewAReport(View v) {

        startActivity(new Intent(this,ListOfColors.class));
        this.finish();


//        notification.setSmallIcon(R.drawable.ic_menu_gallery);
//        notification.setTicker("REPORT READY");
//        notification.setWhen(System.currentTimeMillis());
//        notification.setContentTitle("REPORT:");
//        notification.setContentText("THIS IS THE REPORT: \n :D :D :D");
//
//        Intent intent = new Intent(this, Home.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        notification.setContentIntent(pendingIntent);
//
//        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            nm.notify(uniqueId,notification.build());

//            getterSetterAddIncome.getUnderConstruction(customDialog);

    }

    public void getSavedSettings(){
        SharedPreferences get = getSharedPreferences("xud", Context.MODE_PRIVATE);
        getBackgroundColor = get.getString("backgroundColor", "");
        getButtonColor = get.getString("buttonColor","");
    }




}
