package br.com.silver.dybapp.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import br.com.silver.dybapp.R;
import br.com.silver.dybapp.domain.Delivery;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebClient {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;
    private String url;
    private SharedPreferences prefs;

    public WebClient(Context ctx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        url = prefs.getString(ctx.getString(R.string.pref_url), "");
        client = new OkHttpClient();
    }

    public String post(Delivery delivery) {
        if(url.equals("")) {
            return "Configure o endereço do servidor";
        }

        RequestBody body = RequestBody.create(JSON, delivery.toJson());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
        catch(Exception e) {
            Log.d("ERROR", e.getMessage());
            return e.getMessage();
        }

    }

    public String postAll(ArrayList<String> deliveries) {
        if(url.equals("")) {
            return "Configure o endereço do servidor";
        }

        HashMap<String, ArrayList<String>> map = new HashMap<>();
        map.put("deliveries", deliveries);

        Gson gson = new Gson();
        String jsonDeliveries = gson.toJson(map);

        RequestBody body = RequestBody.create(JSON, jsonDeliveries);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if(response.body() != null) {
                return response.body().string();
            }

            return "";
        }
        catch(Exception e) {
            Log.d("ERROR", e.getMessage());
            return e.getMessage();
        }

    }

}
