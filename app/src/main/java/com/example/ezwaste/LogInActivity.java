package com.example.ezwaste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {
    private Button button;
    private TextView text_username;
    private TextView text_password;
    private Context loginContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_log_in);
        loginContext = this;
        button = (Button) findViewById(R.id.btn_login);
        text_username = (TextView) findViewById(R.id.text_username);
        text_password = (TextView) findViewById(R.id.text_password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomeActivity();
                button.setEnabled(false);
            }
        });
    }

    private void openHomeActivity() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("user_table");

        Query query = databaseReference.orderByChild("userName").equalTo(text_username.getText().toString().trim());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        User user = snapshot.getValue(User.class);

                        if (user.getPassword().equals(text_password.getText().toString().trim())) {
                            Intent intent = new Intent(loginContext, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(loginContext,"Password is wrong:"+user.getPassword(),Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(loginContext, "User not found", Toast.LENGTH_LONG).show();
                }
                button.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(loginContext, "Database Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
