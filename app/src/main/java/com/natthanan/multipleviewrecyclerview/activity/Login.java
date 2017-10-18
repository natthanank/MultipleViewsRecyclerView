package com.natthanan.multipleviewrecyclerview.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.natthanan.multipleviewrecyclerview.R;
import com.natthanan.multipleviewrecyclerview.model.RequestModel;
import com.natthanan.multipleviewrecyclerview.model.ResponseModel;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginAsyncTask().execute();

            }
        });
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private class LoginAsyncTask extends AsyncTask<RequestModel, String, String> {
        HttpURLConnection connection = null;
        @Override
        protected String doInBackground(RequestModel... params) {
            try {
                URL url = new URL("http://arismktd.ar.co.th/USE001/MSignin");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("charset", "utf-8");
                connection.setRequestMethod("POST");
                connection.connect();
                RequestModel requestModel = new RequestModel();
                requestModel.setDeviceOwner("Sarinthon's MacBook Pro");
                requestModel.setAppName("AProject");
                requestModel.setOs("iOS");
                requestModel.setPassword(md5("123456"));
                requestModel.setAppVersion("1.8.0");
                requestModel.setDeviceName("Sarinthon's MacBook Pro");
                requestModel.setUsername("natthanank");
                requestModel.setDeviceModel("iPhone");
                requestModel.setEmulatorVersion("10.2");
                requestModel.setMacAddress("4171C44B-ACE6-41C1-9F83-E7194D8A3AF2");
                Gson gson = new Gson();
                String json = "json="+gson.toJson(requestModel).toString();


                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.write(json.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                System.out.println("response code = " +connection.getResponseCode());
                String json_response = "";
                InputStreamReader in = new InputStreamReader(connection.getInputStream());
                BufferedReader br = new BufferedReader(in);
                String text = "";
                while ((text = br.readLine()) != null) {
                    json_response += text;
                }

                return json_response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(s);
            try {
                Gson gson = new Gson();
                ResponseModel responseModel = gson.fromJson(s, ResponseModel.class);
                System.out.println(responseModel.getStatus());
            } catch (Exception e) {

            }

        }
    }
}


