package com.xudip.pocketmoney;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Sugaste on 1/22/2017.
 */
// INCOME = 0 EXPENSE =1
//    DELETED = 1 NOT DELETED = 0
// TRANSACTION TYPE = 1 FOR INCOME
//    TRANSACTION TYPE =0 FOR EXPENSE

public class DBHandler extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "pocket_money_system.db";

    public static final String TABLE_RECORDS = "table_records";
    public static final String COLUMN_ID= "id";
    public static final String COLUMN_SOURCE = "source";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_CUSTOM_DESCRIPTION = "custom_description";
    public static final String COLUMN_CATEGORY= "category";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_TRANSACTION_TYPE = "transaction_type";

    public static final String COLUMN_DELETE_STATUS = "delete_status";

    public static final String TABLE_BALANCE = "table_balance";
    public static final String COLUMN_BID = "id";
    public static final String COLUMN_SAVINGS = "savings";
    public static final String COLUMN_EXPENDABLES = "expendables";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }



//    SQLiteDatabase db;

    @Override
    public void onCreate(SQLiteDatabase dbOncreate) {
        String query_create_table_records = " CREATE TABLE " + TABLE_RECORDS + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SOURCE + " TEXT, " +
                COLUMN_AMOUNT + " TEXT, " +
                COLUMN_CUSTOM_DESCRIPTION + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_TRANSACTION_TYPE + " TEXT, " +
                COLUMN_DELETE_STATUS + " TEXT " +
                ");";
        dbOncreate.execSQL(query_create_table_records);

        String query_create_table_balance = " CREATE TABLE " + TABLE_BALANCE + " (" +
                COLUMN_BID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SAVINGS+ " TEXT, " +
                COLUMN_EXPENDABLES+ " TEXT " +
                ");";
        dbOncreate.execSQL(query_create_table_balance);
        ContentValues r = new ContentValues();
        r.put(COLUMN_SAVINGS,"0");
        r.put(COLUMN_EXPENDABLES,"0");
//        dbOncreate = getWritableDatabase();
        dbOncreate.insert(TABLE_BALANCE, null , r);
//        dbOncreate.close();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_RECORDS);
        onCreate(db);
    }

//    public void addBalance(String accountType, int transactionType, String amount){
    public void addBalance(getterSetterAddIncome gettersetter){
        SQLiteDatabase dbAddBalance = getWritableDatabase();
        String query_select = " SELECT savings,expendables FROM " + TABLE_BALANCE + " WHERE 1";
        Cursor c = dbAddBalance.rawQuery(query_select,null);
        c.moveToFirst();
        Integer currentAmount = Integer.valueOf(gettersetter.getTotalAmount());


          if(c!=null) {
              //initial vale 0 0 for both;
              Integer savings = Integer.valueOf(c.getString(c.getColumnIndex("savings")));
              Integer expendables = Integer.valueOf(c.getString(c.getColumnIndex("expendables")));

                System.out.println("account savings = "+savings);
                System.out.println("account expendables = "+expendables);

              if (gettersetter.getTransactionType() == 0) {
                  if (gettersetter.getCategory().equals("Saving Account")) {
                      System.out.println("saving account berfore subtraction is " + savings);
                      savings = savings - currentAmount;
                        System.out.println("saving account after subtraction is " + savings);
                  } else if (gettersetter.getCategory().equals("Exapendable Account")) {
                      System.out.println("expendable account berfore subtraction is " + expendables);
                      expendables = expendables - currentAmount;
                      System.out.println("expendable account after subtraction is "+expendables);
                  }

              } else if (gettersetter.getTransactionType() == 1) {
                  if (gettersetter.getCategory().equals("Saving Account")) {
                      System.out.println("saving account before addition is " + savings);
                      savings = savings + currentAmount;
                      System.out.println("saving account after adding is " +savings);
                  } else if (gettersetter.getCategory().equals("Exapendable Account")) {
                      System.out.println("expendable account before addition is " + expendables);
                      expendables = expendables + currentAmount;
                      System.out.println("expendable account after adding is " +expendables);
                  }
              }
              String newSavings = String.valueOf(savings);
              String newExpendables = String.valueOf(expendables);
              gettersetter.setSavings(newSavings);
              gettersetter.setExpendables(newExpendables);

              ContentValues contentValues= new ContentValues();
              contentValues.put(COLUMN_SAVINGS,newSavings);
              contentValues.put(COLUMN_EXPENDABLES,newExpendables);
              dbAddBalance.update(TABLE_BALANCE, contentValues,"id="+1,null);

//              dbAddBalance.rawQuery("UPDATE "+ TABLE_BALANCE + " SET "+ COLUMN_SAVINGS + " = '"+newSavings+"' ,"+COLUMN_EXPENDABLES+"= '"+newExpendables+ "' WHERE " + COLUMN_ID + " = "+1, null);
//              db.rawQuery("UPDATE "+ TABLE_BALANCE + " SET "+ COLUMN_EXPENDABLES+"= '"+expendables.toString()+ "' WHERE " + COLUMN_ID + " = "+1, null);
//              String query_update ="UPDATE " + TABLE_BALANCE + " SET savings = " + savings.toString() + ", expendables =" + expendables.toString() + ") WHERE id=" + 1;
//              db.execSQL(query_update);
          }
        dbAddBalance.close();
//        return(c);


    }
// add row to table in database
    public void addRecords(getterSetterAddIncome getterSetter){
        SQLiteDatabase dbAddRecords = getWritableDatabase();

        ContentValues records = new ContentValues();
        records.put(COLUMN_SOURCE, getterSetter.getSource());
        records.put(COLUMN_AMOUNT, getterSetter.getTotalAmount());
        records.put(COLUMN_CUSTOM_DESCRIPTION, getterSetter.getCustomDescription());
        records.put(COLUMN_CATEGORY, getterSetter.getCategory());
        records.put(COLUMN_DATE, getterSetter.getCurrentDate());
        records.put(COLUMN_TIME, getterSetter.getCurrentTime());
        records.put(COLUMN_TRANSACTION_TYPE, getterSetter.getTransactionType());
        records.put(COLUMN_DELETE_STATUS,0);

        dbAddRecords.insert(TABLE_RECORDS, null , records);
        dbAddRecords.close();
//        addBalance(getterSetter.getCategory(), getterSetter.getTransactionType(), getterSetter.getTotalAmount());

    }
//select all rows from table in database

    public Cursor getRecords(int z){
//        String query_select = " SELECT * FROM " + TABLE_RECORDS + " WHERE " + COLUMN_TRANSACTION_TYPE + "=\"" +type+ " \"; ";
//        String query_select = " SELECT * FROM " + TABLE_RECORDS + " WHERE 1";
//        dbAddRecords = getWritableDatabase();
        SQLiteDatabase dbAddRecords = getWritableDatabase();
/*      for select query to match the checkbox

        income  expense   delete    status
     1      0        0       0      empty
     2      0        0       1      deleted only
     3      0        1       0      expense only
     4      0        1       1      expense and deleted only
     5      1        0       0      income only
     6      1        0       1      income and deleted only
     7      1        1       0      income and expense not deleted only
     8      1        1       1      income, expense and deleted all

     here 0=8
     and 1=9
 */
        String query_select;
//        if(z==888){
//            query_select = "SELECT * FROM " + TABLE_RECORDS + " WHERE (delete_status =" + 4 + ")";
//        }
//        if(z==889){
//            query_select = " SELECT * FROM " + TABLE_RECORDS + " WHERE delete_status =" + 1;
//        }
//        if(z==898){
//            query_select = " SELECT * FROM " + TABLE_RECORDS + " WHERE (delete_status =" + 0 + ") AND (transaction_type =" + 0 + ")";
//        }
//        if(z==899){
//            query_select = " SELECT * FROM " + TABLE_RECORDS + " WHERE (delete_status =" + 1 + ") AND (transaction_type =" + 0 + ")";
//        }
//        if(z==988){
//            query_select = " SELECT * FROM " + TABLE_RECORDS + " WHERE (delete_status =" + 0 + ") AND (transaction_type =" + 1 + ")";
//        }
//        if(z==989){
//            query_select = " SELECT * FROM " + TABLE_RECORDS + " WHERE (delete_status =" + 0 + ") AND (transaction_type =" + 1 + ")";
//
//        }
//        if(z==998){
//            query_select = " SELECT * FROM " + TABLE_RECORDS + " WHERE delete_status =" + 0;
//
//        }
//        if(z==999 ){
//
//        }


//        if(z==000){
//            query_select = " SELECT * FROM " + TABLE_RECORDS + " WHERE 1";
//
//        }
//

        if(z==1 || z==0) {
            query_select = " SELECT * FROM " + TABLE_RECORDS + " WHERE (delete_status =" + 0 + ") AND (transaction_type =" + z + ")";
        }
        else{
            query_select = " SELECT * FROM " + TABLE_RECORDS + " WHERE delete_status =" + 0;
        }
//        else {
//            query_select = " SELECT * FROM " + TABLE_RECORDS + " WHERE 1";
//        }
        Cursor c = dbAddRecords.rawQuery(query_select, null);
        c.moveToFirst();
        dbAddRecords.close();
        return (c);
//        String source[] = new String[100], amount[] = new String[100], customDescription[]= new String[100], transactionType[]= new String[100];
//        int x = 0;
//        while(!c.isAfterLast()){
//            if(c.getString(c.getColumnIndex("id"))!= null ){
//                source[x] = c.getString(c.getColumnIndex("source"));
//                amount[x] = c.getString(c.getColumnIndex("amount"));
//                customDescription[x]= c.getString(c.getColumnIndex("custom_description"));
//                transactionType[x] = c.getString(c.getColumnIndex("transaction_type"));
//                x++;
//
//            }
////        }
//        if(c.equals(null)){
//            return(c.);
//        }
    }

// delete rows from table in database
    public void deleteRecords(int idToDelete){
        SQLiteDatabase dbDelete = getWritableDatabase();
//        db.execSQL(" DELETE FROM  " + TABLE_RECORDS + " WHERE " + COLUMN_ID + " =\" " + "1" + " \"; ");
//        String query_delete ="DELETE  FROM " + TABLE_RECORDS + " WHERE id =" + idToDelete  ;
//        ContentValues del = new ContentValues();
//        del.put(COLUMN_DELETE_STATUS,"1");
//        db.update(TABLE_RECORDS, del,"where id =" + idToDelete, null);
        String query_delete ="UPDATE " + TABLE_RECORDS + " SET delete_status = '1' WHERE id=" + idToDelete;
        dbDelete.execSQL(query_delete);
        dbDelete.close();
    }


    public void getBalanceDirect(){
        SQLiteDatabase dbAddBalance = getWritableDatabase();
        String query_select1 = " SELECT savings,expendables FROM " + TABLE_BALANCE + " WHERE 1";
        Cursor c = dbAddBalance.rawQuery(query_select1,null);
        c.moveToFirst();

        getterSetterAddIncome.setExpendables(c.getString(c.getColumnIndex("expendables")));
        getterSetterAddIncome.setSavings(c.getString(c.getColumnIndex("savings")));

    }
////  for table_income.
//    private static final String TABLE_INCOME = "table_income";
//    public static final String INCOME_ID = "income_id";
//    public static final String INCOME_SOURCE = "income_source";
//    public static final String INCOME_AMOUNT = "income_amount";
//
////    for table_expense.
//    private static final String TABLE_EXPENSE = "table_expense";
//    public static final String EXPENSE_ID = "expense_id";
//    public static final String EXPENSE_ON = "expense_on";
//    public static final String EXPENSE_AMOUNT="expense_amount";
//    public static final String EXPENSE_CUSTOM_DESCRIPTION = "expense_custom_description";
//
////    for table_savings.
//    private static final String TABLE_SAVINGS = "table_savings";
//    public static final String SAVINGS_ID = "savings_id";
//    public static final String SAVINGS_SOURCE = "savings_source";
//    public static final String SAVINGS_AMOUNT = "savings_amount";


//    for next transaction in view a transaction
    public Cursor getNextTransaction(int id){
        SQLiteDatabase dbNext = getWritableDatabase();
        Cursor c=null;
        String sqlSelect = "SELECT * FROM " + TABLE_RECORDS + " WHERE (delete_status=" + 0 +")" ;
        c = dbNext.rawQuery(sqlSelect,null);
        c.moveToFirst();

        return c;
    }

    public Cursor getASpecificTransaction(int id){
        SQLiteDatabase dbCurrent = getWritableDatabase();
        Cursor c=null;
        String sqlSelect = "SELECT * FROM " + TABLE_RECORDS + " WHERE (id=" + id +")" ;
        c = dbCurrent.rawQuery(sqlSelect,null);
        c.moveToFirst();
        return c;
    }

    public void resetAllData(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_RECORDS);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_BALANCE);
        onCreate(db);

    }

}
