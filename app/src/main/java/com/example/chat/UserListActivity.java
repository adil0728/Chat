package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    private DatabaseReference mUserDatabaseReference;
    private ChildEventListener mUserChildEventListener;
    private FirebaseAuth mAuth;

    private String userName;
    private ArrayList<User> mUserArrayList;
    private RecyclerView mUserRecyclerView;
    private UserAdapter mUserAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        Intent intent = getIntent();
        if (intent != null) {
            userName = intent.getStringExtra("userName");
        }
        mAuth = FirebaseAuth.getInstance();

        mUserArrayList = new ArrayList<>();

        attachUserDatabaseReferenceListener();
        buildRecyclerView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserListActivity.this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void attachUserDatabaseReferenceListener() {
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        if (mUserChildEventListener == null) {
            mUserChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    User user = snapshot.getValue(User.class);

                    if (!user.getId().equals(mAuth.getCurrentUser().getUid())) {
                        user.setAvatarMockUpResource(R.drawable.baseline_person_24);
                        mUserArrayList.add(user);
                        mUserAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };

            mUserDatabaseReference.addChildEventListener(mUserChildEventListener);
        }
    }

    private void buildRecyclerView() {

        mUserRecyclerView = findViewById(R.id.userListRecyclerView);
        mUserRecyclerView.setHasFixedSize(true);
        mUserRecyclerView.addItemDecoration(new DividerItemDecoration(mUserRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mLayoutManager = new LinearLayoutManager(this);
        mUserAdapter = new UserAdapter(mUserArrayList);

        mUserRecyclerView.setLayoutManager(mLayoutManager);
        mUserRecyclerView.setAdapter(mUserAdapter);

        mUserAdapter.setOnUserClickListener(position -> {
            goToChat(position);
        });

    }

    private void goToChat(int position) {
        Intent intent = new Intent(UserListActivity.this, ChatActivity.class);
        intent.putExtra("recipientUserId", mUserArrayList.get(position).getId());
        intent.putExtra("recipientUserName", mUserArrayList.get(position).getName());
        intent.putExtra("userName", userName);
        startActivity(intent);
    }
}