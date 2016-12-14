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
	public static int WinHeight = 640;
	public static int WinWidth = 480;
	public static double speed = 0.1;

	public enum Direction {
		UP, DOWN, RIGHT, LEFT
	}

	private Direction direction = Direction.RIGHT; // The default movement is to
													// the right
	// private boolean moved = false; // Boolean that states if the snake has
	// moved already or not

	private boolean running = false; // Boolean that states if the game is
										// running

	private Timeline timeline = new Timeline(); // For animation, declaring a
												// timeline
	private ObservableList<Node> snake; // A list called snake, to add length to
										// it during the game

	// The main play area

	private Parent game() {
		Pane root = new Pane();
		root.setPrefSize(WinHeight, WinWidth);

		Group snakeBody = new Group();
		snake = snakeBody.getChildren();

		// Declaring food, random location in the game
		Rectangle food = new Rectangle(blocksize, blocksize);
		food.setTranslateX((int) (Math.random() * WinHeight)/blocksize*blocksize);
		food.setTranslateY((int) (Math.random() * WinWidth)/blocksize*blocksize);
		food.setFill(Color.WHITE);
		
		// Will update frames after every 0.2 seconds
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

			// moved = true;

			if (toRemove)
				snake.add(0, tail);

			// Check collision with borders
			if (tail.getTranslateX() < 0 || tail.getTranslateX() >= root.getWidth() || tail.getTranslateY() < 0
					|| tail.getTranslateY() >= root.getHeight())
				restartGame();

			// Check if snake is on the same position as food, if it is,
			// generate new food position and add one block to the tail
			if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {
				food.setTranslateX((int) (Math.random() * WinHeight) / blocksize*blocksize);
				food.setTranslateY((int) (Math.random() * WinWidth) / blocksize*blocksize);

				Rectangle extra = new Rectangle(blocksize, blocksize);
				extra.setTranslateX(tailX);
				extra.setTranslateY(tailY);
				extra.setFill(Color.WHITE);

				snake.add(extra);
			}

		});

		// Add new frame to the timeline, which all together makes the animation
		timeline.getKeyFrames().add(frame);
		timeline.setCycleCount(Timeline.INDEFINITE);

		root.getChildren().addAll(food, snakeBody);
		
		return root;
	}

	private void stopGame() {
		running = false;
		timeline.stop();
		snake.clear();
	}

	private void startGame() {
		direction = Direction.RIGHT;
		Rectangle head = new Rectangle(blocksize, blocksize);
		head.setTranslateX(0);
		head.setTranslateY(0);
		head.setFill(Color.WHITE);
		snake.add(head);
		timeline.play();
		running = true;
	}

	private void restartGame() {
		stopGame();
		startGame();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(game());
		scene.setFill(Color.BLACK);

		scene.setOnKeyPressed(event -> {
			// if (!moved)
			// return;

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

		primaryStage.setTitle("Snake Game 2016");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
		startGame();

	}

	public static void main(String[] args) {
		launch(args);
	}
}