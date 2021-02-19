/*
Honours Project - PC part Builder
File: CreateComputerListActivity Class
Author: Conner McGill - B00320975
Date: 2021/02/18

Summary of file:

    Update this later once I have the list sort of working right

 */

package com.example.honoursproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateComputerListActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_computer_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Create List");

        final Button selectCPU = findViewById(R.id.addCPUButton);
        selectCPU.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addCPUButton:
                openSelectCPUOptions();
                break;
        }
    }

    private void openSelectCPUOptions() {
        Intent openSelectCPUOptions = new Intent(CreateComputerListActivity.this,
                Selectcpu.class);
        startActivity(openSelectCPUOptions);
    }
}