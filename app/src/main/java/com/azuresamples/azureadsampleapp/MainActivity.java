package com.azuresamples.azureadsampleapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.microsoft.aad.adal.*;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    /* UI & Debugging Variables */
    private static final String TAG = MainActivity.class.getSimpleName();
    Button callGraphButton;
    Button signOutButton;

    /* Azure AD Constants */
    //TODO : Add description for Authority
    private static final String AUTHORITY = "https://login.microsoftonline.com/common";
    /* The clientID of your application is a unique identifier which can be obtained from the app registration portal */
    private static final String CLIENT_ID = "<ENTER YOUR CLIENT ID HERE>";
    /* Resource URI of the endpoint which will be accessed */
    private static final String RESOURCE_ID = "https://graph.microsoft.com/";
    /* The Redirect URI of the application (Optional) */
    private static final String REDIRECT_URI = "<ENTER YOUR REDIRECT URI HERE";

    /* Microsoft Graph Constants */
    private final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me";

    /* Azure AD Variables */
    private AuthenticationContext mAuthContext;
    private AuthenticationResult mAuthResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callGraphButton = (Button) findViewById(R.id.callGraph);
        signOutButton = (Button) findViewById(R.id.clearCache);

        callGraphButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCallGraphClicked();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSignOutClicked();
            }
        });

        mAuthContext = new AuthenticationContext(MainActivity.this, AUTHORITY, false);

        /*Attempt an acquireTokenSilent call to see if we're signed in*/

       Iterator<TokenCacheItem> iterator =  mAuthContext.getCache().getAll();
       while (iterator.hasNext()){
           TokenCacheItem tokenCacheItem = iterator.next();
           String userId = tokenCacheItem.getUserInfo().getUserId();
           if(!TextUtils.isEmpty(userId)){
               mAuthContext.acquireTokenSilentAsync(RESOURCE_ID, CLIENT_ID, userId, getAuthSilentCallback());
               break;
           }
       }
    }

    //
    // Core Auth methods used by ADAL
    // ==================================
    // onActivityResult() - handles redirect from System browser
    // onCallGraphClicked() - attempts to get tokens for graph, if it succeeds calls graph & updates UI
    // callGraphAPI() - called on successful token acquisition which makes an HTTP request to graph
    // onSignOutClicked() - Signs user out of the app & updates UI
    //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAuthContext.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * End user clicked call Graph API button, time for Auth
     * Use ADAL to get an Access token for the Microsoft Graph API
     */
    private void onCallGraphClicked() {
        mAuthContext.acquireToken(getActivity(), RESOURCE_ID, CLIENT_ID, REDIRECT_URI,  PromptBehavior.Auto, getAuthInteractiveCallback());
    }

    private void callGraphAPI() {
        Log.d(TAG, "Starting volley request to graph");

        /* Make sure we have a token to send to graph */
        if (mAuthResult.getAccessToken() == null) {return;}

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
        } catch (Exception e) {
            Log.d(TAG, "Failed to put parameters: " + e.toString());
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MSGRAPH_URL,
                parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: " + response.toString());

                updateGraphUI(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + mAuthResult.getAccessToken());
                return headers;
            }
        };

        Log.d(TAG, "Adding HTTP GET to Queue, Request: " + request.toString());
        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void onSignOutClicked() {
        // End user has clicked the Sign Out button
        // Kill the token cache
        // Optionally call the signout endpoint to fully sign out the user account
        mAuthContext.getCache().removeAll();
        updateSignedOutUI();
    }

    //
    // UI Helper methods
    // ================================
    // updateGraphUI() - Sets graph response in UI
    // updateSuccessUI() - Updates UI when token acquisition succeeds
    // updateSignedOutUI() - Updates UI when app sign out succeeds
    //

    private void updateGraphUI(JSONObject response) {
        // Called on success from /me endpoint
        // Writes graph data to the UI
        TextView graphText = (TextView) findViewById(R.id.graphData);
        graphText.setText(response.toString());
    }

    private void updateSuccessUI() {
        // Called on success from /me endpoint
        // Removed call Graph API button and paint Sign out
        callGraphButton.setVisibility(View.INVISIBLE);
        signOutButton.setVisibility(View.VISIBLE);
        findViewById(R.id.welcome).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.welcome)).setText("Welcome, " +
                mAuthResult.getUserInfo().getGivenName());
        findViewById(R.id.graphData).setVisibility(View.VISIBLE);

    }

    private void updateSignedOutUI() {
        callGraphButton.setVisibility(View.VISIBLE);
        signOutButton.setVisibility(View.INVISIBLE);
        findViewById(R.id.welcome).setVisibility(View.INVISIBLE);
        findViewById(R.id.graphData).setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.graphData)).setText("No Data");
    }

    //
    // ADAL Callbacks
    // ======================
    // getActivity() - returns activity so we can acquireToken within a callback
    // getAuthSilentCallback() - callback defined to handle acquireTokenSilent() case
    // getAuthInteractiveCallback() - callback defined to handle acquireToken() case
    //

    public Activity getActivity() {
        return this;
    }

    /* Callback used in for silent acquireToken calls.
     * Looks if tokens are in the cache (refreshes if necessary and if we don't forceRefresh)
     * else errors that we need to do an interactive request.
     */
    private AuthenticationCallback<AuthenticationResult> getAuthSilentCallback() {
        return new AuthenticationCallback<AuthenticationResult>() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                /* Successfully got a token, call graph now */
                Log.d(TAG, "Successfully authenticated");

                /* Store the mAuthResult */
                mAuthResult = authenticationResult;
                /* call graph */
                callGraphAPI();

                /* update the UI to post call graph state */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateSuccessUI();
                    }
                });
            }

            @Override
            public void onError(Exception exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof AuthenticationException) {
                    /* TODO: Make sure user cancel error is covered */
                   ADALError  error = ((AuthenticationException)exception).getCode();
                   if(error == ADALError.ERROR_SILENT_REQUEST || error == ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED || error == ADALError.INVALID_TOKEN_CACHE_ITEM ){
                       /* Tokens expired or no session, retry with interactive */
                       mAuthContext.acquireToken(getActivity(), RESOURCE_ID, CLIENT_ID, REDIRECT_URI,  PromptBehavior.Auto, getAuthInteractiveCallback());
                   }
                } else {
                    /* Tokens expired or no session, retry with interactive */
                    mAuthContext.acquireToken(getActivity(), RESOURCE_ID, CLIENT_ID, REDIRECT_URI,  PromptBehavior.Auto, getAuthInteractiveCallback());
                }
            }
        };
    }

    /* Callback used for interactive request.  If succeeds we use the access
     * token to call the Microsoft Graph. Does not check cache
     */
    private AuthenticationCallback<AuthenticationResult> getAuthInteractiveCallback() {
        return new AuthenticationCallback<AuthenticationResult>() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                /* Successfully got a token, call graph now */
                Log.e(TAG, "Successfully authenticated");
                Log.e(TAG, "ID Token: " + authenticationResult.getIdToken());

                /* Store the auth result */
                mAuthResult = authenticationResult;

                /* call graph */
                callGraphAPI();

                /* update the UI to post call graph state */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateSuccessUI();
                    }
                });

            }

            @Override
            public void onError(Exception exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof AuthenticationException) {
                    /* TODO: Implement error handling described in doc https://docs.microsoft.com/en-us/azure/active-directory/develop/active-directory-devhowto-adal-error-handling */
                    /* TODO: Make sure user cancel error is covered */

                } else {
                    /* Tokens expired or no session, retry with interactive */
                }
            }
        };
    }

}
