package it.unimi.wmn.battleship.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;

import it.unimi.wmn.battleship.R;
import it.unimi.wmn.battleship.controller.BluetoothService;
import it.unimi.wmn.battleship.controller.BluetoothWrapper;
import it.unimi.wmn.battleship.controller.Constants;
import it.unimi.wmn.battleship.controller.Game;

public class Menu extends AppCompatActivity implements Observer {
    private Button playBtn;
    private Button ServerBtn;
    private Button pairBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Game.getBluetoothWrapper().addObserver(this);
        this.playBtn = (Button)findViewById(R.id.Play);
        this.playBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseEnemy.class);
                startActivity(intent);            }
        });

        this.ServerBtn = (Button)findViewById(R.id.StartServer);
        this.ServerBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Game.getBluetoothWrapper().getBluetoothService().getState()== BluetoothService.STATE_NONE){
                    ServerBtn.setText("Stop Play Server");
                    Game.getBluetoothWrapper().getBluetoothService().start();
                }else{
                    ServerBtn.setText("Start a Play Server");
                    Game.getBluetoothWrapper().getBluetoothService().stop();
                }
            }

        });

        this.pairBtn = (Button)findViewById(R.id.Find);
        this.pairBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("MENU","Click on Pair new device");
                Intent intent = new Intent(getApplicationContext(), BTNearbyDevice.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public void update(Observable observable, Object data) {
        if(data.equals(Constants.CONNECTION_SUCCESFUL)){
            startGameServerRole();
        }
    }

    private void startGameServerRole(){
        Intent intent = new Intent(getApplicationContext(),BattleBoard.class);
        startActivity(intent);
    }
}
