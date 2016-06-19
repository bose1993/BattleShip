package it.unimi.wmn.battleship.controller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Set;

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
public class BluetoothWrapper extends Observable implements BattleshipComunicationWrapper {
    public static final Object NEW_PAIRED_DEVICE = "NEW_PAIRED_DEVICE";
    private Context ctx;
    private BluetoothAdapter BA;
    private  final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            //setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            //mConversationArrayAdapter.clear();
                            sendShootInfo(1,1);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            //setStatus(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            //setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //mConversationArrayAdapter.add("Me:  " + writeMessage);
                    Log.d("BTWrapperRead",writeMessage);
                    break;
                case BluetoothService.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.d("BTWrapperRead",readMessage);
                    break;
                case BluetoothService.MESSAGE_DEVICE_NAME:
                    //TODO
                case BluetoothService.UI_MESSAGE:
                    //TODO
                    break;
            }
        }
    };

    public Set<BluetoothDevice> getPairedDevices() {
        return pairedDevices;
    }

    Set<BluetoothDevice> pairedDevices;
    private BluetoothService BS;

    public BluetoothWrapper() {
        this.BA = BluetoothAdapter.getDefaultAdapter();
        this.updatePairedDevice();
        Log.d("BTWrapper","Setting Up BTAdapter");
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
        IntentFilter intent = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        ctx.registerReceiver(mPairReceiver, intent);
    }
    public BluetoothService getBluetoothService() {
        if(this.BS==null){
            this.BS = new BluetoothService(mHandler);
        }
        return BS;
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

    public BluetoothAdapter getAdapter(){
        return this.BA;
    }

    private void updatePairedDevice(){
        this.pairedDevices = this.BA.getBondedDevices();
    }

    public boolean checkIfPaired(String address){
        for (BluetoothDevice device : this.pairedDevices) {
            if(device.getAddress().equals(address)){
                return true;
            }
        }
        return false;
    }


    public boolean isEnable(){
        return this.BA.isEnabled();
    }

    public void startDiscover(){
        this.BA.startDiscovery();
    }

    public void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);
                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    updatePairedDevice();
                    notifyObservers(BluetoothWrapper.NEW_PAIRED_DEVICE);
                    Toast.makeText(ctx,"Paired", Toast.LENGTH_LONG).show();
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    Toast.makeText(ctx,"Unpaired", Toast.LENGTH_LONG).show();
                }
            }
        }
    };



    @Override
    public void sendShootInfo(int r, int c) {
        Log.d("BTWrapper","Sending Shoot Info");
        //this.reciveShootInfo(Game.getGameBoard().Shoot(r,c));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject("Test");
            byte[] BytesObj = bos.toByteArray();
            this.getBluetoothService().write(BytesObj);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    @Override
    public void reciveShootInfo(ShootResponse sr) {
        Game.getGameBoard().EnemyShootResponse(sr);


    }
}
