package com.xudip.pocketmoney;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.*;
import android.provider.Settings;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by Sugaste on 2/15/2017.
 */


public class CustomAdapterForColor extends ArrayAdapter<String> {
    private String TAG = ListOfColors.class.getSimpleName();
    float initialX = 0, finalX=0, initialY;
    private static final int SWIPE_DISTANCE_THRESHOLD = 150;


    public CustomAdapterForColor(Context context, String[] colors) {
        super(context, R.layout.custom_row_for_color, colors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater colorsInflator = LayoutInflater.from(getContext());
        View customView = colorsInflator.inflate(R.layout.custom_row_for_color, parent, false);

        RelativeLayout layoutCustomRow = (RelativeLayout) customView.findViewById(R.id.layoutCustomRow);
//        this.gestureDetector = new GestureDetectorCompat(this,this);

        final String colors = getItem(position);
        final Button lblColor = (Button) customView.findViewById(R.id.lblColor);
        lblColor.setText(colors);
        layoutCustomRow.setBackgroundColor(Color.GRAY);

        lblColor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int action = event.getActionMasked();

                switch (action) {

                    case MotionEvent.ACTION_DOWN:
                        initialX = event.getX();
                        break;

                    case MotionEvent.ACTION_MOVE:
//                        Log.d(TAG, "Action was MOVE");
                         finalX = event.getX();

//                        Log.d(TAG, "Action was UP");
                        if ((initialX < finalX) && ((finalX-initialX)>=SWIPE_DISTANCE_THRESHOLD)) {
                            Log.d(TAG, "Left to Right swipe performed");
//                            Toast.makeText(ListOfColors.this, "asdf", Toast.LENGTH_SHORT).show();
                            lblColor.setText("DELETED");
                        }

                        if ((initialX > finalX) && ((initialX-finalX)>=SWIPE_DISTANCE_THRESHOLD)) {
                            Log.d(TAG, "Right to Left swipe performed");
                            lblColor.setText("UNDO DELEETE");
                        }

                        break;
                }

                return true;
            }
        });


//        final Button btnColor = (Button) customView.findViewById(R.id.btnColor);
//        btnColor.setText(colors);
//        btnColor.setBackgroundColor(Color.parseColor(colors));
//
//
////        ImageView imageColor = (ImageView) customView.findViewById(R.id.colorImage );
////        TextView lblColorName = (TextView) customView.findViewById(R.id.lblColors );
//
//        btnColor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("COLOR TEST: "+ btnColor.getText().toString());
//                ListOfColors.selectedBackgroundColor = btnColor.getText().toString();
//                btnColor.setText("CLICKED :D :D");
//            }
//        });
//

//        if(colors.equals("GREEN")){
//                btnColor.setBackgroundColor(Color.FromName(color));
//        }
//        else if(colors.equals("BLUE")){
//            btnColor.setBackgroundColor(Color.BLUE);
//        }
//        else if(colors.equals("GRAY")){
//            btnColor.setBackgroundColor(Color.GRAY);
//        }
//        else if(colors.equals("YELLOW")){
//            btnColor.setBackgroundColor(Color.YELLOW);
//        }
//        else if(colors.equals("RED")){
//            btnColor.setBackgroundColor(Color.RED);
//        }
//        else{
//            btnColor.setBackgroundColor(0);
//        }


        return(customView);
    }



}
