package com.neo.signview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.neo.libray.SignView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SignView signView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        signView= (SignView) findViewById(R.id.daily_sv);
        button= (Button) findViewById(R.id.next_btn);
        button.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next_btn:
                signView.MoveToNext();
                break;
            default:
                break;
        }
    }
}
