package it.unimi.wmn.battleship.model;

import java.util.InputMismatchException;
import java.util.Observable;

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
public class Field extends Observable {

    public final static String EMPTY = "Empty";
    public final static String BOAT = "Boat";
    public final static String HITBOAT = "HitBoat";
    public final static String SINK = "Sink";
    public final static String MISS = "Miss";
    private String Value;
    private Boat boat;

    /**
     * Class Constructor: set Field to Empty
     */
    public Field(){
        this.Value=Field.EMPTY;
    }

    /**
     *
     * @param Type
     * @throws AlreadyExistingBoatException
     */
    Field(String Type) throws AlreadyExistingBoatException {
        this.setField(Type);
    }

    /**
     * Get Field Type
     * @return Field
     */
    public String getField(){
        return this.Value;
    }

    /**
     * Set Field Type
     * Possible Field type are:Empty,Boat,HitBoat
     */
    public void setField(String Type) throws InputMismatchException {
        if(Type.equals(Field.EMPTY) || Type.equals(Field.BOAT) || Type.equals(Field.HITBOAT) || Type.equals(Field.SINK) || Type.equals(Field.MISS)){
            this.Value=Type;
            this.setChanged();
            this.notifyObservers();
        }else {
            throw new InputMismatchException("No valid Field Type");
        }
    }

    public void setBoat(Boat b){
        this.boat = b;
    }

    public Boat getBoat() throws RequestBoatOfEmptyFieldException {
        if(this.boat==null){
            throw new  RequestBoatOfEmptyFieldException("Request Boat of Empty Field");
        }
        return this.boat;
    }



}
