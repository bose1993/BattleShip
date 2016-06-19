package it.unimi.wmn.battleship.controller;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.TreeMap;

import it.unimi.wmn.battleship.model.Boat;
import it.unimi.wmn.battleship.model.Field;
import it.unimi.wmn.battleship.model.RequestBoatOfEmptyFieldException;
import it.unimi.wmn.battleship.model.ShootResponse;
import it.unimi.wmn.battleship.view.BattleBoard;

/**
 * Created by ebosetti on 09/06/2016.
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
public class GameBoard extends Observable {

    private static final String TAG = "GameBoard";

    public static final String ENEMYBOARD = "EB";
    public static final String MYBOARD = "MB";


    public static final int STATUS_BOAT_POSITIONING = 0;
    public static final int STATUS_DECIDE_FISRST_SHOOT = 1;
    public static final int STATUS_SHOOT = 2;
    public static final int STATUS_WAIT_SHOOT = 3;
    public static final int STATUS_WAITING_SHOOT_RESPONSE = 4;
    public static final int STATUS_GAME_ENDED = 5;

    private Field[][] Board;
    private Field[][] EmenyBoard;
    private ArrayList<Boat> BoatList;
    private Map<Integer, Boat> EnemyBoatList = new TreeMap<Integer, Boat>();

    private GameRoundManager grm;

    public int getStatus() {
        return status;
    }

    private int status;

    /**
     * Constructor of the class: Set the Gameboard to alla Empty
     */

     public GameBoard(){
         this.BoatList = new ArrayList<>();
         this.Board = new Field[10][10];
         this.grm = new GameRoundManager();
        for (int i=0;i<10;i++){
            for (int j=0;j<10;j++){
                // Set field to empty
                this.Board[i][j]= new Field();
            }
        }

         this.EmenyBoard = new Field[10][10];
         for (int i=0;i<10;i++){
             for (int j=0;j<10;j++){
                 // Set field to empty
                 this.EmenyBoard[i][j]= new Field();
             }
         }

         this.changeGameStatus(STATUS_BOAT_POSITIONING);

    }

    private void changeGameStatus(int status){
        Log.d(TAG,this.status+"-->"+status);
        this.status=status;
        this.setChanged();
        this.notifyObservers(Constants.GAME_STATUS_CHANGED);

    }

    public void ChangeFieldStatus(int r, int c, String state, Boat b){
        this.Board[r][c].setField(state);
        this.Board[r][c].setBoat(b);
    }

    public void allBoatPositioned(){
        this.changeGameStatus(STATUS_DECIDE_FISRST_SHOOT);
        this.grm.sendNonce();
        if (this.grm.existEnemyNonce()){
            //Check if enemy has just set all boat & send nonce or wait
            int round = this.grm.getRound();
            this.changeGameStatus(round);
        }
    }



    public void receiveNonce(int nonce){
        this.grm.setEnemyNonce(nonce);
        if (this.getStatus()==GameBoard.STATUS_DECIDE_FISRST_SHOOT) {
            //Check if all boat are positioned or wait the position of all boat to change the status
            int round = this.grm.getRound();
            this.changeGameStatus(round);
        }
    }

    public void sendShoot(int r, int c){
        if(this.status== GameBoard.STATUS_SHOOT){
            Game.getBluetoothWrapper().sendShootInfo(r,c);

        }else{
            this.changeGameStatus(GameBoard.STATUS_WAIT_SHOOT);
        }

    }

    private void sendShootResponse(ShootResponse sr){
        Game.getBluetoothWrapper().sendShootResponseInfo(sr.getColumn(),sr.getRow(),sr.getStatus(),sr.getBoatid());
        this.changeGameStatus(GameBoard.STATUS_SHOOT);
    }

    public void receiveEnemyShoot(int r, int c){
        ShootResponse sr;
        try {
            if(this.Board[r][c].getField().equals(Field.BOAT)){
                this.Board[r][c].setField(Field.HITBOAT);
                Boat b = this.Board[r][c].getBoat();
                if(b.checkIfSink()){
                    sr = new ShootResponse(r,c,ShootResponse.SINK,b.getId());
                }else{
                    sr = new ShootResponse(r,c,ShootResponse.HIT,b.getId());
                }
                this.sendShootResponse(sr);
            }else {
                sr = new ShootResponse(r, c, ShootResponse.MISS, -1);
            }
            this.sendShootResponse(sr);
        } catch (RequestBoatOfEmptyFieldException e) {
            e.printStackTrace();
        }
    }

    public void receiveEnemyShootResponse(ShootResponse sr){
        switch (sr.getStatus()) {
            case ShootResponse.HIT:
                if(this.EnemyBoatList.containsKey(sr.getBoatid())){
                    this.EnemyBoatList.get(sr.getBoatid()).setField(this.EmenyBoard[sr.getRow()][sr.getColumn()]);
                }else{
                    Boat b = new Boat();
                    b.setBoatInfo(sr.getRow(),sr.getColumn(),-1,sr.getBoatid());
                    b.setField(this.EmenyBoard[sr.getRow()][sr.getColumn()]);
                    this.EnemyBoatList.put(sr.getBoatid(),b);
                }
                this.EmenyBoard[sr.getRow()][sr.getColumn()].setField(Field.HITBOAT);
                break;
            case ShootResponse.SINK:
                this.EnemyBoatList.get(sr.getBoatid()).setField(this.EmenyBoard[sr.getRow()][sr.getColumn()]);
                this.EnemyBoatList.get(sr.getBoatid()).updateAllFieldToSink();
                this.EmenyBoard[sr.getRow()][sr.getColumn()].setField(Field.SINK);
                break;
            case ShootResponse.MISS:
                this.EmenyBoard[sr.getRow()][sr.getColumn()].setField(Field.MISS);
                break;

        }

        this.changeGameStatus(GameBoard.STATUS_WAIT_SHOOT);
    }

    public void insertObserverOfField(int row, int column, Observer o){
        this.Board[row][column].addObserver(o);
    }

    public void insertObserverOfEnemyField(int row, int column, Observer o){
        this.EmenyBoard[row][column].addObserver(o);
    }

    public String getValueOfField(int row, int column,String Type){
        if(Type.equals(GameBoard.MYBOARD)) {
            return this.Board[row][column].getField();
        }else{
            return this.EmenyBoard[row][column].getField();
        }
    }

    public Field getField(int row, int column, String Type){
        if(Type.equals(GameBoard.MYBOARD)) {
            return this.Board[row][column];
        }else{
            return this.EmenyBoard[row][column];
        }
    }
}