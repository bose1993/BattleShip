package it.unimi.wmn.battleship.controller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import it.unimi.wmn.battleship.model.ShootResponse;

/**
 * Created by ebosetti on 16/06/2016.
 *
 * Copyright (C) 2016  Università degli studi di Milano
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class BluetoothWrapper implements BattleshipComunicationWrapper {
    private Context ctx;
    private BluetoothAdapter BA;

    public BluetoothWrapper() {
        this.BA = BluetoothAdapter.getDefaultAdapter();
        Log.d("BTWrapper","Setting Up BTAdapter");
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public void on(Activity a){
        if (!this.BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            a.startActivityForResult(turnOn, 0);
            Toast.makeText(ctx," BT Turned on",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(ctx,"BT Already on", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void sendShootInfo(int r, int c) {
        this.reciveShootInfo(Game.getGameBoard().Shoot(r,c));
    }

    @Override
    public void reciveShootInfo(ShootResponse sr) {
        Game.getGameBoard().EnemyShootResponse(sr);
    }
}
