package it.unimi.wmn.battleship.controller;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.Observer;
import java.util.TreeMap;

import it.unimi.wmn.battleship.model.Boat;
import it.unimi.wmn.battleship.model.Field;
import it.unimi.wmn.battleship.model.RequestBoatOfEmptyFieldException;
import it.unimi.wmn.battleship.model.ShootResponse;

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
public class GameBoard {



    public static final String ENEMYBOARD = "EB";
    public static final String MYBOARD = "MB";

    Field[][] Board;
    Field[][] EmenyBoard;
    ArrayList<Boat> BoatList;
    Map<Integer, Boat> EnemyBoatList = new TreeMap<Integer, Boat>();


    /**
     * Constructor of the class: Set the Gameboard to alla Empty
     */

     public GameBoard(){
        Log.d("GameBoard","Board Contructor");
        this.BoatList = new ArrayList<>();
        this.Board = new Field[10][10];
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

    }

    public void ChangeFieldStatus(int r, int c, String state, Boat b){
        this.Board[r][c].setField(state);
        this.Board[r][c].setBoat(b);
    }


    public ShootResponse Shoot(int r, int c){
        if(this.Board[r][c].getField().equals(Field.BOAT)){
            this.Board[r][c].setField(Field.HITBOAT);
            try {
                Boat b = this.Board[r][c].getBoat();
                if(b.checkIfSink()){
                    return new ShootResponse(r,c,ShootResponse.SINK,b.getId());
                }else{
                    return new ShootResponse(r,c,ShootResponse.HIT,b.getId());
                }
            } catch (RequestBoatOfEmptyFieldException e) {
                e.printStackTrace();
            }
        }
        return new ShootResponse(r,c,ShootResponse.MISS,-1);
    }

    public void EnemyShootResponse(ShootResponse sr){
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
                this.EmenyBoard[sr.getRow()][sr.getColumn()].setField(Field.EMPTY);
                break;
        }
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