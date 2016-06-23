package it.unimi.wmn.battleship.controller;

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
public class Game {
    private static GameBoard gb = null;
    private static BoatFactory bf = null;
    private static BluetoothWrapper br = null;


    /**
     * Singleton class
     * @return Gamboard
     */
    public static GameBoard getGameBoard(){
        if(gb == null){
            gb = new GameBoard();
        }
        return gb;
    }

    public static BoatFactory getBoatFactory(){
        if(bf == null){
            bf = new BoatFactory();
        }
        return bf;
    }

    public static BluetoothWrapper getBluetoothWrapper(){
        if(br == null){
            br = new BluetoothWrapper();
        }
        return br;
    }

    public static void resetGame(){
        br.getBluetoothService().stop();
        gb = null;
        bf = null;
        br = null;
    }

}
