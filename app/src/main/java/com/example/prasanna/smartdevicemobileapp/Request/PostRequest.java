package com.example.prasanna.smartdevicemobileapp.Request;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prasanna.smartdevicemobileapp.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by prasanna on 5/5/17.
 */

public class PostRequest extends AsyncTask <Void,Void,Void> {
    private String SERVER_URL;
    private String serverResponse;
    private Context context;
    private Map<String, String> patameters;
    private String method;
    private ProgressDialog pd;
    private HomeActivity activity;
    private String url;

    public PostRequest(Context context, Map<String,String> params, ProgressDialog pd, HomeActivity activity,String method, String url){
        this.context = context;
        this.pd = pd;
        this.patameters = params;
        this.activity = activity;
        this.method = method;
        this.url = "http://10.0.2.2:8000/" + url;
    }
    @Override
    protected void onPreExecute() {
        pd.setIndeterminate(true);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Processing..");
        pd.show();
        SERVER_URL = url;
    }

    @Override
    protected Void doInBackground(final Void... params) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, SERVER_URL,
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
                            Log.i("TAG", "Error caught during request transmission :- " + error.toString());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> paramMap;
                    paramMap = patameters;
                    return paramMap;
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
        }catch (Exception e){
            serverResponse = "Internal Error :- " + e.toString();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.i("TAG","Response :- " + serverResponse);
        try {
            if(method.equals("CONNECT")) {
                JSONObject obj = new JSONObject(serverResponse);
                if (obj.has("serial")) {
                    activity.setTextDisplay("Connected!");
                    Log.i("TAG", "Serial for the session :- " + obj.getString("serial"));
                } else {
                    activity.setTextDisplay("Not Connected!");
                    Log.i("TAG", "Connection error, Please try again");
                }
            }else if(method.equals("DISCONNECT")){
                JSONObject obj = new JSONObject(serverResponse);
                if (obj.has("response")) {
                    activity.setTextDisplay("Not Connected!");
                    Log.i("TAG", "Response :- " + obj.getString("response"));
                } else {
                    activity.setTextDisplay("Connected!");
                    Log.i("TAG", "Connection error, Please try again");
                }
            }
        } catch (JSONException e) {
            Log.i("TAG","Invalid response from server :- " + e.toString());
        }

        pd.dismiss();
    }
}
