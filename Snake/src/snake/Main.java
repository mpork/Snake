package snake;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	public static int blocksize = 10;
	public static int WinWidth = 640;
	public static int WinHeight = 480;
	public double speed = 0.1;
	static Font font = Font.font(24);

	public enum Direction {
		UP, DOWN, RIGHT, LEFT
	}

	// Declaring various variables, for future use
	public static Direction direction = Direction.RIGHT;
	public static boolean running = false;
	public boolean moved = false;
	public static Timeline timeline = new Timeline();
	public static ObservableList<Node> snake;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setResizable(false);
		primaryStage.setTitle("Snake Game 2016");

		// Label lblContinue = new Label("Continue");
		// lblContinue.setFont(font);

		Button btnStart = new Button("New Game");
		btnStart.setFont(font);
		btnStart.setOnAction(event -> {
			try {
				game(primaryStage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		Button btnControls = new Button("Controls");
		btnControls.setFont(font);
		btnControls.setOnAction(event -> {
			try {
				options(primaryStage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		Button btnExit = new Button("Exit");
		btnExit.setFont(font);
		btnExit.setOnAction(event -> Platform.exit());

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(20);
		GridPane.setHalignment(btnControls, HPos.CENTER);
		GridPane.setValignment(btnControls, VPos.CENTER);
		GridPane.setHalignment(btnExit, HPos.CENTER);
		GridPane.setValignment(btnExit, VPos.CENTER);

		grid.add(btnStart, 1, 1);
		grid.add(btnControls, 1, 2);
		grid.add(btnExit, 1, 3);

		Scene scene = new Scene(grid, 640, 480);
		background();

		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public void options(Stage primaryStage) throws Exception {
		Font font = Font.font(24);

		Button btnDiff = new Button("Difficulty");
		btnDiff.setFont(font);
		btnDiff.setOnAction(event -> {

		});

		Button btnControls = new Button("Controls");
		btnControls.setFont(font);
		// btnControls.setOnAction(event -> options());

		Button btnBack = new Button("Back");
		btnBack.setFont(font);
		btnBack.setOnAction(event -> {
			try {
				start(primaryStage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(20);
		GridPane.setHalignment(btnControls, HPos.CENTER);
		GridPane.setValignment(btnControls, VPos.CENTER);
		GridPane.setHalignment(btnBack, HPos.CENTER);
		GridPane.setValignment(btnBack, VPos.CENTER);

		grid.add(btnDiff, 2, 5);
		grid.add(btnControls, 2, 1);
		grid.add(btnBack, 2, 6);

		Scene scene = new Scene(grid, 640, 480);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// Class, that has snake movement, food RNG and snake eating, growing
	public Parent game() {
		Pane root = new Pane();
		root.setPrefSize(WinWidth, WinHeight);

		Group snakeBody = new Group();
		snake = snakeBody.getChildren();

		// Declaring food, random location in the game
		Rectangle food = new Rectangle(blocksize, blocksize);
		Methods.foodRandom(food);

		// Will update frames after specific time in seconds, using variable
		// speed
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

			moved = true;

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

		root.getChildren().addAll(background(), food, snakeBody);

		return root;
	}

	// The game area

	public void game(Stage gameStage) throws Exception {
		Scene scene = new Scene(game());
		keyPressed(scene);
		gameStage.setScene(scene);
		gameStage.show();
		startGame();

	}

	//
	public void keyPressed(Scene scene) {
		scene.setOnKeyPressed(event -> {
			if (!moved)
				return;
			switch (event.getCode()) {
			case W:
			case UP:
				if (direction != Direction.DOWN)
					direction = Direction.UP;
				break;
			case A:
			case LEFT:
				if (direction != Direction.RIGHT)
					direction = Direction.LEFT;
				break;
			case S:
			case DOWN:
				if (direction != Direction.UP)
					direction = Direction.DOWN;
				break;
			case D:
			case RIGHT:
				if (direction != Direction.LEFT)
					direction = Direction.RIGHT;
				break;
			case SPACE:
				timeline.pause();
				scene.setOnKeyPressed(event1 -> {
					switch (event1.getCode()) {
					case SPACE:
						timeline.play();
						keyPressed(scene);
						break;
					case ESCAPE:
						running = false;
						timeline.stop();
						break;
					default:
						break;
					}
				});
				break;
			default:
				break;

			}
			moved = false;
		});
	}

	// Method that stops the game, stopping timeline and clearing the snake
	public static void stopGame() {
		running = false;
		timeline.stop();
		snake.clear();

	}

	// Starts game, setting default values to variables
	public static void startGame() {
		direction = Direction.RIGHT;
		Rectangle tail = new Rectangle(blocksize, blocksize);
		tail.setFill(Color.WHITE);

		// tail.setFill(Color.WHITE);
		snake.add(tail);

		timeline.play();
		running = true;
	}

	// Restarts game, after colliding, using 2 previous methods
	public static void restartGame() {
		stopGame();
		startGame();

	}

	// Using it to make the background black
	public Rectangle background() {
		Rectangle bg = new Rectangle(WinWidth, WinHeight);
		bg.setFill(Color.BLACK);
		return bg;
	}

	// public void pauseGame(Scene scene) {
	// Scene scene = this.scene;
	// Label lblContinue = new Label("Continue");
	// lblContinue.setFont(font);
	// scene.getChildren().Add(lblContinue);
	//
	// }

	public static void main(String[] args) {
		launch(args);
	}
}