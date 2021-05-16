package com.prakriti.catordog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
// TIC TAC TOE

    // global vars exists thru the lifetime of the class / program
    enum Player {
        ONE, TWO, NONE
    }
    Player currentPlayer = Player.ONE;

        // to save the selected boxes for each player -> 0 to 8
    Player[] playerChoices = new Player[9]; // initialise to NONE at first

        // for winner - initialize with all possible winning combinations
    private int [][] winnerArray = {{0, 1, 2},
                            {3, 4, 5},
                            {6, 7, 8},
                            {0, 3, 6},
                            {1, 4, 7},
                            {2, 5, 8},
                            {0, 4, 8},
                            {2, 4, 6}};

    private boolean gameOver = false;
    private Button btnReset;
    private GridLayout myGrid; // import the class relevant to UI component used in XML file -> androidx.gridlayout.widget
    private TextView txtStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i = 0; i < playerChoices.length; i++) {
            playerChoices[i] = Player.NONE;
        }

        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        myGrid = findViewById(R.id.myGrid);
        txtStart = findViewById(R.id.txtStart);
    }


    public void imageIsTapped(View v) {
        ImageView tappedImage = (ImageView) v;

        int imageTag = Integer.parseInt(tappedImage.getTag().toString());
            // use getTag() and add tags to images starting from 0

        txtStart.animate().alpha(0).setDuration(500);

        // allow changing of images only if that position is empty/unoccupied by a player
        if(playerChoices[imageTag] == Player.NONE && gameOver == false) {
                // to disallow interaction once winner has been found

            btnReset.setVisibility(View.GONE); // game is not over so Reset is disabled
                // above line can also be put in resetGame() method

            playerChoices[imageTag] = currentPlayer;

            if (currentPlayer == Player.ONE) {
                // move image to X position off screen
                tappedImage.setTranslationX(-1000);
                // set img resource programatically
                tappedImage.setImageResource(R.drawable.dog);
                // specify animation to be shown for image
                tappedImage.animate().translationXBy(1000).alpha(1).rotation(1800).setDuration(500);
                // initially set alpha to be <1 for alpha(1) animation to work

                // next player's turn to play
                currentPlayer = Player.TWO;
            } else if (currentPlayer == Player.TWO) {
                tappedImage.setTranslationX(1000);
                tappedImage.setImageResource(R.drawable.cat);
                tappedImage.animate().translationXBy(-1000).alpha(1).rotation(1800).setDuration(500);

                currentPlayer = Player.ONE;
            }

            // to show image tag
            // Toast.makeText(this, tappedImage.getTag().toString(), Toast.LENGTH_SHORT).show();

            // check for winner using winnerArray() -> iterate over 2d array after each image is tapped
            for (int[] winnerColumns : winnerArray) {
                // avoid condition Player.NONE evaluating to true
                if (playerChoices[winnerColumns[0]] != Player.NONE &&
                        playerChoices[winnerColumns[0]] == playerChoices[winnerColumns[1]] &&
                        playerChoices[winnerColumns[1]] == playerChoices[winnerColumns[2]])
                {
                    btnReset.setVisibility(View.VISIBLE);
                    gameOver = true;
                    String winner = "";
                    // after tapping image, current player is switched
                    // so player opposite to current player has evaluated this condition to true and is the winner
                    if (currentPlayer == Player.ONE) {
                        winner = "PLAYER 2 is the Winner!";
                    } else if (currentPlayer == Player.TWO) {
                        winner = "PLAYER 1 is the Winner!";
                    }
                    Toast.makeText(this, winner, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // reset the game
    private void resetGame() {
        // grid layout is a view group containing image views as its children
            // iterate over all children of parent viewgroup
        for(int index = 0; index < myGrid.getChildCount(); index++) {
                // cast returned view to image view
            ImageView image = (ImageView) myGrid.getChildAt(index);
                // inital state as specified in XML file
            image.setImageDrawable(null);
            image.setAlpha(0.5f);
        }
            // set game to initial state
        currentPlayer = Player.ONE; // refers to global var
        for(int i = 0; i < playerChoices.length; i++) {
            playerChoices[i] = Player.NONE;
        }
        gameOver = false;
        txtStart.animate().alpha(1).setDuration(500);
    }
}