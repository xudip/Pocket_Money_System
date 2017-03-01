package com.xudip.pocketmoney;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ViewATransaction extends AppCompatActivity
                                implements GestureDetector.OnGestureListener{

    getterSetterAddIncome viewATransaction;

    private static final int SWIPE_DISTANCE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    int id;
    String transactionType;
    String source;
    String date;
    String amount;
    String time;
    String category;
    String customDescription;

    Cursor currentCursor;
    Integer current;
    Integer next;
    Integer previous;

    TextView lblTransactionType;
    TextView lblSource;
    TextView lblDate;
    TextView lblAmount;
    TextView lblTime;
    TextView lblCategory;
    TextView lblCustomDescription;
    TextView lblIncomeExpense;

    DBHandler nextTransaction;

    private GestureDetectorCompat gestureDetector;

    @Override
    public void onBackPressed() {
            startActivity(new Intent(this, Transactions.class));
            this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_atransaction);

        this.gestureDetector = new GestureDetectorCompat(this,this);

        lblTransactionType = (TextView) findViewById(R.id.lblTransactionType);
        lblSource = (TextView) findViewById(R.id.lblSource);
        lblDate = (TextView) findViewById(R.id.lblDate);
        lblAmount =  (TextView) findViewById(R.id.lblTotalAmount);
        lblTime =  (TextView) findViewById(R.id.lblTime);
        lblCategory =  (TextView) findViewById(R.id.lblCategory);
        lblCustomDescription =  (TextView) findViewById(R.id.lblCustomDescription);
        lblIncomeExpense = (TextView) findViewById(R.id.lblIncomeExpnese);

        id = Integer.valueOf(getIntent().getExtras().getString("id"));

        nextTransaction = new DBHandler(this,null,null,1);
        currentCursor = nextTransaction.getNextTransaction(id);
//        viewATransaction = new getterSetterAddIncome();

        while (!currentCursor.isAfterLast()) {
            if (currentCursor.getInt(currentCursor.getColumnIndex("id")) == id) {
                current = currentCursor.getInt(currentCursor.getColumnIndex("id"));
                break;
            }
            currentCursor.moveToNext();
        }


        if(getIntent().getExtras().getString("transactionType").equals("1")){
            transactionType = "INCOME";
            lblIncomeExpense.setText("MONEY RECEIVED FROM: ");
        }
        else{
            lblIncomeExpense.setText("MONEY SPENT ON: ");
            transactionType = "EXPENSE";
        }
        source = getIntent().getExtras().getString("source");
        date = getIntent().getExtras().getString("date");
        amount =getIntent().getExtras().getString("amount");
        time = getIntent().getExtras().getString("time");
        category = getIntent().getExtras().getString("category");
        customDescription = getIntent().getExtras().getString("customDescription");

        lblTransactionType.setText(""+transactionType);
        lblSource.setText(source);
        lblDate.setText("DATE: " + date);
        lblAmount.setText(amount);
        lblTime.setText("TIME: " + time);
        lblCategory.setText(category);
        lblCustomDescription.setText(customDescription);

//        Toast.makeText(this,source,Toast.LENGTH_SHORT).show();

    }

    public void actionNext(View nextView){
      getNextData();
    }

    private void getNextData() {
        currentCursor.moveToNext();
        try{
            next = currentCursor.getInt(currentCursor.getColumnIndex("id"));
//            Toast.makeText(this,"Next id is: "+ next ,Toast.LENGTH_SHORT).show();
            current = currentCursor.getInt(currentCursor.getColumnIndex("id"));
            refreshData();
        }catch (android.database.CursorIndexOutOfBoundsException x){
            Toast.makeText(this, "NO NEXT ITEM!", Toast.LENGTH_SHORT).show();
        }
//        Cursor next = nextTransaction.getNextTransaction(id, "NEXT");
    }

    public void actionPrevious(View previousView){
       getPreviousData();
    }

    private void getPreviousData() {
        currentCursor.moveToPrevious();
        try{
            previous = currentCursor.getInt(currentCursor.getColumnIndex("id"));
//            Toast.makeText(this,"Previous id is: "+ previous,Toast.LENGTH_SHORT).show();
            current = currentCursor.getInt(currentCursor.getColumnIndex("id"));
            refreshData();
        }catch (android.database.CursorIndexOutOfBoundsException x1){
            Toast.makeText(this, "NO PREVIOUS ITEM!", Toast.LENGTH_SHORT).show();

        }
//        Cursor previous = nextTransaction.getNextTransaction(id,"PREVIOUS");
    }

    public void refreshData(){

        Cursor now = nextTransaction.getASpecificTransaction(current);
        if(now.getInt(now.getColumnIndex("transaction_type"))==1){
            transactionType = "INCOME";
            lblIncomeExpense.setText("MONEY RECEIVED FROM: ");
        }
        else{
            lblIncomeExpense.setText("MONEY SPENT ON: ");
            transactionType = "EXPENSE";
        }
        lblTransactionType.setText(transactionType);
        lblSource.setText(now.getString(now.getColumnIndex("source")));
        lblDate.setText("DATE: " +now.getString(now.getColumnIndex("date")));
        lblAmount.setText(now.getString(now.getColumnIndex("amount")));
        lblTime.setText("TIME: " + now.getString(now.getColumnIndex("time")));
        lblCategory.setText(now.getString(now.getColumnIndex("category")));
        lblCustomDescription.setText(now.getString(now.getColumnIndex("custom_description")));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velocityX, float velocityY) {

        float distanceX = motionEvent1.getX() - motionEvent.getX();
        float distanceY = motionEvent.getY() - motionEvent1.getY();
        if (Math.abs(distanceX) > Math.abs(distanceY) &&
                Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD &&
                Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
            if (distanceX > 0)
                onSwipeRight();
            else
                onSwipeLeft();
            return true;
        }
        return true;


    }

    private void onSwipeLeft() {
        getNextData();
//        Toast.makeText(this, "LEFT", Toast.LENGTH_SHORT).show();

    }

    private void onSwipeRight() {
        getPreviousData();
//        Toast.makeText(this, "RIGHT", Toast.LENGTH_SHORT).show();

    }
}
