package com.microsoft.aad.taskapplication.helpers;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TodoListHttpService {

    private class TodoServiceGetAll extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader br = null;
            try {
                List<String> items = new ArrayList<>();
                URL url = new URL(Constants.SERVICE_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + params[0]);
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                }

                br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                String apiOutput = br.readLine();
                JSONArray jsonArray = new JSONArray(apiOutput);
                JSONObject obj = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = jsonArray.getJSONObject(i);
                    items.add(obj.getString("task"));
                }
                return items;
            } catch (Exception e) {
                return new ArrayList<>();
            } finally {
                AppHelper.close(br);
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }


    private class TodoServicePostTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader br = null;
            try {
                List<String> items = new ArrayList<>();
                URL url = new URL(Constants.SERVICE_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + params[1]);
                String urlParameters = "{        \"task\": \"" + params[0] + "\"    }";

                // Send post request
                conn.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = conn.getResponseCode();
                if (responseCode != 200 || responseCode != 204) {
                    throw new Exception("invalid response code:" + responseCode);
                }
            } catch (Exception e) {
                //TODO - do what?
            } finally {
                AppHelper.close(br);
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;
        }
    }

    public List<String> getAllItems(String token) throws Exception {
        return new TodoServiceGetAll().execute(token).get();
    }


    public void addItem(String title, String token) throws Exception {
        new TodoServicePostTask().execute(title, token);
    }
}
