package com.example.javafx_test2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Application {

    // The size of the grid
    private static final int SIZE = 4;

    // The colors of the tiles
    private static final Color[] COLORS = {
            Color.WHITE,
            Color.LIGHTGRAY,
            Color.LIGHTSALMON,
            Color.SALMON,
            Color.CORAL,
            Color.ORANGE,
            Color.DARKORANGE,
            Color.GOLD,
            Color.YELLOW,
            Color.LIGHTGREEN,
            Color.GREEN,
            Color.DARKGREEN,
            Color.LIGHTBLUE,
            Color.BLUE,
            Color.DARKBLUE,
            Color.PURPLE
    };

    // The font size of the numbers
    private static final int FONT_SIZE = 40;

    // The random number generator
    private Random random = new Random();

    // The grid of tiles
    private Tile[][] grid = new Tile[SIZE][SIZE];

    // The score label
    private Label scoreLabel = new Label("Score: 0");

    // The game over label
    private Label gameOverLabel = new Label("Game Over!");

    // The current score
    private int score = 0;

    // The flag to indicate if the game is over
    private boolean gameOver = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create the root pane
        GridPane root = new GridPane();

        // Add the score label to the top left corner
        root.add(scoreLabel, 0, 0);

        // Add the game over label to the center and hide it initially
        gameOverLabel.setFont(Font.font(FONT_SIZE * 2));
        gameOverLabel.setVisible(false);
        root.add(gameOverLabel, 0, 1, SIZE, SIZE);

        // Create and add the tiles to the grid
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = new Tile();
                root.add(grid[i][j], j, i + 1);
            }
        }

        // Add two random tiles to start the game
        addRandomTile();
        addRandomTile();

        // Set the scene and the stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("2048");
        primaryStage.show();

        // Handle the key events
        scene.setOnKeyPressed(e -> {
            if (!gameOver) {
                if (e.getCode() == KeyCode.UP) {
                    moveUp();
                } else if (e.getCode() == KeyCode.DOWN) {
                    moveDown();
                } else if (e.getCode() == KeyCode.LEFT) {
                    moveLeft();
                } else if (e.getCode() == KeyCode.RIGHT) {
                    moveRight();
                }
                updateUI();
            }
        });
    }

    // A helper method to update the UI after each move
    private void updateUI() {
        // Update the score label
        scoreLabel.setText("Score: " + score);

        // Check if the game is over
        gameOver = isGameOver();

        // Show or hide the game over label accordingly
        gameOverLabel.setVisible(gameOver);

        addRandomTile();
    }

    // A helper method to check if the game is over
    private boolean isGameOver() {
        // If there is any empty tile, the game is not over
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j].isEmpty()) {
                    return false;
                }
            }
        }

        // If there is any adjacent tile with the same value, the game is not over
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE - 1; j++) {
                if (grid[i][j].getValue() == grid[i][j + 1].getValue()) {
                    return false;
                }
            }
        }

        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE - 1; i++) {
                if (grid[i][j].getValue() == grid[i + 1][j].getValue()) {
                    return false;
                }
            }
        }

        // Otherwise, the game is over
        return true;
    }

    // A helper method to add a random tile to an empty spot
    private void addRandomTile() {
        // Get a list of empty tiles
        /*List<Tile> emptyTiles = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j].isEmpty()) {
                    emptyTiles.add(grid[i][j]);
                }
            }
        }*/

        if (isHasEmptyTile()) {
            System.out.println("in fun RandTitl, emptyTilesSize = " + getSizeEmptyTile());
            //int index = random.nextInt(getSizeEmptyTile());
            //System.out.println("index = " + index);
            //Tile tile = emptyTiles.get(index);
            Tile tile = new Tile();

            int value = random.nextInt(2) == 0 ? 2 : 4;
            addAndSetTile(getRandTo(getSizeEmptyTile()), value);

            //tile.setValue(helpTempVar);
        }
    }

        // If there is any empty tile, choose one randomly and set its value to 2 or 4
        /*if (!emptyTiles.isEmpty()) {
            System.out.println("in fun RandTitl, emptyTilesSize = " + emptyTiles.size());
            int index = random.nextInt(emptyTiles.size());
            System.out.println("index = " + index);
            //Tile tile = emptyTiles.get(index);
            Tile tile = new Tile();

            int helpTempVar = random.nextInt(2) == 0 ? 2 : 4;
            tile.setValue(helpTempVar);
        }*/



    // A helper method to move the tiles up
    private void moveUp() {
        // For each column, move the tiles up and merge them if possible
        for (int j = 0; j < SIZE; j++) {
            // A list to store the non-empty tiles in the column
            List<Tile> tiles = new ArrayList<>();

            // A flag to indicate if a merge has occurred in the previous step
            boolean merged = false;

            // Loop from top to bottom
            for (int i = 0; i < SIZE; i++) {
                // If the tile is not empty
                if (!grid[i][j].isEmpty()) {
                    // If the list is not empty and the last tile in the list has the same value as the current tile and no merge has occurred in the previous step
                    if (!tiles.isEmpty() && tiles.get(tiles.size() - 1).getValue() == grid[i][j].getValue() && !merged) {
                        // Double the value of the last tile in the list
                        tiles.get(tiles.size() - 1).setValue(tiles.get(tiles.size() - 1).getValue() * 2);

                        // Update the score
                        score += tiles.get(tiles.size() - 1).getValue();

                        // Set the merge flag to true
                        merged = true;
                    } else {
                        // Otherwise, add the current tile to the list
                        tiles.add(grid[i][j]);

                        // Set the merge flag to false
                        merged = false;
                    }
                }
            }

            // Fill the column with the tiles in the list from top to bottom and clear the remaining tiles
            for (int i = 0; i < SIZE; i++) {
                if (i < tiles.size()) {
                    grid[i][j] = tiles.get(i);
                } else {
                    grid[i][j] = new Tile();
                }
            }
        }

        // Add a random tile after each move
        addRandomTile();
    }

    // A helper method to move the tiles down
    private void moveDown() {
        // For each column, move the tiles down and merge them if possible
        for (int j = 0; j < SIZE; j++) {
            // A list to store the non-empty tiles in the column
            List<Tile> tiles = new ArrayList<>();

            // A flag to indicate if a merge has occurred in the previous step
            boolean merged = false;

            // Loop from bottom to top
            for (int i = SIZE - 1; i >= 0; i--) {
                // If the tile is not empty
                if (!grid[i][j].isEmpty()) {
                    // If the list is not empty and the last tile in the list has the same value as the current tile and no merge has occurred in the previous step
                    if (!tiles.isEmpty() && tiles.get(tiles.size() - 1).getValue() == grid[i][j].getValue() && !merged) {
                        // Double the value of the last tile in the list
                        tiles.get(tiles.size() - 1).setValue(tiles.get(tiles.size() - 1).getValue() * 2);

                        // Update the score
                        score += tiles.get(tiles.size() - 1).getValue();

                        // Set the merge flag to true
                        merged = true;
                    } else {
                        // Otherwise, add the current tile to the list
                        tiles.add(grid[i][j]);

                        // Set the merge flag to false
                        merged = false;                    }
                }
            }

            // Fill the column with the tiles in the list from bottom to top and clear the remaining tiles
            for (int i = SIZE - 1; i >= 0; i--) {
                if (i >= SIZE - tiles.size()) {
                    grid[i][j] = tiles.get(SIZE - 1 - i);
                } else {
                    grid[i][j] = new Tile();
                }
            }
        }

        // Add a random tile after each move
        addRandomTile();
    }

    // A helper method to move the tiles left
    private void moveLeft() {
        // For each row, move the tiles left and merge them if possible
        for (int i = 0; i < SIZE; i++) {
            // A list to store the non-empty tiles in the row
            List<Tile> tiles = new ArrayList<>();

            // A flag to indicate if a merge has occurred in the previous step
            boolean merged = false;

            // Loop from left to right
            for (int j = 0; j < SIZE; j++) {
                // If the tile is not empty
                if (!grid[i][j].isEmpty()) {
                    // If the list is not empty and the last tile in the list has the same value as the current tile and no merge has occurred in the previous step
                    if (!tiles.isEmpty() && tiles.get(tiles.size() - 1).getValue() == grid[i][j].getValue() && !merged) {
                        // Double the value of the last tile in the list
                        tiles.get(tiles.size() - 1).setValue(tiles.get(tiles.size() - 1).getValue() * 2);

                        // Update the score
                        score += tiles.get(tiles.size() - 1).getValue();

                        // Set the merge flag to true
                        merged = true;
                    } else {
                        // Otherwise, add the current tile to the list
                        tiles.add(grid[i][j]);

                        // Set the merge flag to false
                        merged = false;
                    }
                }
            }

            // Fill the row with the tiles in the list from left to right and clear the remaining tiles
            for (int j = 0; j < SIZE; j++) {
                if (j < tiles.size()) {
                    grid[i][j] = tiles.get(j);
                } else {
                    grid[i][j] = new Tile();
                }
            }
        }

        // Add a random tile after each move
        addRandomTile();
    }

    // A helper method to move the tiles right
    private void moveRight() {
        // For each row, move the tiles right and merge them if possible
        for (int i = 0; i < SIZE; i++) {
            // A list to store the non-empty tiles in the row
            List<Tile> tiles = new ArrayList<>();

            // A flag to indicate if a merge has occurred in the previous step
            boolean merged = false;

            // Loop from right to left
            for (int j = SIZE - 1; j >= 0; j--) {
                // If the tile is not empty
                if (!grid[i][j].isEmpty()) {
                    // If the list is not empty and the last tile in the list has the same value as the current tile and no merge has occurred in the previous step
                    if (!tiles.isEmpty() && tiles.get(tiles.size() - 1).getValue() == grid[i][j].getValue() && !merged) {
                        // Double the value of the last tile in the list
                        tiles.get(tiles.size() - 1).setValue(tiles.get(tiles.size() - 1).getValue() * 2);

                        // Update the score
                        score += tiles.get(tiles.size() - 1).getValue();

                        // Set the merge flag to true
                        merged = true;
                    } else {
                        // Otherwise, add the current tile to the list
                        tiles.add(grid[i][j]);

                        // Set the merge flag to false
                        merged = false;
                    }
                }
            }

            // Fill the row with the tiles in the list from right to left and clear the remaining tiles
            for (int j = SIZE - 1; j >= 0; j--) {
                if (j >= SIZE - tiles.size()) {
                    grid[i][j] = tiles.get(SIZE - 1 - j);
                } else {
                    grid[i][j] = new Tile();
                }
            }
        }

        // Add a random tile after each move
        addRandomTile();
    }

    public boolean isHasEmptyTile()
    {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j].isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getSizeEmptyTile()
    {
        int sum = 0;
        //List<Tile> emptyTiles = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j].isEmpty()) {
                    //emptyTiles.add(grid[i][j]);
                    sum = sum++;
                }
            }
        }

        return sum;
    }

    public void addAndSetTile(int index, int value)
    {
        System.out.println(index == 0 && grid[0][0].isEmpty());
        if(index == 0 && grid[0][0].isEmpty()) {
            System.out.println("vaAAA" + value);
            grid[0][0].setValue(value);
            System.out.println("Znachh" + grid[0][0]);
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j].isEmpty()) {
                    index = index - 1;
                    if(index == 0) {
                        System.out.println("vaAAA" + value);
                        grid[i][j].setValue(value);
                        System.out.println("Znachh" + grid[i][j]);
                    }
                }
            }
        }
    }

    public int getRandTo(int valueTo){
        int rand = (int) Math.floor(Math.random() * valueTo + 1);
        System.out.println("func getRand " + rand);
        return rand;
    }

    // A helper class to represent a tile
    private class Tile extends StackPane {

        // The rectangle of the tile
        private Rectangle rectangle = new Rectangle();

        // The label of the tile
        private Label label = new Label();

        // The value of the tile
        private int value;

        // The constructor of the tile
        public Tile() {
            // Set the size and color of the rectangle
            rectangle.setWidth(100);
            rectangle.setHeight(100);
            rectangle.setFill(COLORS[0]);
            rectangle.setStroke(Color.BLACK);

            // Set the font and color of the label
            label.setFont(Font.font(FONT_SIZE));
            label.setTextFill(Color.BLACK);

            // Add the rectangle and the label to the stack pane
            getChildren().addAll(rectangle, label);

            // Set the value of the tile to 0 (empty)
            value = 0;
            //setValue(0);
        }

        // A getter method for the value of the tile
        public int getValue() {
            return value;
        }

        // A setter method for the value of the tile
        public void setValue(int value) {
            this.value = value;

            // If the value is 0, hide the label
            if (value == 0) {
                label.setText("");
            } else {
                // Otherwise, show the label and set its text to the value
                label.setText(String.valueOf(value));
            }

            // Set the color of the rectangle according to the value
            System.out.println("value " + value);
            System.out.println("Math.log(value) " + Math.log(value));
            int index = (int) (Math.log(value) / Math.log(2));
            System.out.println("index " + index);
            System.out.println(COLORS[index]);
            rectangle.setFill(COLORS[index]);
        }

        // A helper method to check if the tile is empty
        public boolean isEmpty() {
            return value == 0;
        }
    }

    // The main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }
}
