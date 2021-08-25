package com.example.minderheitenquartett;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.util.ArrayList;

public class DeviceListAdapter {
    Context context;
    int device_adapter_view;
    ArrayList<BluetoothDevice> mBTDevices;

    public DeviceListAdapter(Context context, int device_adapter_view, ArrayList<BluetoothDevice> mBTDevices) {
        this.context = context;
        this.device_adapter_view = device_adapter_view;
        this.mBTDevices = mBTDevices;
    }

}