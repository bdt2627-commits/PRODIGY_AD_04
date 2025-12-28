package com.example.tictacteo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    // 0: खाली, 1: Player 1 (X), 2: Player 2 (O)
    int[] gameState = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[][] winningPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // क्षैतिज (Horizontal)
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // लंबवत (Vertical)
            {0, 4, 8}, {2, 4, 6}             // विकर्ण (Diagonal)
    };

    boolean gameActive = true;
    int activePlayer = 1; // 1 = Player X, 2 = Player O
    int turnCount = 0;

    TextView statusTextView;
    EditText player1NameInput;
    EditText player2NameInput;
    String player1Name = "Player X";
    String player2Name = "Player O";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = findViewById(R.id.textViewStatus);
        player1NameInput = findViewById(R.id.player1Name);
        player2NameInput = findViewById(R.id.player2Name);

        updateStatus(player1Name + "'s Turn (X)");
    }

    // 💡 सेल क्लिक होने पर (On Cell Clicked)
    public void onCellClicked(View view) {
        if (!gameActive) {
            Toast.makeText(this, "Game Over. Reset to play again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. नाम अपडेट करें (Update names)
        player1Name = player1NameInput.getText().toString().trim().isEmpty() ? "Player X" : player1NameInput.getText().toString().trim();
        player2Name = player2NameInput.getText().toString().trim().isEmpty() ? "Player O" : player2NameInput.getText().toString().trim();

        Button tappedButton = (Button) view;
        int tappedButtonId = tappedButton.getId();

        // बटन की आईडी से gameState इंडेक्स निकालें (Determine index from Button ID)
        int gameStateIndex = getIndexFromId(tappedButtonId);

        // 2. देखें कि क्या सेल खाली है (Check if cell is empty)
        if (gameState[gameStateIndex] == 0) {
            gameState[gameStateIndex] = activePlayer;
            turnCount++;

            // 3. प्रतीक सेट करें (Set Symbol)
            if (activePlayer == 1) {
                tappedButton.setText("X");
            } else {
                tappedButton.setText("O");
            }

            // 4. जीत की जाँच करें (Check for Winner)
            if (checkForWin()) {
                String winnerName = (activePlayer == 1) ? player1Name : player2Name;
                updateStatus(winnerName + " has WON! 🎉");
                gameActive = false;
                return;
            }

            // 5. ड्रॉ की जाँच करें (Check for Draw)
            if (turnCount == 9) {
                updateStatus("It's a DRAW! 🤝");
                gameActive = false;
                return;
            }

            // 6. अगला खिलाड़ी (Next Player)
            activePlayer = (activePlayer == 1) ? 2 : 1;
            String nextPlayerName = (activePlayer == 1) ? player1Name : player2Name;
            updateStatus(nextPlayerName + "'s Turn (" + (activePlayer == 1 ? "X" : "O") + ")");

        } else {
            Toast.makeText(this, "Cell already taken.", Toast.LENGTH_SHORT).show();
        }
    }

    // 🏆 जीत की जाँच (Check for Win)
    private boolean checkForWin() {
        for (int[] winningPosition : winningPositions) {
            if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                    gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                    gameState[winningPosition[0]] != 0) {
                return true;
            }
        }
        return false;
    }

    // 🔄 गेम रीसेट करें (Reset Game)
    public void resetGame(View view) {
        gameActive = true;
        activePlayer = 1;
        turnCount = 0;

        // gameState को 0 पर रीसेट करें
        Arrays.fill(gameState, 0);

        // सभी बटन को खाली करें
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button" + i + j;
                @SuppressLint("DiscouragedApi") int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                Button button = findViewById(resID);
                if (button != null) {
                    button.setText("");
                }
            }
        }

        // खिलाड़ी के नाम को दोबारा अपडेट करें
        player1Name = player1NameInput.getText().toString().trim().isEmpty() ? "Player X" : player1NameInput.getText().toString().trim();
        player2Name = player2NameInput.getText().toString().trim().isEmpty() ? "Player O" : player2NameInput.getText().toString().trim();

        updateStatus(player1Name + "'s Turn (X)");
        Toast.makeText(this, "New Game Started!", Toast.LENGTH_SHORT).show();
    }

    // 🔔 स्टेटस अपडेट करने का फंक्शन (Update Status Function)
    private void updateStatus(String message) {
        statusTextView.setText(message);
    }

    // 🆔 बटन की आईडी से इंडेक्स निकालने का सहायक फंक्शन
    private int getIndexFromId(int id) {
        // यह लॉजिक बटन की ID (e.g., button00, button01, ..., button22)
        // को 0 से 8 के gameState इंडेक्स में मैप करता है।
        if (id == R.id.button00) return 0;
        if (id == R.id.button01) return 1;
        if (id == R.id.button02) return 2;
        if (id == R.id.button10) return 3;
        if (id == R.id.button11) return 4;
        if (id == R.id.button12) return 5;
        if (id == R.id.button20) return 6;
        if (id == R.id.button21) return 7;
        if (id == R.id.button22) return 8;
        return -1; // त्रुटि (Error)
    }
}