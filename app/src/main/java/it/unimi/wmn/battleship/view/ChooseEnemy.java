package it.unimi.wmn.battleship.view;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import it.unimi.wmn.battleship.R;
import it.unimi.wmn.battleship.controller.Game;

public class ChooseEnemy extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> deviceName;
    private ArrayList<BluetoothDevice> bdevices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_enemy);
        listView = (ListView) findViewById(R.id.listView);
        deviceName = new ArrayList<>();
        bdevices = new ArrayList<>();
        for (BluetoothDevice device : Game.getBluetoothWrapper().getPairedDevices()) {
            deviceName.add(device.getName());Log.d("ChooseEnemy",device.getName());
            this.bdevices.add(device);
        }
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, deviceName);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice bd = bdevices.get(position);
                Game.getBluetoothWrapper().getBluetoothService().connect(bd,true);
                Log.d("ChooseEnemy","Press"+position);
            }
        });
    }

}
