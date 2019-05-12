package com.example.kig.billiardsguide;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ball_select extends Dialog {
    private OnDismissListener _listener ;
    private ImageButton yb,wb;
    private EditText tv;

    public ball_select(Context context) {
        super(context);
    }

    @Override
    public void onCreate( Bundle $savedInstanceState ) {
        super.onCreate( $savedInstanceState ) ;
        setContentView( R.layout.ball_select ) ;

        yb=(ImageButton)findViewById(R.id.yb);
        wb=(ImageButton)findViewById(R.id.wb);
        tv=(EditText) findViewById(R.id.text1);

        yb.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("1");
                if( _listener == null ) {} else {
                    _listener.onDismiss( ball_select.this ) ;
                }
                dismiss() ;
            }
        }) ;

        wb.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("2");
                if( _listener == null ) {} else {
                    _listener.onDismiss( ball_select.this ) ;
                }
                dismiss() ;
            }
        }) ;
    }

    public void setOnDismissListener( OnDismissListener $listener ) {
        _listener = $listener ;
    }

    public String getNum() {
        return tv.getText().toString();
    }
}
