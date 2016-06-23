package it.unimi.wmn.battleship.controller;

import android.util.Log;

import java.util.Random;

import it.unimi.wmn.battleship.model.BluetoothMessage;

/**
 * Created by ebosetti on 19/06/2016.
 * <p/>
 * Copyright (C) 2016  Università degli studi di Milano
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class GameRoundManager {
    public final static String TAG = "GameRounfManager";

    int MyNonce;
    int EnemyNonce;

    public GameRoundManager() {
        Random r = new Random();
        this.MyNonce = r.nextInt(65530);
        this.EnemyNonce= -1;
    }

    public void sendNonce(){
        BluetoothMessage m = new BluetoothMessage();
        m.setType(BluetoothMessage.DECIDE_FIRST_SHOOT);
        m.setPayload(this.MyNonce);
        //if(Game.getBluetoothWrapper().getBluetoothService().getState()== BluetoothService.STATE_CONNECTED){
            Game.getBluetoothWrapper().sendInfo(m);
        //}else{
          // Log.d(TAG,"Error Socket not connected status "+Game.getBluetoothWrapper().getBluetoothService().getState());
        //}
    }

    public void setEnemyNonce(int nonce){
        this.EnemyNonce=nonce;
    }

    public int getRound(){
        if(MyNonce>EnemyNonce){
            return GameBoard.STATUS_SHOOT;
        }else if(MyNonce<EnemyNonce){
            return GameBoard.STATUS_WAIT_SHOOT;
        }else {
            //TODO Implements case 2 number equals
            return 0;
        }
    }
    public boolean existEnemyNonce(){
        return this.EnemyNonce != -1;
    }
}
