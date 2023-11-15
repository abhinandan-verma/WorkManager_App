package com.example.workmanagerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_COUNTER_VALUE = "key_count";
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.button);

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();
        //Data Creation
        Data data = new Data.Builder()
                .putInt(KEY_COUNTER_VALUE,500).build();

        //Making the use of Worker

        WorkRequest countWorkRequest = new OneTimeWorkRequest
                .Builder(DemoWorker.class)
               // .setConstraints(constraints)
                .setInputData(data)
                .build();

        btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkManager
                    .getInstance(getApplicationContext())
                    .enqueue(countWorkRequest);
        }
    });

    //displaying the Status of Worker
        WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(countWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null){
                            Toast.makeText(getApplicationContext(), "Status: "+workInfo.getState().name(),
                                    Toast.LENGTH_SHORT).show();

                            if(workInfo.getState().isFinished()){
                                Data data1 = workInfo.getOutputData();
                                String msg = data1.getString(DemoWorker.KEY_WORKER);
                                Toast.makeText(getApplicationContext(), ""+msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}