package br.com.silver.dybapp.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
        if(url == "") {
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
            return e.getMessage();
        }

    }

}
