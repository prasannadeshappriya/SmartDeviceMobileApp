package com.example.prasanna.smartdevicemobileapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prasanna.smartdevicemobileapp.Request.PostRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private ProgressDialog pd;
    private Button btnConnect;
    private TextView tvDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pd = new ProgressDialog(this);
        tvDisplay = (TextView)findViewById(R.id.tvDisplay);
        btnConnect = (Button) findViewById(R.id.btnConnect);

        btnConnect.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendConnectionRequest();
                    }
                }
        );
    }

    private void sendConnectionRequest() {
        Map<String, String> param = new HashMap<>();
        try {
            param.put("status","connection request");
        }catch (Exception e){
            Log.i("TAG", "Error while setting parameters for post request");
        }
        PostRequest request = new PostRequest(getApplicationContext(),param,pd, this);
        request.execute();
    }
}
