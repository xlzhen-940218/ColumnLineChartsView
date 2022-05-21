package com.xlzhen.columnlinechartsview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.column_line_charts_view).post(() -> ((ColumnLineChartsView)findViewById(R.id.column_line_charts_view)).startAnimator());
    }
}