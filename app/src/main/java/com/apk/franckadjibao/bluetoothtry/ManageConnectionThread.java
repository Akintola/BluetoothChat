package com.apk.franckadjibao.bluetoothtry;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Franck ADJIBAO on 18/02/2017.
 */
public class ManageConnectionThread extends Thread {
    private InputStream inputStream;
    private OutputStream outputStream;
    private BluetoothSocket mSocket;
    private Context context;
    private Handler mhandler;
    byte[] messageBytes=new byte[1024];

    public ManageConnectionThread(BluetoothSocket socket, Context c, Handler handler){
        context=c;
        mSocket=socket;
        mhandler=handler;
        InputStream tmpinputStream=null;
        OutputStream tmpoutStream=null;

        try{
            tmpinputStream=mSocket.getInputStream();
        }catch (Exception e1){
            Toast.makeText(c,"Flux d'entrée non récupéré",Toast.LENGTH_SHORT).show();
        }
        try{
            tmpoutStream=mSocket.getOutputStream();
        }catch (Exception e2){
            Toast.makeText(c,"Flux de sortie non récupéré",Toast.LENGTH_SHORT).show();
        }
        inputStream=tmpinputStream;
        outputStream=tmpoutStream;
    }

    @Override
    public void run() {
        int mInt;
        while(true){
            try{
                mInt=inputStream.read(messageBytes);
                //Dans le package android.os on prend Handler
                Message msg= mhandler.obtainMessage(2,mInt,-1,messageBytes);
                msg.sendToTarget();

            }catch (Exception e){

            }
        }
    }

    public void write(final String msg){
        try{
                outputStream.write(msg.getBytes());
                outputStream.flush();

                Message msg_sended= mhandler.obtainMessage(1,msg);
                msg_sended.sendToTarget();

        }catch (Exception e2){
            Toast.makeText(context,"On n'a pas pu écrire le message",Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel(){
        try{
            mSocket.close();
        }catch (IOException ioe){
            Toast.makeText(context,"On n'a pas pu fermer le canal",Toast.LENGTH_SHORT).show();
        }
    }
}
