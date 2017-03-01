package com.xudip.pocketmoney;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Sugaste on 2/11/2017.
 */

public class MenuResetAll {

    public static int confirmation=0;

    public void resetAll(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure to delete?");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(this,"yes", Toast.LENGTH_SHORT).show();`
                System.out.println("CONFIRMATION: YES");
               confirmation =1;

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.out.println("CONFIRMATION: NO");

                confirmation =0;
            }
        });
        AlertDialog confirmDelete = builder.create();
        confirmDelete.show();
//        return (confirmation);
    }
}
