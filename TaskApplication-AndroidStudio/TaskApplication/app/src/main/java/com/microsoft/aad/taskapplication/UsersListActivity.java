package com.microsoft.aad.taskapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.ITokenStoreQuery;
import com.microsoft.aad.adal.PromptBehavior;
import com.microsoft.aad.adal.TokenCacheItem;
import com.microsoft.aad.taskapplication.helpers.Constants;
import com.microsoft.aad.taskapplication.helpers.InMemoryCacheStore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class UsersListActivity extends Activity {

    private AuthenticationContext mAuthContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        mAuthContext= new AuthenticationContext(UsersListActivity.this, Constants.AUTHORITY_URL,
                false, InMemoryCacheStore.getInstance());

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

                if(Constants.CORRELATION_ID != null &&
                        Constants.CORRELATION_ID.trim().length() !=0){
                    mAuthContext.setRequestCorrelationId(UUID.fromString(Constants.CORRELATION_ID));
                }
                mAuthContext.acquireToken(UsersListActivity.this, Constants.RESOURCE_ID, Constants.CLIENT_ID,
                        Constants.REDIRECT_URL, Constants.USER_HINT, PromptBehavior.REFRESH_SESSION,
                        "nux=1" + Constants.EXTRA_QP, new AuthenticationCallback<AuthenticationResult>() {

                            @Override
                            public void onSuccess(AuthenticationResult result) {
                                Constants.CURRENT_RESULT = result;
                                finish();
                                startActivity(getIntent());
                            }

                            @Override
                            public void onError(Exception exc) {
                                SimpleAlertDialog.showAlertDialog(UsersListActivity.this, "Exception caught", exc.getMessage());
                            }
                        });
            }
        });

        ListView listview = (ListView) findViewById(R.id.usersList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, refreshedUsersList());
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                callAdal(item);
            }

        });
    }

    private List<String> refreshedUsersList() {
        List<String> list = new ArrayList<>();
        ITokenStoreQuery cacheStoreQuery = InMemoryCacheStore.getInstance();
        Iterator<TokenCacheItem> iter = cacheStoreQuery.getAll();
        while (iter.hasNext()) {
            TokenCacheItem item = iter.next();
            if (item.getUserInfo() != null && !list.contains(item.getUserInfo().getDisplayableId())) {
                list.add(item.getUserInfo().getDisplayableId());
            }
        }
        return list;
    }

    private void callAdal(String user) {
        if(Constants.CORRELATION_ID != null &&
                Constants.CORRELATION_ID.trim().length() !=0){
            mAuthContext.setRequestCorrelationId(UUID.fromString(Constants.CORRELATION_ID));
        }

        mAuthContext.acquireToken(UsersListActivity.this, Constants.RESOURCE_ID, Constants.CLIENT_ID,
                Constants.REDIRECT_URL, user, PromptBehavior.REFRESH_SESSION,
                "nux=1&" + Constants.EXTRA_QP, new AuthenticationCallback<AuthenticationResult>() {

                    @Override
                    public void onSuccess(AuthenticationResult result) {
                        Constants.CURRENT_RESULT = result;
                        finish();
                    }

                    @Override
                    public void onError(Exception exc) {
                        SimpleAlertDialog.showAlertDialog(UsersListActivity.this, "Exception caught", exc.getMessage());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mAuthContext != null) {
            mAuthContext.onActivityResult(requestCode, resultCode, data);
        }
    }
}
