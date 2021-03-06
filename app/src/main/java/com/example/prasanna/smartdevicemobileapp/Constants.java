package com.example.prasanna.smartdevicemobileapp;

/**
 * Created by prasanna on 5/5/17.
 */

public abstract class Constants {
    //From Mobilr Devices
    public static final String CONNECT_URL = "/SmartDeviceController/public/connect";
    public static final String DISCONNECT_URL = "/SmartDeviceController/public/disconnect";
    public static final String PROCESS_URL = "/SmartDeviceController/public/read_data";

    //From Android Emulators
//    public static final String CONNECT_URL = "http://10.0.2.2:8000/connect";
//    public static final String DISCONNECT_URL = "http://10.0.2.2:8000/disconnect";
//    public static final String PROCESS_URL = "http://10.0.2.2:8000/read_data";


    public static final String METHOD_CONNECT = "CONNECT";
    public static final String METHOD_DISCONNECT = "DISCONNECT";
}
