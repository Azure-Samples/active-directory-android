package com.microsoft.aad.taskapplication.helpers;

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

    public static List<String> getAllItems(String token) {
        HttpURLConnection conn = null;
        BufferedReader br = null;
        try {
            List<String> items = new ArrayList<>();
            URL url = new URL(Constants.SERVICE_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String apiOutput = br.readLine();
            JSONArray jsonArray = new JSONArray(apiOutput);
            JSONObject obj = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                obj = jsonArray.getJSONObject(i);
                items.add(obj.getString("Title"));
            }
            return items;
        } catch (Exception e) {
            return null;
        } finally {
            AppHelper.close(br);
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    public static void addItem(String title, String token) throws Exception{
        //
        HttpURLConnection conn = null;
        BufferedReader br = null;
        try {
            List<String> items = new ArrayList<>();
            URL url = new URL(Constants.SERVICE_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            String urlParameters = "{        \"Title\": \""+title+"\"    }";

            // Send post request
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();
            if(responseCode !=200 || responseCode !=204){
                throw new Exception("invalid response code:" + responseCode);
            }
        } finally {
            AppHelper.close(br);
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
