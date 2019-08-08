package com.fitness.curli;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    TextView textView;
    private static final int RC_SIGN_IN = 1;
    private String idToken;

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.login_layout);

        String clientId = "471670717660-j3iv4a7clpdseeo9md14s8bkdnelm1o5.apps.googleusercontent.com";

        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientId)
                .build();
        System.out.println("error? ^");

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();



        signInButton=(SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            idToken = account.getIdToken();
            System.out.println("ID TOKEN === > " + idToken.length());

            gotoProfile();
            new postData().execute();
        } else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }

    private class postData extends AsyncTask<String, Integer, JSONObject> {
        final String POST_PARAMS = "{token:" + idToken + "}";

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject response = null;
            try {
                //here we initialize the Send request class which is simply a wrapper for the OKHTTP3 request library
                //this class and the method within it greatly simplified the process of sending a request so that one only needs to use 4 lines
                SendRequest sr = new SendRequest();
                //here we initialize the json object that we will be sending to the server in the request body
                //the server is currently setup to look for a 'token' key with the corresponding value being the actual token received when a google sign in completes
                JSONObject json = new JSONObject();
                json.put("token", "test token");
                //this call is simply to send the above data to the given url
                //the return type of this method is a JSONObject, if the request is successful the JSONObject should be {"result":"success"}, otherwise the method will return null
                response = sr.sendJson("http://34.74.139.218/api/", json.toString());
                System.out.println(response);

            }catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onProgressUpdate(Integer... progress){}

        @Override
        protected void onPostExecute(JSONObject response){
            System.out.println("H - > " + response);
        }
    }

    private void gotoProfile(){
        Intent intent=new Intent(Login.this, InfoView.class);
        startActivity(intent);
    }

}
