package com.neumontmc.stats_app.Activities;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.neumontmc.api.Models.User;
import com.neumontmc.stats_app.Controllers.APIController;
import com.neumontmc.stats_app.Controllers.ObjCompressor;
import com.neumontmc.stats_app.Controllers.SearchAdapter;
import com.neumontmc.stats_app.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;

public class SearchUsersActivity extends AppCompatActivity {
    APIController apic;
    RecyclerView recyclerView;
    FloatingActionButton reload, logo, help, info;
    boolean isFABOpen = false;
    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);
        try {
            ObjCompressor objCompressor = new ObjCompressor();
            apic = (APIController) objCompressor.decompressObject(getIntent().getByteArrayExtra("compressedApiController"));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        run();
    }

    private void run() {
        recyclerView = findViewById(R.id.reView);
        users = apic.getUserList();
        genViewer(removeFaction(users));
        EditText searchUserET = findViewById(R.id.searchUserET);
        searchUserET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        logo = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        reload = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        help = (FloatingActionButton) findViewById(R.id.floatingActionButton3);
        info = (FloatingActionButton) findViewById(R.id.floatingActionButton4);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) showFABMenu();
                else closeFABMenu();
            }
        });

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessPhoenix.triggerRebirth(getApplicationContext());
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(1);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), InformationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showFABMenu() {
        isFABOpen = true;
        reload.animate().translationY(-155);//aprox 145 len
        help.animate().translationX(-155);
        info.animate().translationY(-155).translationX(-155);
        logo.setImageResource(R.drawable.ic_close);
    }

    private void closeFABMenu() {
        isFABOpen = false;
        reload.animate().translationY(0);
        help.animate().translationX(0);
        info.animate().translationY(0).translationX(0);
        logo.setImageResource(R.drawable.ic_logo );
    }

    private ArrayList<User> removeFaction(ArrayList<User> currentList) {
        ArrayList<User> cleanList = new ArrayList<>();
        for (User u : currentList)
            if (!u.getUsername().toLowerCase().contains("faction-")) cleanList.add(u);
        return cleanList;
    }

    private void filter(String text) {
        ArrayList<User> filteredList = new ArrayList<>();
        for (User u : users)
            if (u.getUsername().toLowerCase().contains(text.toLowerCase())) filteredList.add(u);
            else if (u.getUuid().toString().toLowerCase().contains(text.toLowerCase())) filteredList.add(u);
        genViewer(removeFaction(filteredList));
    }

    private void genViewer(ArrayList<User> currentUsers) {
        SearchAdapter sAdapter = new SearchAdapter(this, currentUsers, apic);
        recyclerView.setAdapter(sAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}