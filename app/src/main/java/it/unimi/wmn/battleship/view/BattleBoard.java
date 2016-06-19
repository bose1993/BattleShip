package it.unimi.wmn.battleship.view;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;


import it.unimi.wmn.battleship.R;
import it.unimi.wmn.battleship.controller.Game;
import it.unimi.wmn.battleship.controller.GameBoard;


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
    public class BattleBoard extends AppCompatActivity {
        /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    GridLayout gridLayout;
    private Game game;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            /*mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);*/
            //game = new Game();



        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_battle_board);
        this.CreateGridofBattleBoard();
        this.CreateEnemyGridBattleBoard();
        super.onCreate(savedInstanceState);
    }


    private void CreateEnemyGridBattleBoard(){
        GameBoard gb = Game.getGameBoard();  //Call singleton pattern GameBoard Object
        gridLayout = (GridLayout) findViewById(R.id.mBoardEnemy);
        gridLayout.removeAllViews();
        int buttons= 100;//the number of bottons i have to put in GridLayout
        int buttonsForEveryRow = 10; // buttons i can put inside every single row
        int buttonsForEveryRowAlreadyAddedInTheRow =0; // count the buttons added in a single rows
        int columnIndex=0; //cols index to which i add the button
        int rowIndex=0; //row index to which i add the button
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = (size.y/2)-25;
        for(int i=0; i < buttons;i++){
            if(buttonsForEveryRowAlreadyAddedInTheRow == buttonsForEveryRow ){
                rowIndex++; //here i increase the row index
                buttonsForEveryRowAlreadyAddedInTheRow  =0;
                columnIndex=0;
            }

            GridLayout.Spec row = GridLayout.spec(rowIndex,1);
            GridLayout.Spec colspan = GridLayout.spec(columnIndex,1);
            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, colspan);
            gridLayoutParam.width = width/buttonsForEveryRow;
            gridLayoutParam.height = height/(buttons/buttonsForEveryRow);
            BattleShipButtonEnemyBoard but = new BattleShipButtonEnemyBoard(rowIndex,columnIndex,BattleShipButtonMyBoard.ENEMYBOARD,this);
            gb.insertObserverOfEnemyField(rowIndex,columnIndex,but);
            gridLayout.addView(but,gridLayoutParam);

            buttonsForEveryRowAlreadyAddedInTheRow ++;
            columnIndex++;
        }

    }
    private void CreateGridofBattleBoard(){

        GameBoard gb = Game.getGameBoard();  //Call singleton pattern GameBoard Object
        mVisible = true;
        //mControlsView = findViewById(R.id.fullscreen_content_controls);
        //mContentView = findViewById(R.id.fullscreen_content);

        gridLayout = (GridLayout) findViewById(R.id.mBoard);
        gridLayout.removeAllViews();
        int buttons= 100;//the number of bottons i have to put in GridLayout
        int buttonsForEveryRow = 10; // buttons i can put inside every single row
        int buttonsForEveryRowAlreadyAddedInTheRow =0; // count the buttons added in a single rows
        int columnIndex=0; //cols index to which i add the button
        int rowIndex=0; //row index to which i add the button
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = (size.y/2)-25;
        for(int i=0; i < buttons;i++){
            if(buttonsForEveryRowAlreadyAddedInTheRow == buttonsForEveryRow ){
                rowIndex++; //here i increase the row index
                buttonsForEveryRowAlreadyAddedInTheRow  =0;
                columnIndex=0;
            }

            GridLayout.Spec row = GridLayout.spec(rowIndex,1);
            GridLayout.Spec colspan = GridLayout.spec(columnIndex,1);
            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, colspan);
            gridLayoutParam.width = width/buttonsForEveryRow;
            gridLayoutParam.height = height/(buttons/buttonsForEveryRow);
            BattleShipButtonMyBoard but = new BattleShipButtonMyBoard(rowIndex,columnIndex,BattleShipButtonMyBoard.MYBOARD,this);
            gb.insertObserverOfField(rowIndex,columnIndex,but);
            gridLayout.addView(but,gridLayoutParam);

            buttonsForEveryRowAlreadyAddedInTheRow ++;
            columnIndex++;
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        //mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
