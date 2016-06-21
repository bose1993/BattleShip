package it.unimi.wmn.battleship.model;

import java.util.ArrayList;

/**
 * Created by ebosetti on 13/06/2016.
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
public class Boat {

    int cStart;
    int rStart;
    String state;

    public static final String HIT = "HIT";
    public static final String SINK = "SINK";
    public static final String OK = "OK";
    private ArrayList<Field> BoardFiled;
    private int BoatDim ;

    public int getId() {
        return id;
    }

    private int id;

    public Boat(){
        this.BoardFiled = new ArrayList<Field>();
        this.rStart= -1;
        this.cStart = -1;
        this.BoatDim = -1;
    }

    public void setBoatInfo(int r,int c,int dim,int id){
        this.rStart=r;
        this.cStart=c;
        this.BoatDim= dim;
        this.state= this.OK;
        this.id = id;
    }

    public void setField(Field f){
        this.BoardFiled.add(f);
    }

    public boolean checkIfSink(){
        if(this.BoatDim != -1) {
            int hittedNum = 0;
            for (int i = 0; i < this.BoardFiled.size(); i++) {
                if (this.BoardFiled.get(i).getField() == Field.HITBOAT || this.BoardFiled.get(i).getField() == Field.SINK) {
                    hittedNum++;
                }
            }
            if (hittedNum == this.BoatDim) {
                this.state = this.SINK;
                this.updateAllFieldToSink();
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    public void updateAllFieldToSink(){
        for (int i=0;i<this.BoardFiled.size();i++){
            this.BoardFiled.get(i).setField(Field.SINK);
        }
    }

    public void deleteBoat(){
        for (int i=0;i<this.BoardFiled.size();i++){
            this.BoardFiled.get(i).setField(Field.EMPTY);
        }
    }

}
