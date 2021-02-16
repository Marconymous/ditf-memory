import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Game extends Application {
  private BorderPane root = new BorderPane();
  private GridPane grid = new GridPane();
  private StackPane stack = new StackPane();
  private Image back;
  private Tile[][] tiles = new Tile[4][6];
  private Image[] fronts = new Image[12];
  private ArrayList<Integer> usedImages = new ArrayList<>();
  private boolean isSecondCard = false;
  private Tile[] openTiles = new Tile[2];
  private Stage stage;
  private int moves = 0;
  private int highscore = Integer.MAX_VALUE;
  private boolean animationRunning = false;
  private Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), kf -> {
    for (Tile tile : openTiles) {
      tile.getImageView().setImage(back);
    }
  }));

  public Game() {
    try {
      FileInputStream input = new FileInputStream("resources/back.png");
      this.back = new Image(input);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    for (int i = 0; i < fronts.length; i++) {
      try {
        FileInputStream input = new FileInputStream("resources/front" + i + ".png");
        fronts[i] = new Image(input);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
    animation.setOnFinished(e -> animationRunning = false);
    animation.setCycleCount(1);
  }

  @Override
  public void start(Stage primaryStage) {
    stack.getChildren().add(grid);
    root.setCenter(stack);
    initCards();

    try {
      FileInputStream input = new FileInputStream("resources/icon.png");
      primaryStage.getIcons().add(new Image(input));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    this.stage = primaryStage;

    Scene scene = new Scene(root, 1190, 790);
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.setTitle("Memory");
    primaryStage.show();
  }

  private void initCards() {
    for (int y = 0; y < 4; y++) {
      for (int x = 0; x < 6; x++) {
        Tile t = new Tile();

        ImageView iv = new ImageView(back);
        iv.setFitHeight(200);
        iv.setFitWidth(200);
        int front;
        do {
          front = (int) (Math.random() * 12);
        } while (!isImageAvaivable(front));
        System.out.print(front + "; ");
        usedImages.add(front);
        t.setFront(fronts[front]);

        int finalX = x;
        int finalY = y;
        iv.setOnMouseClicked(event -> {
          if (animationRunning) return;
          System.out.printf("Tile pressed : %d, %d\n", finalY, finalX);
          if (!t.getImageView().getImage().equals(back)) return;
          iv.setImage(t.getFront());

          if (isSecondCard) openTiles[1] = t;
          else openTiles[0] = t;


          if (isSecondCard) {
            if (!isMatchingPair()) {
              animationRunning = true;
              animation.play();
            }
            moves++;
          }

          isSecondCard = !isSecondCard;

          if (isGameFinished()) {
            showWinScene();
          }
        });

        t.setImageView(iv);

        tiles[y][x] = t;
        grid.add(tiles[y][x].getImageView(), x, y);
      }
      System.out.println();
    }
  }

  private void showWinScene() {
    VBox b = new VBox();

    b.setStyle("-fx-background-color: #363d5c;");

    highscore = (highscore > moves) ? moves : highscore;

    Label won = new Label("You've Won!");
    won.setFont(Font.font(30));
    won.setTextFill(new Color(1, 1, 1, 1));
    Label moves = new Label("Moves : " + this.moves + "\t Best score : " + this.highscore);
    moves.setTextFill(new Color(1, 1, 1, 1));
    moves.setFont(Font.font(30));
    Button retry = new Button("Retry");
    retry.setStyle("-fx-background-color: #000000;");
    retry.setTextFill(new Color(1, 1, 1, 1));
    retry.setFont(Font.font(30));
    retry.setOnAction(e -> {
      this.moves = 0;
      root = new BorderPane();
      grid = new GridPane();
      stack = new StackPane();
      usedImages = new ArrayList<>();
      openTiles = new Tile[2];
      isSecondCard = false;
      start(stage);
    });

    b.getChildren().addAll(won, moves, retry);
    Scene winScene = new Scene(b, 1190, 790);
    stage.setScene(winScene);
  }

  private boolean isMatchingPair() {
    return openTiles[0].equals(openTiles[1]);
  }

  private boolean isImageAvaivable(int id) {
    int counter = 0;
    for (int i : usedImages) {
      if (i == id) counter++;
    }

    return counter < 2;
  }

  private boolean isGameFinished() {
    for (Tile[] ta : tiles) {
      for (Tile t : ta) {
        if (t.getImageView().getImage().equals(back)) return false;
      }
    }
    return true;
  }

  public static void main(String[] args) {
    launch(args);
  }
}
