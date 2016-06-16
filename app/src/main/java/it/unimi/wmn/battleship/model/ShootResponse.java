package it.unimi.wmn.battleship.model;

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
public class ShootResponse {

    private int row;
    private int column;
    private String  status;
    private int boatid;
    public static final String HIT = "HIT";
    public static final String MISS = "MISS";
    public static final String SINK = "SINK";

    public ShootResponse(int row, int column, String status, int boatid) {
        this.setRow(row);
        this.setColumn(column);
        this.setStatus(status);
        if(status.equals(ShootResponse.MISS)){
            this.boatid=-1;
        }else {
            this.boatid = boatid;
        }

    }
    public int getRow() {
        return row;
    }

    private void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    private void setColumn(int column) {
        this.column = column;
    }

    public String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    public int getBoatid() {
        return boatid;
    }


}
