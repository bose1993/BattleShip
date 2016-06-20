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
    /**
     * This class provide a data structure for a bluetooth message exchanged bby the application
     */

    //Type of the message
    public static final int DECIDE_FIRST_SHOOT = 0;
    public static final int SHOOT = 1;
    public static final int SHOOT_RESPONSE = 2;
    public static final int NOTIFY_WIN = 3;

    private int type;
    private int column;
    private int row;
    private int shootStatus;
    private Object Payload;

    /**
     * Getter for the payload
     * @return Object
     */
    public Object getPayload() {
        return Payload;
    }

    /**
     * Setter for the payload
     * @param payload
     */
    public void setPayload(Object payload) {
        Payload = payload;
    }

    /**
     * Getter for the shoot status
     * @return int (Shoot Status)
     */

    public int getShootStatus() {
        return shootStatus;
    }

    /**
     * Setter for the shoot status
     * @param shootStatus
     */
    public void setShootStatus(int shootStatus) {
        this.shootStatus = shootStatus;
    }

    /**
     * Getter for row
     * @return int row
     */
    public int getRow() {
        return row;
    }

    /**
     * Setter for row
     * @param row
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Getter for column
     * @return int column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Setter for column
     * @param column
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Getter for type
     * @return int type
     */
    public int getType() {
        return type;
    }

    /**
     * Setter for type
     * @param type int
     */

    public void setType(int type) {
        this.type = type;
    }
}
