package com.example.prasanna.smartdevicemobileapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button btnDisconnect;
    private Button btnStart;
    private EditText IP;
    private TextView tvDisplay;
    private TextView tvDisplayOutput;
    private Boolean con;
    private String serial;
    private String data="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        
        //Key Listeners
        btnStart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startReceiving();
                    }
                }
        );
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

    private void startReceiving()  {
        if(btnStart.getText().toString().equals("Start")){
            if(tvDisplay.getText().toString().equals("Connected!")) {
                setStartButtonText(1);
                con = true;
                processInput process = new processInput(this,IP.getText().toString());
                process.execute();
            }else{
                Toast.makeText(this,"This device is not connected!",Toast.LENGTH_LONG).show();
            }

        }else{
            con = false;
            setStartButtonText(0);
        }
    }

    private class processInput extends AsyncTask<Void,Void,Void>{
        private String serverResponse;
        private Context context;
        private String data;
        private String ip_value;
        public processInput(Context context, String ip_value){
            this.context = context; this.ip_value = ip_value;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(con==true){
                setTextOutputDisplay(data);
                processInput process = new processInput(context,ip_value);
                process.execute();
            }else{
                setTextOutputDisplay("No Output Available !");
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("TAG","Send request and receive data from server");

            //-------------------------------------------------------------
            try {
                Log.i("TAG","http://" + ip_value + Constants.PROCESS_URL);
                StringRequest request = new StringRequest(Request.Method.POST, "http://" + ip_value + Constants.PROCESS_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                serverResponse = response;
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                serverResponse = error.toString();
                                con = false;
                                Log.i("TAG", "Error caught during request transmission :- " + error.toString());
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        try {
                            param.put("serial", serial);
                            param.put("status","reading_data");
                        } catch (Exception e) {
                            con = false;
                            Log.i("TAG", "Error while setting parameters for post request");
                        }
                        return param;
                    }
                };

                request.setTag("TAG");
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(request);

                long start_time = System.currentTimeMillis();
                long duration = 0;
                while (serverResponse==null){
                    long present_time = System.currentTimeMillis();
                    duration = (present_time-start_time);
                }
                Log.i("TAG","Time taken :- " + String.valueOf(duration));
                JSONObject obj = new JSONObject(serverResponse);
                if (obj.has("data")) {
                    data = obj.getString("data");
                    Log.i("TAG", obj.getString("data"));
                } else {
                    con = false;
                    if (obj.has("error")){
                        Log.i("TAG", obj.getString("error"));
                    }else {
                        Log.i("TAG", "Connection error, Please try again");
                    }
                }
            }catch (Exception e){
                con = false;
                serverResponse = "Internal Error :- " + e.toString();
            }
            //-------------------------------------------------------------

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                con = false;
                serverResponse = "While-loop Error :- " + e.toString();
            }
            return null;
        }
    }

    private void init() {
        //Initialize everything
        pd = new ProgressDialog(this); con=false;
        tvDisplay = (TextView)findViewById(R.id.tvDisplay);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnDisconnect = (Button) findViewById(R.id.btnDisconnect);
        btnStart = (Button) findViewById(R.id.btnStart);
        tvDisplayOutput = (TextView) findViewById(R.id.tvDisplayOutput);
        IP = (EditText) findViewById(R.id.etIP);
    }

    private void sendDisconnectionRequest() {
        if(tvDisplay.getText().toString().equals("Connected!")) {
            con = false;
            Map<String, String> param = new HashMap<>();
            try {
                param.put("status", "disconnection request");
            } catch (Exception e) {
                Log.i("TAG", "Error while setting parameters for post request");
            }
            PostRequest request = new PostRequest(getApplicationContext(), param, pd, this,
                    Constants.METHOD_DISCONNECT,"http://" + IP.getText().toString() + Constants.DISCONNECT_URL);
            request.execute();
        }else{
            Toast.makeText(this,"This device is not connected!",Toast.LENGTH_LONG).show();
        }
    }

    private void sendConnectionRequest() {
        if(tvDisplay.getText().toString().equals("Connected!")) {
            Toast.makeText(this,"This device is already connected!",Toast.LENGTH_LONG).show();
        }else {
            Map<String, String> param = new HashMap<>();
            try {
                param.put("status", "connection request");
            } catch (Exception e) {
                Log.i("TAG", "Error while setting parameters for post request");
            }
            PostRequest request = new PostRequest(getApplicationContext(), param, pd, this,
                    Constants.METHOD_CONNECT, "http://" + IP.getText().toString() + Constants.CONNECT_URL);
            request.execute();
        }
    }

    public void setTextDisplay(String message){
        tvDisplay.setText(message);
    }

    public void setTextOutputDisplay(String message){
        tvDisplayOutput.setText(message);
    }

    public void setSerial(String serial){this.serial = serial;}

    public void setStartButtonText(int value){
        if(value==0){
            btnStart.setText("Start");
        }else{
            btnStart.setText("Stop");
        }
    }
}
