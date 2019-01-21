package com.app.loadingbutton;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.app.loadbtnlib.LoadingButton;

public class MainActivity extends AppCompatActivity {

    private LoadingButton mLoadingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingBtn = findViewById(R.id.main_loadbtn_test);

        mLoadingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startLoading();
            }
        });

    }

    private void startLoading() {
        mLoadingBtn.startLoading(new LoadingButton.OnLoadingListener() {
            @Override
            public void onStart() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //  finishLoading();
                        mLoadingBtn.finishLoading();
                    }
                }, 1000);
            }

            @Override
            public void onFinish() {

            }
        });
    }

}
