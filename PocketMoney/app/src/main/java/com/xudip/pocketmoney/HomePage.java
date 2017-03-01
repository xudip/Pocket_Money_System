package com.xudip.pocketmoney;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home);

        Button btnAddIncome = (Button) findViewById(R.id.btnAddIncome);
        Button btnAddExpense = (Button) findViewById(R.id.btnAddExpense);
        Button btnExit = (Button) findViewById(R.id.btnExit);

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


    }
    public void getaddIncome() {
        this.finish();
        startActivity(new Intent(this, AddIncome.class));
    }

    public void getactionAddExpense() {
        this.finish();
        startActivity(new Intent(this, AddExpenses.class));
    }

    public void getactionExit() {
        this.finish();
    }
}