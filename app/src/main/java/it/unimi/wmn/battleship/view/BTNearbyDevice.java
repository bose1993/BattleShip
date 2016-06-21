package it.unimi.wmn.battleship.view;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import it.unimi.wmn.battleship.R;
import it.unimi.wmn.battleship.controller.Constants;
import it.unimi.wmn.battleship.controller.Game;

public class BTNearbyDevice extends AppCompatActivity implements Observer {

    private ListView listView;
    private ArrayList<String> mDeviceList = new ArrayList<>();
    private Map<Integer,BluetoothDevice> deviceList = new TreeMap<>();
    private ArrayAdapter<String> listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btnearby_device);
        Game.getBluetoothWrapper().addObserver(this);
        listView = (ListView) findViewById(R.id.listView);
        Game.getBluetoothWrapper().setCtx(getApplicationContext());
        Game.getBluetoothWrapper().on(this);
        Game.getBluetoothWrapper().startDiscover();
        this.ensureDiscoverable();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        IntentFilter discoveryFinisced = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mDiscFinisched, discoveryFinisced);
        this.listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, mDeviceList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("BTNearbyDevice","Press"+position);
                Game.getBluetoothWrapper().pairDevice(deviceList.get(position));
            }
        });


    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        unregisterReceiver(mDiscFinisched);
        super.onDestroy();


    }

    private void ensureDiscoverable() {
        if (Game.getBluetoothWrapper().getAdapter().getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
    private void deviceDiscovered(BluetoothDevice bd){
        String deviceStatus;
        if(Game.getBluetoothWrapper().checkIfPaired(bd.getAddress())){
            deviceStatus = "Paired";
        }else{
            deviceStatus = "Not Paired";
        }
        String s = bd.getName() + "\n" + bd.getAddress();
        mDeviceList.add(s);
        this.deviceList.put(mDeviceList.indexOf(s), bd);

        this.listAdapter.notifyDataSetChanged();
        Log.i("BT", bd.getName() + "\n" + bd.getAddress());
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                final BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                deviceDiscovered(device);
            }
        }
    };

    private final BroadcastReceiver mDiscFinisched = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getApplicationContext(),"Discovery Process Finished", Toast.LENGTH_LONG).show();


        }
    };

    @Override
    public void update(Observable observable, Object data) {
        Log.d("BTNearby","Update From Controller");
        String typeUpdate = (String) data;
        if(data.equals(Constants.NEW_PAIRED_DEVICE)){
            this.mDeviceList.clear();
           for(Map.Entry<Integer,BluetoothDevice> entry : this.deviceList.entrySet()) {
                Integer key = entry.getKey();
                BluetoothDevice value = entry.getValue();
                this.deviceDiscovered(value);
            }
        }
    }
}
