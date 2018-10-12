package com.apk.franckadjibao.bluetoothtry;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Franck ADJIBAO on 18/02/2017.
 */
public class ConnectedAsServerThread extends Thread {
    private final UUID mUUID=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothServerSocket serverSocket;
    private BluetoothSocket socket;
    private Context context;
    private Application application;

    public ConnectedAsServerThread(BluetoothAdapter bluetoothAdapter, Context c, Application app){
        context=c;
        application=app;
        BluetoothServerSocket tmp=null;
        try{
            tmp=bluetoothAdapter.listenUsingRfcommWithServiceRecord("BluetoothTry",mUUID);
        }catch (IOException e){}
        serverSocket=tmp;
    }

    @Override
    public void run() {
        //Toast.makeText(context,"Dans le run du serveur",Toast.LENGTH_SHORT).show();
        while(true){
            try {
                socket=serverSocket.accept();
            }catch (IOException e1){
                break;
            }

            if(socket!=null){
                //Toast.makeText(context,"Le socket n'est pas null",Toast.LENGTH_SHORT).show();
                ((MyApplication)application).setServeurSocket(socket);
                Intent intent=new Intent(context,GoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("statut","serveur");
                application.startActivity(intent);
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancel(){
        try{
            serverSocket.close();
        }catch (IOException e2){
            Log.v("ERREUR","Serveur non éteint");
        }
    }
    public BluetoothSocket getSocket(){
        Toast.makeText(context,"Vous êtes dans le getter de socket",Toast.LENGTH_SHORT).show();
        return socket;
    }
}
