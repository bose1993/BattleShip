package it.unimi.wmn.battleship.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import it.unimi.wmn.battleship.controller.Game;
import it.unimi.wmn.battleship.controller.GameBoard;
import it.unimi.wmn.battleship.model.AlreadyExistingBoatException;
import it.unimi.wmn.battleship.model.BoatOutOfFieldException;
import it.unimi.wmn.battleship.model.Field;

/**
 * Created by ebosetti on 14/06/2016.
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

public class BattleShipButtonEnemyBoard extends Button implements Observer,BattleButtonInterface {
    int row;
    int column;
    String type;
    Context ctx;

    BattleShipButtonEnemyBoard(final int row, final int column, String Type, Context ctx){
        super(ctx);
        this.row = row;
        this.column = column;
        this.type = Type;
        this.callModel();
        this.ctx = ctx;
        this.addListener(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        //When notify by Pattern Observer check status ok Field in Model
        //Log.d("BATTLEBUTTON","Notification From Model");
        this.callModel();
        //Get Instance from Singleton Class & update the view
    }

    @Override
    public void callModel(){
        GameBoard gb = Game.getGameBoard();
        String status = gb.getValueOfField(this.row,this.column,this.type);
        //Log.d("BATTLEBUTTON",status);
        switch (status) {
            case Field.SINK:
                this.setBackgroundResource(android.R.drawable.btn_default_small);
                this.setBackgroundColor(Color.RED);
                this.setOnClickListener(null);
                break;
            case Field.HITBOAT:
                this.setBackgroundResource(android.R.drawable.btn_default_small);
                this.setBackgroundColor(Color.YELLOW);
                this.setOnClickListener(null);
                break;
            case Field.BOAT:
                this.setBackgroundResource(android.R.drawable.btn_default_small);
                this.setBackgroundColor(Color.GREEN);
                break;
            case Field.EMPTY:
                this.setBackgroundResource(android.R.drawable.btn_default_small);
                break;
            case Field.MISS:
                this.setBackgroundColor(Color.BLUE);
                break;
        }
    }

    @Override
    public void addListener(Button b) {
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(type.equals(BattleShipButtonEnemyBoard.MYBOARD)){
                    try {
                        Game.getBoatFactory().createBoat(row,column);
                        Log.d("BATTLEBUTTON",row+ " "+column);
                    } catch (BoatOutOfFieldException | AlreadyExistingBoatException e) {
                        e.printStackTrace();
                    }
                }else if (type.equals(BattleShipButtonEnemyBoard.ENEMYBOARD)){
                    if(Game.getGameBoard().getStatus()==GameBoard.STATUS_SHOOT){
                        Game.getGameBoard().sendShoot(row,column);
                    }else if(Game.getGameBoard().getStatus()==GameBoard.STATUS_BOAT_POSITIONING) {
                        Toast.makeText(ctx,"Place all Boat first", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });
    }

}
