package com.apk.franckadjibao.bluetoothtry;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Franck ADJIBAO on 18/02/2017.
 * */
public class ConnectedAsClientThread extends Thread{
    private final UUID mUUID=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice CDevice;
    BluetoothAdapter bAdapter;
    private Context context;
    private Application application;

    public ConnectedAsClientThread(BluetoothDevice device, Context c, BluetoothAdapter bA, Application app){
        bAdapter=bA;
        CDevice=device;
        context=c;
        application=app;
        BluetoothSocket tmp=null;

        try{
            tmp=device.createRfcommSocketToServiceRecord(mUUID);
        }catch (IOException e1){
            Toast.makeText(context,"Socket non créé",Toast.LENGTH_SHORT).show();
        }
        bluetoothSocket=tmp;
    }

    @Override
    public void run() {
        //On doit faire le cancelDiscovery dans le main
        bAdapter.cancelDiscovery();
        try{
            bluetoothSocket.connect();
        }catch (IOException e2){
            try{
                bluetoothSocket.close();
            }catch (Exception e3){
                Toast.makeText(context,"Impossible de fermer le bs",Toast.LENGTH_SHORT).show();
            }
            return ;
        }
        ((MyApplication)application).setClientSocket(bluetoothSocket);
        Intent intent1=new Intent(context,GoActivity.class);
        intent1.putExtra("statut","client");
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent1);
    }

    public void cancel(){
        try{
            bluetoothSocket.close();
        }catch (Exception e3){
            Toast.makeText(context,"Impossible de fermer le bs",Toast.LENGTH_SHORT).show();
        }
    }
    public BluetoothSocket getBluetoothSocket(){
        return bluetoothSocket;
    }
}
