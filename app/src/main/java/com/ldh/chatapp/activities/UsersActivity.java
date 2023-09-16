package com.ldh.chatapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ldh.chatapp.adapters.UserAdapter;
import com.ldh.chatapp.databinding.ActivityUsersBinding;
import com.ldh.chatapp.listeners.UserListener;
import com.ldh.chatapp.models.User;
import com.ldh.chatapp.utlities.Constants;
import com.ldh.chatapp.utlities.PreferenceManager;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity implements UserListener {
    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        getUsers();
        setListeners();
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    // TODO: get users
    private void getUsers() {
        loading(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_USERS).get().addOnCompleteListener(task -> {
            loading(false);
            String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
            if (task.isSuccessful() && task.getResult() != null) {
                ArrayList<User> users = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                        continue;
                    }
                    User user = new User(queryDocumentSnapshot.getString(Constants.KEY_NAME),
                            queryDocumentSnapshot.getString(Constants.KEY_IMAGE),
                            queryDocumentSnapshot.getString(Constants.KEY_EMAIL),
                            queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN),
                            queryDocumentSnapshot.getId()
                            );
                    users.add(user);

                }
                if (users.size() > 0) {
                    UserAdapter adapter = new UserAdapter(users, this);
                    binding.rvUsers.setAdapter(adapter);
                    binding.rvUsers.setVisibility(View.VISIBLE);
                } else {
                    showErrorMessage();
                }
            } else {
                showErrorMessage();
            }
        });
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s", "No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}