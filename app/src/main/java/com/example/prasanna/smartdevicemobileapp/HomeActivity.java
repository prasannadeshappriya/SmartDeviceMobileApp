package com.example.prasanna.smartdevicemobileapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prasanna.smartdevicemobileapp.Request.PostRequest;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private ProgressDialog pd;
    private Button btnConnect;
    private Button btnDisconnect;
    private TextView tvDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pd = new ProgressDialog(this);
        tvDisplay = (TextView)findViewById(R.id.tvDisplay);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnDisconnect = (Button) findViewById(R.id.btnDisconnect);

        btnDisconnect.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendDisconnectionRequest();
                    }
                }
        );

        btnConnect.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendConnectionRequest();
                    }
                }
        );
    }

    private void sendDisconnectionRequest() {
        if(tvDisplay.getText().toString().equals("Connected!")) {
            Map<String, String> param = new HashMap<>();
            try {
                param.put("status", "disconnection request");
            } catch (Exception e) {
                Log.i("TAG", "Error while setting parameters for post request");
            }
            PostRequest request = new PostRequest(getApplicationContext(), param, pd, this,
                    Constants.METHOD_DISCONNECT,Constants.DISCONNECT_URL);
            request.execute();
        }else{
            Toast.makeText(this,"This device is not connected!",Toast.LENGTH_LONG).show();
        }
    }

    private void sendConnectionRequest() {
        Map<String, String> param = new HashMap<>();
        try {
            param.put("status","connection request");
        }catch (Exception e){
            Log.i("TAG", "Error while setting parameters for post request");
        }
        PostRequest request = new PostRequest(getApplicationContext(),param,pd, this,
                Constants.METHOD_CONNECT,Constants.CONNECT_URL);
        request.execute();
    }

    public void setTextDisplay(String message){
        tvDisplay.setText(message);
    }
}
