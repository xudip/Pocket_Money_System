package com.xudip.pocketmoney;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
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

public class AddIncome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Integer testing = 0;

    Vector<Integer> listItemId = new Vector<Integer>(10);

    String getBackgroundColor;
    String getButtonColor;

    DBHandler db;
    ScrollView scrollViewWholeScreen;
    ScrollView scrollViewListOfTransactions;
//    ListView listOfTransactions; = (ListView) findViewById(R.id.listOfTransactions);
    ListView listOfTransactions;
    EditText sourceOfIncome;
    EditText totalAmount;
    EditText customDescription;
    Button addIncome;
    Button deleteSelected;

    Spinner spinnerChooseACategory;
    Spinner spinnerCategory;
    Integer selectedIdOfListOfTransaction;
    getterSetterAddIncome selected;

    int toggleSelector = 0;
    int confirmation=4;

    TextView savingAccount;
    TextView expendableAccount;

    Vector<String> listItemSource = new Vector<String>(10);
    Vector<String> listItemAmount = new Vector<String>(10);
    Vector<String> listItemCustomDescription = new Vector<String>(10);
    Vector<String> listItemCategory = new Vector<String>(10);
    Vector<String> listItemDate = new Vector<String>(10);
    Vector<String> listItemTime = new Vector<String>(10);
    Vector<Integer> listItemTransactionType = new Vector<Integer>(10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        final ScrollView scrollViewwholeScreen = (ScrollView) findViewById(R.id.scollViewWholeScreen);
        sourceOfIncome = (EditText) findViewById(R.id.txtSourceOfIncome);
        totalAmount = (EditText) findViewById(R.id.txtTotalAmount);
        spinnerChooseACategory = (Spinner) findViewById(R.id.spinnerChooseACategory);
        customDescription = (EditText) findViewById(R.id.txtCustomDescription);
        listOfTransactions = (ListView) findViewById(R.id.listOfTransactions);
        addIncome = (Button) findViewById(R.id.btnAddIncome);
        deleteSelected = (Button) findViewById(R.id.btnDeleteSelected);

        getSavedSettings();
        try{
            View root = listOfTransactions.getRootView();
            root.setBackgroundColor(Color.parseColor(getBackgroundColor));
            addIncome.setBackgroundColor(Color.parseColor(getButtonColor));
            deleteSelected.setBackgroundColor(Color.parseColor(getButtonColor));
        }catch(java.lang.StringIndexOutOfBoundsException e){
//            Toast.makeText(this, "Error in loading settings.\n Restoring Default Colors", Toast.LENGTH_SHORT).show();
        }
        selected = new getterSetterAddIncome();

        DBHandler remaining = new DBHandler(this, null, null , 1);
        remaining.getBalanceDirect();
        savingAccount = (TextView) findViewById(R.id.SavingAccount);
        expendableAccount = (TextView) findViewById(R.id.ExpendableAccount);
        savingAccount.setText(getterSetterAddIncome.getSavings());
        expendableAccount.setText(getterSetterAddIncome.getExpendables());

        String categoryItems[] = {"Exapendable Account", "Saving Account" };
        ArrayAdapter <String> categoryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, categoryItems);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerChooseACategory);
        spinnerCategory.setAdapter(categoryAdapter);

//        for getting transactions list
        db = new DBHandler(this, null, null , 1);
        clearListOfTransaction();
        getTransactions();

//      for navigation bar
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_add_income_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        scrollViewwholeScreen.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
//                Log.v(sud,”PARENT TOUCH”);
                findViewById(R.id.listOfTransactions).getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        listOfTransactions.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event)
            {
//                Log.v(TAG,”CHILD TOUCH”);
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        customDescription.setOnTouchListener(new View.OnTouchListener() {

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
//                int displayId = (int) listOfTransactions.getItemIdAtPosition((int) listOfTransactions.getSelectedItemId());
//                Toast.makeText(this, displayId, Toast.LENGTH_SHORT).show();
//                int displayId = listOfTransactions.getItemIdAtPosition(i);
                //to show soft keyboard
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//                view.requestFocus();
//to hide it, call the method again
//                imm.hideSoftInputFromWindow(EditTextName.getWindowToken(), 0);
//                imm.toggleSoftInput(1, 0);
//                imm.hideSoftInputFromInputMethod((IBinder) listOfTransactions,0);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                selectedIdOfListOfTransaction = listItemId.get(i);
                selected.setTransactionType(0);
                selected.setCategory(listItemCategory.get(i));
                selected.setTotalAmount(listItemAmount.get(i));

//
//                if(toggleSelector == 1) {
//                    selectedIdOfListOfTransaction = listItemId.get(i);
//                    toggleSelector--;
//                    sourceOfIncome.setText(""+toggleSelector);
//                }
//                else{
//                    listOfTransactions.setSelection(-1);
//                    selectedIdOfListOfTransaction = null;
//                    toggleSelector++;
//                    sourceOfIncome.setText(""+toggleSelector);
//
//
//                }

//                sourceOfIncome.setText( "" + i + "  dbId: " + listItemId.get(i));

            }
        });
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_add_income_layout);
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
            this.finish();
            startActivity(new Intent(this,AddIncome.class));

        } else if (id == R.id.addExpense) {
            this.finish();
            startActivity(new Intent(this, AddExpenses.class));
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
        }
        this.finish();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_add_income_layout);
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


    public void actionAddIncome(View v){
//        EditText sourceOfIncome = (EditText) findViewById(R.id.txtSourceOfIncome);
//        EditText totalAmount = (EditText) findViewById(R.id.txtTotalAmount);
//        EditText customDescription = (EditText) findViewById(R.id.txtCustomDescription);
//        Spinner spinnerChooseACategory = (Spinner) findViewById(R.id.spinnerChooseACategory);


        String source =  sourceOfIncome.getText().toString().trim();
        String amount =   totalAmount.getText().toString().trim();
        String category =   spinnerChooseACategory.getSelectedItem().toString().trim();
        String custom_description = customDescription.getText().toString().trim();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = timeFormat.format(c.getTime()).toString();
        String currentDate = dateFormat.format(c.getTime()).toString();
        int transactionType = 1;

        getterSetterAddIncome getterSetter = new getterSetterAddIncome();

        getterSetter.setSource(source);
        getterSetter.setTotalAmount(amount);
        getterSetter.setCategory(category);
        getterSetter.setCustomDescription(custom_description);
        getterSetter.setCurrentDate(currentDate);
        getterSetter.setCurrentTime(currentTime);
        getterSetter.setTransactionType(transactionType);

//        Toast.makeText(this, getterSetter.getSource() +
//            getterSetter.getTotalAmount()+
//            getterSetter.getCategory()+
//            getterSetter.getCustomDescription()+
//            getterSetter.getCurrentDate()+
//            getterSetter.getCurrentTime()+
//            getterSetter.getTransactionType(),Toast.LENGTH_SHORT).show();


//            Toast.makeText(this, getterSetter.toString(),Toast.LENGTH_SHORT).show();
        if(getterSetter.getSource().equals("") || getterSetter.getTotalAmount().equals("") || getterSetter.getCategory().equals("")) {
            Toast.makeText(this,"Insert Not Successfull!\n Try Again. Thank You!", Toast.LENGTH_SHORT).show();
        }
        else{
            db.addRecords(getterSetter);
            db.addBalance(getterSetter);
            clearListOfTransaction();
            getTransactions();
            savingAccount.setText(getterSetterAddIncome.getSavings());
            expendableAccount.setText(getterSetterAddIncome.getExpendables());
            Toast.makeText(this,"Insert Successfull!" , Toast.LENGTH_SHORT).show();
        }
//        Toast.makeText(this,
//                "Source Of Inocme:  " + source + "\n Total Amount:  " + amount + "\n Selected Category:  " + category,
//                Toast.LENGTH_SHORT).show();

    }
    public void actionDeleteRecords(View v12){
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
    }
    public void getTransactions(){
            Cursor c = db.getRecords(1);
//        Toast.makeText(this,"Testing:" + c.getInt(c.getColumnIndex("id")),Toast.LENGTH_SHORT).show();
//        String listItemSource[] = new String[100], listItemAmount[] = new String[100], listItemCustomDescription[]= new String[100], listItemTransactionType[]= new String[100];
//        Vector<Integer> listItemId = new Vector<Integer>(10);
//        Vector<String> listItemSource = new Vector<String>(10);
//        Vector<String> listItemAmount = new Vector<String>(10);
//        Vector<String> listItemCustomDescription = new Vector<String>(10);
//        Vector<String> listItemTransactionType = new Vector<String>(10);
//        Vector<String> listItemDate = new Vector<String>(10);
//        Vector<String> listItemTime = new Vector<String>(10);
if(c!=null) {
    Vector<String> listItems = new Vector<String>();

    int x = 0;
    int y = 0;

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

//                listItems[x] = (listItemSource[x] + "   " +
//                        listItemAmount[x] + "   " +
//                        listItemCustomDescription[x] + "   " +
//                        listItemTransactionType[x]
//                );
        c.moveToNext();
        x++;
    }
//        Toast.makeText(this, "Adapter created", Toast.LENGTH_SHORT).show();
//        ArrayAdapter<Cursor> listItems = new ArrayAdapter<Cursor>(this, android.R.layout.simple_expandable_list_item_1, c.toString());
//        listOfTransactions.setAdapter(listItems);
    while (x != 0) {
        listItems.add(y,y+".  " + "SOURCE OF INCOME: " + listItemSource.get(y) + "\nDATE: " + listItemDate.get(y) + " \nAMOUNT: Rs. " + listItemAmount.get(y) + " ( EARNED ) " );
        x--;
        y++;
    }
    ArrayAdapter<String> listItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listItems);
    listItemsAdapter.notifyDataSetChanged();
    listOfTransactions.setAdapter(listItemsAdapter);
//        listOfTransactions.refreshDrawableState();
}
    }

    private void clearListOfTransaction() {
        listOfTransactions.setAdapter(null);
        sourceOfIncome.setText("");
        totalAmount.setText("");
        customDescription.setText("");

    }

    public void getSavedSettings(){
        SharedPreferences get = getSharedPreferences("xud", Context.MODE_PRIVATE);
        getBackgroundColor = get.getString("backgroundColor", "");
        getButtonColor = get.getString("buttonColor","");
    }


}




