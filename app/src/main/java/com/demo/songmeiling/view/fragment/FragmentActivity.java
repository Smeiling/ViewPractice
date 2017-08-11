package com.demo.songmeiling.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.demo.songmeiling.view.R;

public class FragmentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FragmentActivity", "onCreate");
        setContentView(R.layout.activity_fragment);
        getFragmentManager().beginTransaction().add(new TestFragment(), "testFragment").commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("FragmentActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("FragmentActivity", "onResume");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("FragmentActivity", "onNewIntent");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("FragmentActivity", "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("FragmentActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("FragmentActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("FragmentActivity", "onDestroy");
    }
}
