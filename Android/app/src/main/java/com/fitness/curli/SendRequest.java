package com.fitness.curli;

import android.text.Html;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SendRequest {

    public JSONObject sendJson(String URL, String json) throws IOException {
        JSONObject jsonObjectResp = null;

        try {
            //this tells the server that it will be recieving json instead of something like form
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            //this just adds the json and the url provided from the parameter of this method to the request body so that it can later be sent
            okhttp3.RequestBody body = RequestBody.create(JSON, json.toString());
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(URL)
                    .post(body)
                    .build();
            //this is where the request is actually send, hence the .execute()
            okhttp3.Response response = client.newCall(request).execute();
            //here the url encoded quote or " is normally represented by '&quot;' so we need to decode it to: "
            String networkResp = response.body().string().replaceAll("&quot;", "'"); // raises exception if first 2 escape chars arent present
            if (!networkResp.isEmpty()) {
                jsonObjectResp = new JSONObject(networkResp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        //if any errors are present with this request the method will simply return null instead of the typical JSONObject
        return jsonObjectResp;
    }

}