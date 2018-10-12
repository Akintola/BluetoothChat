package com.apk.franckadjibao.bluetoothtry;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ListView listView=null;
    TextView rechercher=null;
    int REQUEST_ENABLE_BT=1;
    BluetoothAdapter bluetoothAdapter;
    BroadcastReceiver bluetoothBroadCast;
    ConnectedAsClientThread client=null;
    ArrayList<BluetoothDevice> newBluetoothDevices;
    List<String> ListNew;
    ListView decouvert;
    ArrayAdapter<String> adapterFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Définir l'adapter des nouveaux devices
        newBluetoothDevices=new ArrayList<BluetoothDevice>();
        ListNew= new ArrayList<String>();
        decouvert= (ListView)findViewById(R.id.decouvert);
        adapterFound= new AdapterBluetooth(getApplicationContext(),ListNew);
        decouvert.setAdapter(adapterFound);

        bluetoothBroadCast= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action= intent.getAction();

                Toast.makeText(getApplicationContext(),"Dans le broadcast",Toast.LENGTH_SHORT).show();

                if(BluetoothDevice.ACTION_FOUND.equals(action)){
                    BluetoothDevice device= intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    newBluetoothDevices.add(device);
                    ListNew.add(device.getName());
                    adapterFound.notifyDataSetChanged();
                }
            }
        };

        decouvert.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device=null;
                TextView textView=(TextView) view.findViewById(R.id.name);
                Toast.makeText(getApplicationContext(),"On a pu cliquer",Toast.LENGTH_SHORT).show();

                for(BluetoothDevice identite : newBluetoothDevices){
                    if(identite.getName().equals(textView.getText())){
                        device=identite;
                        Toast.makeText(getApplicationContext(),"Le periphérique distant a ete retrouve",Toast.LENGTH_SHORT).show();
                        client=new ConnectedAsClientThread(device,getApplicationContext(),bluetoothAdapter,getApplication());
                        break;
                    }

                }
                client.start();
            }
        });

        //Enrégistrer le broadcast
        IntentFilter filtre= new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothBroadCast,filtre);

        //Enrégistrer le broadcast de connection
        IntentFilter connectionFiltre= new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        //registerReceiver(BondBroadCast,connectionFiltre);

        rechercher=(TextView)findViewById(R.id.rechercher);
        bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();

        List<String> mylist= new ArrayList<String>();
        listView= (ListView)findViewById(R.id.appare);

        final Set<BluetoothDevice> pairedDevices= bluetoothAdapter.getBondedDevices();
            if(pairedDevices.size()>0){
                for(BluetoothDevice identite : pairedDevices){
                    mylist.add(identite.getName());
                }
            }
            ArrayAdapter arrayAdapter= new AdapterBluetooth(getApplicationContext(),mylist);
            listView.setAdapter(arrayAdapter);

        rechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lancer la découverte
                bluetoothAdapter.startDiscovery();
                //On change le text de rechercher
                TextView CurrentTv=(TextView)v;
                CurrentTv.setText("Recherche en cours...");
                CurrentTv.setTextColor(getResources().getColor(R.color.gray));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device=null;
                TextView textView=(TextView) view.findViewById(R.id.name);
                Toast.makeText(getApplicationContext(),"On a pu cliquer",Toast.LENGTH_SHORT).show();

                for(BluetoothDevice identite : pairedDevices){
                    if(identite.getName().equals(textView.getText())){
                        device=identite;
                        Toast.makeText(getApplicationContext(),"Le periphérique distant a ete retrouve",Toast.LENGTH_SHORT).show();
                        client=new ConnectedAsClientThread(device,getApplicationContext(),bluetoothAdapter,getApplication());
                        break;
                    }

                }
                client.start();
            }
        });
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 0)
            return;
        if (resultCode == RESULT_OK) {
            // L'utilisation a activé le bluetooth
        } else {
            // L'utilisation n'a pas activé le bluetooth
        }
    }*/
    /*public void connectedClient(BluetoothDevice device,BluetoothAdapter btAdapter){
        final UUID mUUID=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        final BluetoothSocket btsocket;
        final BluetoothAdapter mBluetoothAdapter=btAdapter;

            BluetoothSocket tmp=null;
            try{
                tmp= device.createRfcommSocketToServiceRecord(mUUID);
            }catch (IOException e){

            }
            btsocket=tmp;

    Thread clientThread=new Thread(new Runnable() {
        @Override
        public void run() {
            mBluetoothAdapter.cancelDiscovery();
            try {
                btsocket.connect();
            } catch (Exception connectException) {
                try {
                    btsocket.close();
                } catch (Exception closeException) { }
                return;
            }

            if(btsocket!=null){
                try{
                    inStream= new BufferedInputStream(btsocket.getInputStream());
                    int text=inStream.read();
                    if(text==42)Toast.makeText(getApplicationContext(),"On a lu 42",Toast.LENGTH_SHORT).show();
                }catch (IOException e){
                    msg("On n'a pas recuperé le socket");
                }
            }
        }
    });
        clientThread.start();
    }

    public void msg(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }
        });
    }*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothAdapter.cancelDiscovery();
        unregisterReceiver(bluetoothBroadCast);
    }
}
