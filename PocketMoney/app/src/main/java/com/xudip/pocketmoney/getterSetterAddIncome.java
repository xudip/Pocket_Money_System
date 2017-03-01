package com.xudip.pocketmoney;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Sugaste on 1/23/2017.
 */

public class getterSetterAddIncome {


    private String backgroundColor=null;
    private String buttonColor=null;
    private String source=null;
    private String totalAmount = null;
    private String category = null;
    private String customDescription = null;
    private String currentDate = null;
    private String currentTime = null;
    private int transactionType;
    public static String savings = "0";
    public static String expendables = "0";



    // lists of setters
    public void setSource(String source){
        this.source= source;

    }

    public void setTotalAmount(String totalAmount){
        this.totalAmount = totalAmount;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public void setCustomDescription(String customDescription){
        this.customDescription = customDescription;
    }


//    list of getters
    public String getSource(){
        return(source);
    }

    public String getTotalAmount(){
        return (totalAmount);
    }

    public String getCategory(){
        return(category);
    }

    public String getCustomDescription(){
        return(customDescription);
    }


    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public static String getSavings() {
        return savings;
    }

    public static void setSavings(String savings11) {
        savings = savings11;
    }

    public static String getExpendables() {
        return expendables;
    }

    public static void setExpendables(String expendable) {
        expendables = expendable;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getButtonColor() {
        return buttonColor;
    }

    public void setButtonColor(String buttonColor) {
        this.buttonColor = buttonColor;
    }

    public static void getUnderConstruction(Dialog d){
        d.setContentView(R.layout.under_construction);
        d.setTitle("UNDER CONSTRUCTION :D");
        d.show();
    }



}
