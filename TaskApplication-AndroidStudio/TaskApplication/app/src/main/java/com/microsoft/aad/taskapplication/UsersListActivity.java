package com.microsoft.aad.taskapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.taskapplication.helpers.Constants;

public class UsersListActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        Button button = (Button) findViewById(R.id.userListCancelButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button = (Button) findViewById(R.id.addUserButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call adal
            }
        });
    }

    private void callAdal() {
        try {
            AuthenticationContext ctx = new AuthenticationContext(UsersListActivity.this, Constants.AUTHORITY_URL, false);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Encryption is failed", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
