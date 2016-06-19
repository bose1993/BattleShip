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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Set;

import it.unimi.wmn.battleship.model.BluetoothMessage;
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
    private static final String TAG = "BluetoothWrapper";


    private Context ctx;
    private BluetoothAdapter BA;
    private  final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Log.d(TAG,"Connected");
                            setChanged();
                            notifyObservers(Constants.CONNECTION_SUCCESFUL);
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
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //mConversationArrayAdapter.add("Me:  " + writeMessage);
                    Log.d("BTWrapperWrite",writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    reciveInfo(readBuf);
                    Log.d("BTWrapperRead",readBuf.toString());
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    //TODO
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
        Log.d(TAG,"Setting Up BTAdapter");
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
        IntentFilter intent = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        ctx.registerReceiver(mPairReceiver, intent);
    }
    public BluetoothService getBluetoothService() {
        if(this.BS==null){
            this.BS = new BluetoothService(ctx,mHandler);
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
                    notifyObservers(Constants.NEW_PAIRED_DEVICE);
                    Toast.makeText(ctx,"Paired", Toast.LENGTH_LONG).show();
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    Toast.makeText(ctx,"Unpaired", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    public void sendShootInfo(int r, int c) {
        BluetoothMessage m = new BluetoothMessage();
        m.setType(BluetoothMessage.SHOOT);
        m.setColumn(c);
        m.setRow(r);
        this.sendInfo(m);
    }

    public void sendShootResponseInfo(int c,int r, int status,int boatId){
        BluetoothMessage m = new BluetoothMessage();
        m.setType(BluetoothMessage.SHOOT_RESPONSE);
        m.setColumn(c);
        m.setRow(r);
        m.setShootStatus(status);
        m.setPayload(new Integer(boatId));
        this.sendInfo(m);
    }

    @Override
    public void sendInfo(BluetoothMessage m) {

        //TODO Make this method private & check where used
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(m);
            Log.d(TAG,"Sending Message");
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
                ex.printStackTrace();
            }
            try {
                bos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public BluetoothMessage reciveInfo(byte[] b) {
        Log.d(TAG,"Entering Method Recive Message");
        ByteArrayInputStream bis = new ByteArrayInputStream(b);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            BluetoothMessage o = (BluetoothMessage) in.readObject();
            Log.d(TAG,"MESSAGE TYPE"+String.valueOf(o.getType()));
            Log.d(TAG,String.valueOf(o.getPayload()));
            this.doActivityIncomingMessage(o);
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return null;
    }

    private void doActivityIncomingMessage(BluetoothMessage bm){
        if (bm.getType()==BluetoothMessage.DECIDE_FIRST_SHOOT){
            Game.getGameBoard().receiveNonce((Integer)(bm.getPayload()));
        }else if (bm.getType()==BluetoothMessage.SHOOT){
            Log.d(TAG,"Receive Shoot");
            Game.getGameBoard().receiveEnemyShoot(bm.getRow(),bm.getColumn());
        }else if (bm.getType()==BluetoothMessage.SHOOT_RESPONSE){
            Log.d(TAG,"Receive Shoot Repsonse");
            Game.getGameBoard().receiveEnemyShootResponse(new ShootResponse(bm.getRow(),bm.getColumn(),bm.getShootStatus(),(Integer)bm.getPayload()));
        }
    }
}
