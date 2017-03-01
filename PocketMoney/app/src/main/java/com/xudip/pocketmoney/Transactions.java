package com.xudip.pocketmoney;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

public class Transactions extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnTouchListener{

    DBHandler db;

    Vector<Integer> listItemId = new Vector<Integer>(10);
    Integer selectedIdOfListOfTransaction;

    int incomeStatus = 1;
    int expenseStatus = 1;
    int deletedStatus = 0;

    Button view;
    ScrollView scrollViewWholeScreen;
    ScrollView scrollViewListOfTransactions;

    ListView listOfTransactions;
    TextView savingAccount;
    TextView expendableAccount;

    CheckBox viewIncomes;
    CheckBox viewExpense;
    CheckBox viewDeleted;

    TextView lblSavingAccount;

    Spinner spinnerChooseSorting;
    getterSetterAddIncome getterSetterViewATransaction;

    Vector<String> listItemSource = new Vector<String>(10);
    Vector<String> listItemAmount = new Vector<String>(10);
    Vector<String> listItemCustomDescription = new Vector<String>(10);
    Vector<String> listItemCategory = new Vector<String>(10);
    Vector<String> listItemDate = new Vector<String>(10);
    Vector<String> listItemTime = new Vector<String>(10);
    Vector<Integer> listItemTransactionType = new Vector<Integer>(10);

    Cursor c = null;
    getterSetterAddIncome selected;

    int confirmation=4;

    String getBackgroundColor;
    String getButtonColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

//        RelativeLayout l = (RelativeLayout) findViewById(R.id.activity_view_atransaction);

//        view = (Button) findViewById(R.id.btnView);
        listOfTransactions = (ListView) findViewById(R.id.listOfTransactions);

        getSavedSettings();
        View root = listOfTransactions.getRootView();
        root.setBackgroundColor(Color.parseColor(getBackgroundColor));


//        spinnerChooseSorting = (Spinner) findViewById(R.id.spinnerChooseSorting);
        getterSetterViewATransaction = new getterSetterAddIncome();
        viewIncomes = (CheckBox) findViewById(R.id.lblListIncomes);
        viewExpense = (CheckBox) findViewById(R.id.lblListExpense);
//        viewDeleted = (CheckBox) findViewById(R.id.lblListDeleted);
        lblSavingAccount = (TextView) findViewById(R.id.lblSavingAccount );

//        String sortingItems[] = {"INCOME ONLY", "EXPENSE ONLY", "INCOME AND EXPENSES", "DELETED ONES", "ALL WITH DELETED ONES"};
//        ArrayAdapter<String> adapterSorting = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, sortingItems);
//        spinnerChooseSorting.setAdapter(adapterSorting);

        DBHandler remaining = new DBHandler(this, null, null , 1);
        remaining.getBalanceDirect();
        savingAccount = (TextView) findViewById(R.id.SavingAccount);
        expendableAccount = (TextView) findViewById(R.id.ExpendableAccount);
        savingAccount.setText(getterSetterAddIncome.getSavings());
        expendableAccount.setText(getterSetterAddIncome.getExpendables());

        selected = new getterSetterAddIncome();

        db = new DBHandler(this, null, null , 1);
//        clearListOfTransaction();
        getTransactions();

//        String []color = {"RED", "GREEN", "BLUE","YELLOW", "GRAY", "WHITE"};
//
//        ListAdapter item = new CustomAdapterForColor(this,color);
//        listOfTransactions.setAdapter(item);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_transactions);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listOfTransactions.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event)
            {
//                Log.v(TAG,”CHILD TOUCH”);
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

//        listOfTransactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//            }
//        });

        listOfTransactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                selectedIdOfListOfTransaction = listItemId.get(i);

                getterSetterViewATransaction.setSource(listItemSource.get(i));
                getterSetterViewATransaction.setTotalAmount(listItemAmount.get(i));
                getterSetterViewATransaction.setCurrentDate(listItemDate.get(i));
                getterSetterViewATransaction.setCurrentTime(listItemTime.get(i));
                getterSetterViewATransaction.setTransactionType(listItemTransactionType.get(i));
                getterSetterViewATransaction.setCategory(listItemCategory.get(i));
                getterSetterViewATransaction.setCustomDescription(listItemCustomDescription.get(i));


                if(listItemTransactionType.get(i)==1){
                    selected.setTransactionType(0);
                }
                else{
                    selected.setTransactionType(1);
                }
                selected.setCategory(listItemCategory.get(i));
                selected.setTotalAmount(listItemAmount.get(i));


//                getViewATransaction();
            }

        });

        listOfTransactions.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                selectedIdOfListOfTransaction = listItemId.get(i);

                getterSetterViewATransaction.setSource(listItemSource.get(i));
                getterSetterViewATransaction.setTotalAmount(listItemAmount.get(i));
                getterSetterViewATransaction.setCurrentDate(listItemDate.get(i));
                getterSetterViewATransaction.setCurrentTime(listItemTime.get(i));
                getterSetterViewATransaction.setTransactionType(listItemTransactionType.get(i));
                getterSetterViewATransaction.setCategory(listItemCategory.get(i));
                getterSetterViewATransaction.setCustomDescription(listItemCustomDescription.get(i));

                if(listItemTransactionType.get(i)==1){
                   selected.setTransactionType(0);
                }
                else{
                    selected.setTransactionType(1);
                }

                selected.setCategory(listItemCategory.get(i));
                selected.setTotalAmount(listItemAmount.get(i));

                openContextMenu(listOfTransactions);
                return true;
            }
        });


        viewIncomes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(incomeStatus==0) {

//                    lblSavingAccount .setText("0");
                    incomeStatus++;
                    actionView();
                }
                else{
//                    lblSavingAccount .setText("1");
                    incomeStatus--;
                    actionView();
                }
            }
        });

        viewExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expenseStatus==0) {
                    expenseStatus++;
                    actionView();
                }
                else{
                    expenseStatus--;
                    actionView();
                }
            }
        });
//        viewDeleted.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(deletedStatus==0) {
//                    deletedStatus++;
//                    actionView();
//                }
//                else{
//                    deletedStatus--;
//                    actionView();
//                }
//            }
//        });


        registerForContextMenu(listOfTransactions);



    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle(getterSetterViewATransaction.getCurrentDate() +
                "\n" + getterSetterViewATransaction.getTotalAmount() +
                "?");
        menu.add(0, v.getId(), 0, "Open");
        menu.add(0, v.getId(), 0, "Delete");
        menu.add(0, v.getId(), 0, "Cancel");

        Toast.makeText(this,""+ selectedIdOfListOfTransaction, Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals("Open")){
           getViewATransaction();
        }
        else if(item.getTitle().equals("Delete")){
            getDeleteAction();
        }
        else{

        }
        return true;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_transactions);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(this, Home.class));
            this.finish();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure to delete?");
            builder.setIcon(android.R.drawable.ic_delete);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(confirmation--==0) {
                        db.resetAllData();
                        AllNotification n =  new AllNotification();
                        n.getResetNotification(getBaseContext());
                    }

                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.out.println("CONFIRMATION: NO");

                }
            });
            AlertDialog confirmDelete = builder.create();
            confirmDelete.show();
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
            startActivity(new Intent(this,AddIncome.class));
            this.finish();

        } else if (id == R.id.addExpense) {
            startActivity(new Intent(this, AddExpenses.class));
            this.finish();
        }
        else if(id == R.id.home){
            startActivity(new Intent(this, Home.class));
            this.finish();
        }
        else if(id == R.id.viewAllTransactions){
            startActivity(new Intent(this, Transactions.class));
            this.finish();
        }
        else if(id == R.id.exit){
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_transactions);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void getaddIncome() {
        startActivity(new Intent(this, AddIncome.class));
        this.finish();
    }

    public void getactionAddExpense() {
        startActivity(new Intent(this, Transactions.class));
        this.finish();
    }

    public void getactionExit() {
        this.finish();
    }


    public void actionDeleteSelected(View v){
       getDeleteAction();
    }

    public void actionView(){
//        String sortBy = spinnerChooseSorting.getSelectedItem().toString();

//        if(sortBy.equals("INCOME ONLY")) {
//            c = db.getRecords(1);
//        }
//        else if(sortBy.equals("EXPENSE ONLY")){
//            c = db.getRecords(0);
//        }
//        else if(sortBy.equals("INCOME AND EXPENSES")){
//            c = db.getRecords(2);
//        }
//        else{
//            c = db.getRecords(10);
//        }
//        Toast.makeText(this,"income Status: "+ incomeStatus +"\nExpense Status: " + expenseStatus + "\nDeleteStatus: " + deletedStatus,Toast.LENGTH_SHORT).show();
        if(incomeStatus == 1 && expenseStatus ==1 ){
            c = db.getRecords(2);
        }
        else if(incomeStatus == 0 && expenseStatus ==1 ){
            c = db.getRecords(0);
        }
        else if(incomeStatus == 1 && expenseStatus == 0 ){
            c = db.getRecords(1);
        }


        Vector<String> listItems = new Vector<String>();

        int x = 0;
        int y=0;
        String incomeOrExpense ="";
        if(c!=null) {
            c.moveToFirst();

            while (!c.isAfterLast()) {

                listItemId.add(x, c.getInt(c.getColumnIndex("id")));
                listItemSource.add(x, c.getString(c.getColumnIndex("source")).toString());
                listItemAmount.add(x, c.getString(c.getColumnIndex("amount")).toString());
                listItemCustomDescription.add(x, c.getString(c.getColumnIndex("custom_description")).toString());
                listItemCategory.add(x, c.getString(c.getColumnIndex("category")).toString());
                listItemDate.add(x, c.getString(c.getColumnIndex("date")).toString());
                listItemTime.add(x, c.getString(c.getColumnIndex("time")).toString());
                listItemTransactionType.add(x, c.getInt(c.getColumnIndex("transaction_type")));


                c.moveToNext();
                x++;
            }
            while (x != 0) {
                if (listItemTransactionType.get(y) == 1) {
                    incomeOrExpense = "( EARNED )";
                } else {
                    incomeOrExpense = "( SPENT )";
                }
                listItems.add(y, y + ".  " + "\tDATE: " + listItemDate.get(y) + " \nAMOUNT: Rs. " + listItemAmount.get(y) + incomeOrExpense);
                x--;
                y++;
            }
            ArrayAdapter<String> listItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listItems);
            listItemsAdapter.notifyDataSetChanged();
            listOfTransactions.setAdapter(listItemsAdapter);
        }
    }

    public void getTransactions(){
        Cursor c = db.getRecords(10);

        Vector<String> listItems = new Vector<String>();

        int x = 0;
        int y=0;

        c.moveToFirst();
        while(!c.isAfterLast()){

            listItemId.add(x,c.getInt(c.getColumnIndex("id")));
            listItemSource.add(x, c.getString(c.getColumnIndex("source")).toString());
            listItemAmount.add(x, c.getString(c.getColumnIndex("amount")).toString());
            listItemCustomDescription.add(x, c.getString(c.getColumnIndex("custom_description")).toString());
            listItemCategory.add(x,c.getString(c.getColumnIndex("category")).toString());
            listItemDate.add(x, c.getString(c.getColumnIndex("date")).toString());
            listItemTime.add(x, c.getString(c.getColumnIndex("time")).toString());
            listItemTransactionType.add(x, c.getInt(c.getColumnIndex("transaction_type")));

            c.moveToNext();
            x++;
        }
        String incomeOrExpense;
        while(x!=0){
//            listItems.add(y,"id: " + listItemId.get(y) + "Source: " + listItemSource.get(y) + "  Amount: " + listItemAmount.get(y) + "\n" + "Date: "+ listItemDate.get(y) + "Time: " + listItemTime.get(y)+ "\nType: " + listItemTransactionType.get(y) + "(" + listItemCategory.get(y) + ")");
            if (listItemTransactionType.get(y) == 1) {
                incomeOrExpense = "( EARNED )";
            } else {
                incomeOrExpense = "( SPENT )";
            }

            listItems.add(y, y + ".  " + "\tDATE: " + listItemDate.get(y) + " \nAMOUNT: Rs. " + listItemAmount.get(y) + incomeOrExpense);

            x--;
            y++;
        }
        ArrayAdapter<String> listItemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,listItems);
        listItemsAdapter.notifyDataSetChanged();
        listOfTransactions.setAdapter(listItemsAdapter);

    }


//    public void actionViewATransaction(View v3){
//        if(selectedIdOfListOfTransaction!=null) {
//            Intent i = new Intent(this, ViewATransaction.class);
//
//            i.putExtra("id",String.valueOf(selectedIdOfListOfTransaction));
//            i.putExtra("transactionType",String.valueOf(getterSetterViewATransaction.getTransactionType()));
//            i.putExtra("source", getterSetterViewATransaction.getSource());
//            i.putExtra("date",getterSetterViewATransaction.getCurrentDate());
//            i.putExtra("amount",getterSetterViewATransaction.getTotalAmount());
//            i.putExtra("time",getterSetterViewATransaction.getCurrentTime());
//            i.putExtra("category",getterSetterViewATransaction.getCategory());
//            i.putExtra("customDescription", getterSetterViewATransaction.getCustomDescription());
//
//            startActivity(i);
//            this.finish();
//        }
//        else{
//            Toast.makeText(this,"Please select from a list to view.",Toast.LENGTH_SHORT).show();
//        }
//    }


////    private void clearListOfTransaction() {
////        listOfTransactions.setAdapter(null);
////        spinnerMoneySpentOn.setSelection(0);
////        txtTotalAmount.setText("");
////        txtCustomDescription.setText("");
////    }
////
    private void getViewATransaction() {
        if(selectedIdOfListOfTransaction!=null) {
            Intent i = new Intent(this, ViewATransaction.class);

            i.putExtra("id",String.valueOf(selectedIdOfListOfTransaction));
            i.putExtra("transactionType",String.valueOf(getterSetterViewATransaction.getTransactionType()));
            i.putExtra("source", getterSetterViewATransaction.getSource());
            i.putExtra("date",getterSetterViewATransaction.getCurrentDate());
            i.putExtra("amount",getterSetterViewATransaction.getTotalAmount());
            i.putExtra("time",getterSetterViewATransaction.getCurrentTime());
            i.putExtra("category",getterSetterViewATransaction.getCategory());
            i.putExtra("customDescription", getterSetterViewATransaction.getCustomDescription());

            startActivity(i);
            this.finish();
        }
        else{
            Toast.makeText(this,"Please select from a list to view.",Toast.LENGTH_SHORT).show();
        }
    }

    public void getDeleteAction(){
        if(selectedIdOfListOfTransaction!=null) {
//            clearListOfTransaction();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure to delete?");
            builder.setIcon(android.R.drawable.ic_delete);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
//                    lblSavingAccount.setText("transaction Type: "+selected.getTransactionType()+"    ");
                    db.deleteRecords(selectedIdOfListOfTransaction);
                    db.addBalance(selected);
                    selectedIdOfListOfTransaction=null;
                    getTransactions();
                    savingAccount.setText(getterSetterAddIncome.getSavings());
                    expendableAccount.setText(getterSetterAddIncome.getExpendables());
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog confirmDelete = builder.create();
            confirmDelete.show();

//            db.deleteRecords(selectedIdOfListOfTransaction);
//            selectedIdOfListOfTransaction=null;
//            getTransactions();
        }
        else{
            Toast.makeText(this,"Please select one transaction to delete.", Toast.LENGTH_LONG).show();
        }
    }

    public void getSavedSettings(){
        SharedPreferences get = getSharedPreferences("xud", Context.MODE_PRIVATE);
        getBackgroundColor = get.getString("backgroundColor", "");
        getButtonColor = get.getString("buttonColor","");
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        Float downX = null,
                downY = null,
                      upX,
                        upY;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                return false; // allow other events like Click to be processed
            case MotionEvent.ACTION_UP:
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // horizontal swipe detection
                if (Math.abs(deltaX) > 100) {
                    // left or right
                    if (deltaX < 0) {
                        Toast.makeText(this, "LEFT TO RIGHT", Toast.LENGTH_SHORT).show();
//                        Log.i(logTag, "Swipe Left to Right");
//                        mSwipeDetected = Action.LR;
                        return false;
                    }
                    if (deltaX > 0) {
                        Toast.makeText(this, "RIGHT TO LEFT", Toast.LENGTH_SHORT).show();

//                        Log.i(logTag, "Swipe Right to Left");
//                        mSwipeDetected = Action.RL;
                        return false;
                    }
                }
                return false;
        }
        return false;

    }
}
