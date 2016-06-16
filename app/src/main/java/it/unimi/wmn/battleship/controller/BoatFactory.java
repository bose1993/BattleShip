package it.unimi.wmn.battleship.controller;

import java.util.Stack;
import java.util.UUID;

import it.unimi.wmn.battleship.model.AlreadyExistingBoatException;
import it.unimi.wmn.battleship.model.Boat;
import it.unimi.wmn.battleship.model.BoatOutOfFieldException;
import it.unimi.wmn.battleship.model.Field;

/**
 * Created by ebosetti on 15/06/2016.
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
public class BoatFactory {
    Stack<Integer> availableBoatDimension = new Stack<Integer>();
    GameBoard gb;

    public BoatFactory(){
        this.availableBoatDimension.push(2);
        this.availableBoatDimension.push(2);
        this.availableBoatDimension.push(2);
        this.availableBoatDimension.push(3);
        this.availableBoatDimension.push(3);
        this.availableBoatDimension.push(4);
        this.availableBoatDimension.push(5);
        this.gb = Game.getGameBoard();
    }

    public void createBoat(int row, int column) throws BoatOutOfFieldException, AlreadyExistingBoatException {
        if(!this.availableBoatDimension.empty()) {
            Integer dim = this.availableBoatDimension.pop();

            try {
                this.SetBoat(row, column, dim);
            } catch (AlreadyExistingBoatException e) {
                this.availableBoatDimension.push(dim);
                throw new AlreadyExistingBoatException("Overlapping Boat");            }

        }
    }

    private void SetBoat(int r, int c, int boatDim) throws BoatOutOfFieldException, AlreadyExistingBoatException {
        Boat b = new Boat();
        try {
            b.setBoatInfo(r, c, boatDim,BoatFactory.generateUniqueId());
            for (int i = 0; i < boatDim; i++) {
                if (!gb.getValueOfField(r,c+i, GameBoard.MYBOARD).equals(Field.EMPTY)){
                    throw new AlreadyExistingBoatException("Alredy Existing Boat in this Field");

                }
                gb.ChangeFieldStatus(r, c + i,Field.BOAT,b);
                b.setField( gb.getField(r,c+i,GameBoard.MYBOARD));
            }
        }catch (IndexOutOfBoundsException e){
            b.deleteBoat();
            throw new BoatOutOfFieldException("Boat is out of Field");
        }
        catch (AlreadyExistingBoatException e) {
            b.deleteBoat();
            throw new AlreadyExistingBoatException("Overlapping Boat");
        }
    }

    private static int generateUniqueId() {
        UUID idOne = UUID.randomUUID();
        String str=""+idOne;
        int uid=str.hashCode();
        String filterStr=""+uid;
        str=filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }


}
