package com.apk.franckadjibao.bluetoothtry;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;


public class AdapterBluetooth extends ArrayAdapter<String> {

    public AdapterBluetooth(Context c, List<String> nomBluetooth){
        super(c,R.layout.bluetooth_devices_layout,R.id.name,nomBluetooth);
    }
}
