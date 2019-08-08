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

    private class postData extends AsyncTask<String, Integer, StringBuffer> {
        final String POST_PARAMS = "{token:" + idToken + "}";

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected StringBuffer doInBackground(String... params) {
            StringBuffer response = new StringBuffer();
            try {
                URL obj = new URL("http://34.74.139.218/api/");

                System.out.println(obj);

                HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();

                System.out.println(postConnection);

                String cookiesHeader = postConnection.getHeaderField("Set-Cookie");
                postConnection.disconnect();
                postConnection = (HttpURLConnection) obj.openConnection();

                postConnection.setRequestMethod("POST");

                postConnection.setRequestProperty("Cookie", cookiesHeader);

                //postConnection.setRequestProperty("userId", "a1bcdefgh");

                postConnection.setRequestProperty("Content-Type", "application/json");

                postConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (platform; rv:geckoversion) Gecko/geckotrail Firefox/firefoxversion");

                postConnection.setDoOutput(true);

                System.out.println("POST CONNECTION = " + postConnection);

                OutputStream os = postConnection.getOutputStream();

                System.out.println("OS = ");

                os.write(POST_PARAMS.getBytes());

                System.out.println("OS.WRITE");

                os.flush();

                os.close();

                int responseCode = postConnection.getResponseCode();

                System.out.println("POST Response Code :  " + responseCode);

                System.out.println("POST Response Message : " + postConnection.getResponseMessage());

                if (responseCode == HttpURLConnection.HTTP_CREATED) { //success

                    BufferedReader in = new BufferedReader(new InputStreamReader(

                            postConnection.getInputStream()));

                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {

                        response.append(inputLine);

                    }
                    in.close();

                    // print result

                    System.out.println(response.toString());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onProgressUpdate(Integer... progress){}

        @Override
        protected void onPostExecute(StringBuffer response){
            System.out.println("H - > " + response);
        }
    }

    private void gotoProfile(){
        Intent intent=new Intent(Login.this, InfoView.class);
        startActivity(intent);
    }

}
