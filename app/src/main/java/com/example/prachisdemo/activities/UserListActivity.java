package com.example.prachisdemo.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.example.prachisdemo.R;
import com.example.prachisdemo.adapter.UserRecyclerAdapter;
import com.example.prachisdemo.database.DatabaseHelper;
import com.example.prachisdemo.models.User;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class UserListActivity extends AppCompatActivity {

    private AppCompatActivity activity = UserListActivity.this;
    private TextView textViewName;
    private RecyclerView recyclerViewUsers;
    private List<User> listUsers;
    private UserRecyclerAdapter usersRecyclerAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        initViews();
        initObjects();

    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        textViewName = findViewById(R.id.textViewName);
        recyclerViewUsers = (RecyclerView) findViewById(R.id.recyclerViewUsers);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        listUsers = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);
        usersRecyclerAdapter = new UserRecyclerAdapter(this,listUsers,databaseHelper);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(usersRecyclerAdapter);

        String emailFromIntent = getIntent().getStringExtra("EMAIL");
        textViewName.setText(emailFromIntent);
        listUsers.clear();
        listUsers.addAll(databaseHelper.getAllUser());
        usersRecyclerAdapter.notifyDataSetChanged();
    }
}
