package com.xudip.pocketmoney;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

public class AddExpenses extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


//    NotificationCompat.Builder notification;

    DBHandler db;

    String getBackgroundColor;
    String getButtonColor;

    Vector<Integer> listItemId = new Vector<Integer>(10);
    Vector<String> listItemSource = new Vector<String>(10);
    Vector<String> listItemAmount = new Vector<String>(10);
    Vector<String> listItemCustomDescription = new Vector<String>(10);
    Vector<String> listItemCategory = new Vector<String>(10);
    Vector<String> listItemDate = new Vector<String>(10);
    Vector<String> listItemTime = new Vector<String>(10);
    Vector<Integer> listItemTransactionType = new Vector<Integer>(10);

    Integer selectedIdOfListOfTransaction;
    getterSetterAddIncome selected;


    ScrollView scrollViewWholeScreen;
    ScrollView scrollViewListOfTransactions;

    Spinner spinnerMoneySpentOn;
    Spinner spinnerCategory;
    EditText txtTotalAmount;
    EditText txtCustomDescription;

    Button addExpenses;
    Button deleteSelected;
    ListView listOfTransactions;

    TextView savingAccount;
    TextView expendableAccount;
    MenuResetAll reset;

    getterSetterAddIncome getterSetterViewATransaction;


    int confirmation=4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);

        selected = new getterSetterAddIncome();

        addExpenses = (Button) findViewById(R.id.btnAddExpenses);
        deleteSelected = (Button) findViewById(R.id.btnDeleteSelected);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerMoneySpentFrom);
        spinnerMoneySpentOn = (Spinner) findViewById(R.id.spinnerMoneySpentOn);
        txtTotalAmount = (EditText) findViewById(R.id.txtTotalAmount);
        txtCustomDescription = (EditText) findViewById(R.id.txtCustomDescription);
        listOfTransactions = (ListView) findViewById(R.id.listOfTransactions);

        getterSetterViewATransaction = new getterSetterAddIncome();

        getSavedSettings();
        try {
            View root = spinnerCategory.getRootView();
            root.setBackgroundColor(Color.parseColor(getBackgroundColor));
            addExpenses.setBackgroundColor(Color.parseColor(getButtonColor));
            deleteSelected.setBackgroundColor(Color.parseColor(getButtonColor));
         }catch(java.lang.StringIndexOutOfBoundsException e){
//        Toast.makeText(this, "Error in loading settings.\n Restoring Default Colors", Toast.LENGTH_SHORT).show();
    }

        DBHandler remaining = new DBHandler(this, null, null , 1);
        remaining.getBalanceDirect();
        savingAccount = (TextView) findViewById(R.id.SavingAccount);
        expendableAccount = (TextView) findViewById(R.id.ExpendableAccount);
        savingAccount.setText(getterSetterAddIncome.getSavings());
        expendableAccount.setText(getterSetterAddIncome.getExpendables());

        String moneySpentOnItems[] = { "GENERAL", "FOOD","TRANSPORTATION", "LOAN TAKEN", "SHOPPING"};
        ArrayAdapter<String> moneySpentOnAdaptor = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,moneySpentOnItems);
        spinnerMoneySpentOn.setAdapter(moneySpentOnAdaptor);

        String categoryItems[] = {"Exapendable Account", "Saving Account" };
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, categoryItems);
        spinnerCategory.setAdapter(categoryAdapter);

        db = new DBHandler(this, null, null , 1);
        clearListOfTransaction();
        getTransactions();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_add_expenses_layout);
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

        txtCustomDescription.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event)
            {
//                Log.v(TAG,”CHILD TOUCH”);
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        listOfTransactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        listOfTransactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                selectedIdOfListOfTransaction = listItemId.get(i);
                selected.setTransactionType(1);
                selected.setCategory(listItemCategory.get(i));
                selected.setTotalAmount(listItemAmount.get(i));

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

        registerForContextMenu(listOfTransactions);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_add_expenses_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(this, Home.class));        }
            this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        reset = new MenuResetAll();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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
            this.finish();
            startActivity(new Intent(this, Transactions.class));
        }
        else if(id == R.id.viewAllTransactions){
            startActivity(new Intent(this, Transactions.class));
            this.finish();
        }
        else if(id == R.id.exit){
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_add_expenses_layout);
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

    public void actionAddExpenses(View v){
        String source =  spinnerMoneySpentOn.getSelectedItem().toString().trim();
        String amount =   txtTotalAmount.getText().toString().trim();
        String category =   spinnerCategory.getSelectedItem().toString().trim();
        String custom_description = txtCustomDescription.getText().toString().trim();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = timeFormat.format(c.getTime()).toString();
        String currentDate = dateFormat.format(c.getTime()).toString();
        int transactionType = 0;

        getterSetterAddIncome getterSetterExpense = new getterSetterAddIncome();

        getterSetterExpense.setSource(source);
        getterSetterExpense.setTotalAmount(amount);
        getterSetterExpense.setCategory(category);
        getterSetterExpense.setCustomDescription(custom_description);
        getterSetterExpense.setCurrentDate(currentDate);
        getterSetterExpense.setCurrentTime(currentTime);
        getterSetterExpense.setTransactionType(transactionType);

        if(getterSetterExpense.getSource().equals("") || getterSetterExpense.getTotalAmount().equals("") || getterSetterExpense.getCategory().equals("")) {
            Toast.makeText(this,"Insert Not Successfull!\n Try Again. Thank You!", Toast.LENGTH_SHORT).show();
        }
        else{
            db.addRecords(getterSetterExpense);
            db.addBalance(getterSetterExpense);
            clearListOfTransaction();
            getTransactions();

            Toast.makeText(this,"Insert Successfull!  " + getterSetterExpense.getSavings() + getterSetterExpense.getExpendables() , Toast.LENGTH_SHORT).show();
        }
//        Toast.makeText(this,
//                "Source Of Inocme:  " + source + "\n Total Amount:  " + amount + "\n Selected Category:  " + category,
//                Toast.LENGTH_SHORT).show();
        savingAccount.setText(getterSetterAddIncome.getSavings());
        expendableAccount.setText(getterSetterAddIncome.getExpendables());
    }

    public void actionDeleteSelected(View v){

        if(selectedIdOfListOfTransaction!=null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure to delete?");
            builder.setIcon(android.R.drawable.ic_delete);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    clearListOfTransaction();
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
        }
        else{
            Toast.makeText(this,"Please select one transaction to delete.", Toast.LENGTH_LONG).show();
        }
        savingAccount.setText(getterSetterAddIncome.getSavings());
        expendableAccount.setText(getterSetterAddIncome.getExpendables());
    }

    public void getTransactions(){
        Cursor c = db.getRecords(0);


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
        while(x!=0){
            listItems.add(y,y+".  " + "SPENT ON: " + listItemSource.get(y) + "\nDATE: " + listItemDate.get(y) + " \nAMOUNT: Rs. " + listItemAmount.get(y) + " ( SPENT ) " );
            x--;
            y++;
        }
        ArrayAdapter<String> listItemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,listItems);
        listItemsAdapter.notifyDataSetChanged();
        listOfTransactions.setAdapter(listItemsAdapter);
        db.close();
    }


    private void clearListOfTransaction() {
        listOfTransactions.setAdapter(null);
        spinnerMoneySpentOn.setSelection(0);
        txtTotalAmount.setText("");
        txtCustomDescription.setText("");
    }

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

    private void getDeleteAction(){
        if(selectedIdOfListOfTransaction!=null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure to delete?");
            builder.setIcon(android.R.drawable.ic_delete);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
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

}
