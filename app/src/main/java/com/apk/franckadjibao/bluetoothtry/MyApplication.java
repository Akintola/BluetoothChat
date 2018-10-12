package com.apk.franckadjibao.bluetoothtry;

import android.app.Application;
import android.bluetooth.BluetoothSocket;

/**
 * Created by Franck ADJIBAO on 06/03/2017.
 */
public class MyApplication extends Application {
    BluetoothSocket clientSocket;
    BluetoothSocket serveurSocket;

    public void setClientSocket(BluetoothSocket socket) {
        this.clientSocket = socket;
    }
    public BluetoothSocket getClientSocket(){
        return clientSocket;
    }
    public void setServeurSocket(BluetoothSocket socket) {
        this.serveurSocket = socket;
    }
    public BluetoothSocket getServeurSocket(){
        return serveurSocket;
    }
}
