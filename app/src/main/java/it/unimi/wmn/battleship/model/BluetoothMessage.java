package it.unimi.wmn.battleship.model;

import java.io.Serializable;

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
public class BluetoothMessage implements Serializable {
    public static final int DECIDE_FIRST_SHOOT = 0;
    public static final int SHOOT = 1;
    public static final int SHOOT_RESPONSE = 2;


    private int type;
    private int column;
    private int row;
    private int shootStatus;
    private Object Payload;

    public Object getPayload() {
        return Payload;
    }

    public void setPayload(Object payload) {
        Payload = payload;
    }

    public int getShootStatus() {
        return shootStatus;
    }

    public void setShootStatus(int shootStatus) {
        this.shootStatus = shootStatus;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
