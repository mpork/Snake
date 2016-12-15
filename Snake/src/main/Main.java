package main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	public static int blocksize = 10;
	public static int WinWidth = 640;
	public static int WinHeight = 480;
	public static double speed = 0.1;

	public enum Direction {
		UP, DOWN, RIGHT, LEFT
	}
	
	//Declaring various variables, for future use
	public static Direction direction = Direction.RIGHT;
	public static boolean running = false;
	public static Timeline timeline = new Timeline();
	public static ObservableList<Node> snake;

	// Class, that has snake movement, food RNG and snake eating, growing
	public Parent game() {
		Pane root = new Pane();
		root.setPrefSize(WinWidth, WinHeight);

		Group snakeBody = new Group();
		snake = snakeBody.getChildren();

		// Declaring food, random location in the game
		Rectangle food = new Rectangle(blocksize, blocksize);
		Methods.foodRandom(food);

		// Will update frames after specific time in seconds, using variable speed
		KeyFrame frame = new KeyFrame(Duration.seconds(speed), event -> {
			if (!running)
				return;
			
			
			boolean toRemove = snake.size() > 1;
			Node tail = toRemove ? snake.remove(snake.size() - 1) : snake.get(0);

			// Saving the tail's last position
			double tailX = tail.getTranslateX();
			double tailY = tail.getTranslateY();

			// Implementing movement
			switch (direction) {
			case UP:
				tail.setTranslateX(snake.get(0).getTranslateX());
				tail.setTranslateY(snake.get(0).getTranslateY() - blocksize);
				break;
			case DOWN:
				tail.setTranslateX(snake.get(0).getTranslateX());
				tail.setTranslateY(snake.get(0).getTranslateY() + blocksize);
				break;
			case RIGHT:
				tail.setTranslateX(snake.get(0).getTranslateX() + blocksize);
				tail.setTranslateY(snake.get(0).getTranslateY());
				break;
			case LEFT:
				tail.setTranslateX(snake.get(0).getTranslateX() - blocksize);
				tail.setTranslateY(snake.get(0).getTranslateY());
				break;
			}

			// Add tail as first to the snake list
			if (toRemove)
				snake.add(0, tail);

			// Check collision with walls and itself
			Methods.collision(tail);

			// Check if snake is on the same position as food, if it is,
			// generate new food position and add one block to the tail
			if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {
				Methods.foodRandom(food);
				Methods.addLength(tailX, tailY);
			}

		});

		// Add new frame to the timeline, which all together makes the animation
		timeline.getKeyFrames().add(frame);
		timeline.setCycleCount(Timeline.INDEFINITE);

		root.getChildren().addAll(food, snakeBody);

		return root;
	}

	//The game area
	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(game());
		scene.setFill(Color.BLACK);

		keyPressed(scene);

		primaryStage.setTitle("Snake Game 2016");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
		Methods.startGame();

	}
	
	public void keyPressed(Scene scene) {
		scene.setOnKeyPressed(event -> {

			switch (event.getCode()) {
			case W:
				if (direction != Direction.DOWN)
					direction = Direction.UP;
				break;
			case A:
				if (direction != Direction.RIGHT)
					direction = Direction.LEFT;
				break;
			case S:
				if (direction != Direction.UP)
					direction = Direction.DOWN;
				break;
			case D:
				if (direction != Direction.LEFT)
					direction = Direction.RIGHT;
				break;

			}

		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}