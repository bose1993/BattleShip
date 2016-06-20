package it.unimi.wmn.battleship.view;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import it.unimi.wmn.battleship.R;
import it.unimi.wmn.battleship.controller.BluetoothWrapper;
import it.unimi.wmn.battleship.controller.Constants;
import it.unimi.wmn.battleship.controller.Game;

public class ChooseEnemy extends AppCompatActivity implements Observer {

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
        Game.getBluetoothWrapper().addObserver(this);
        for (BluetoothDevice device : Game.getBluetoothWrapper().getPairedDevices()) {
            deviceName.add(device.getName());Log.d("ChooseEnemy",device.getName());
            this.bdevices.add(device);
        }
        ArrayAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceName);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice bd = bdevices.get(position);
                Game.getBluetoothWrapper().getBluetoothService().connect(bd,false);
                Log.d("ChooseEnemy","Press"+position);
            }
        });
    }

    private void startGameClientRole(){
        Intent intent = new Intent(getApplicationContext(),BattleBoard.class);
        startActivity(intent);
    }

    @Override
    public void update(Observable observable, Object data) {
        if(data.equals(Constants.CONNECTION_SUCCESFUL)){
            startGameClientRole();
        }
    }

}
