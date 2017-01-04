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
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Snake game, hardwork did pay off, game works Did use some help from
 * https://www.youtube.com/watch?v=nK6l1uVlunc
 * 
 * @author Siim Oselein
 * @version %I%, %G%
 */
public class Main extends Application {
	
	/**
	 * Making it unified so that all parts are the same size
	 */
	public static int blocksize = 10;
	public static int WinWidth = 640;
	public static int WinHeight = 480;
	public double speed = 0.1;
	Font font = Font.font(24);

	public enum Direction {
		/**
		 * Moving up
		 */
		UP, DOWN, RIGHT, LEFT
	}

	/**
	 * Declaring various variables for future use
	 */
	public static Direction direction = Direction.RIGHT;
	public static boolean running = false;
	public boolean moved = false;
	public static Timeline timeline = new Timeline();
	public static ObservableList<Node> snake;
	String Difficulty = new String("Medium");
	String newSpeed = new String(Double.toString(speed));

	/**
	 * Starts up the whole game, setting some constants
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setResizable(false);
		primaryStage.setTitle("Snake Game 2016");

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

		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * Options menu to see controls and change difficulty
	 * 
	 * @param primaryStage
	 *            The whole window, adding scenes to it
	 * @throws Exception
	 *             Just in case going back doesn't work
	 */
	public void options(Stage primaryStage) throws Exception {
		Label lblDiff = new Label(Difficulty);
		lblDiff.setFont(font);

		Button btnDiff = new Button("Difficulty");
		btnDiff.setFont(font);
		btnDiff.setOnAction(event -> {
			switch (newSpeed) {
			case "0.1":
				speed = 0.07;
				Difficulty = "Hard";
				lblDiff.setText(Difficulty);
				newSpeed = Double.toString(speed);
				break;
			case "0.07":
				speed = 0.15;
				Difficulty = "Easy";
				lblDiff.setText(Difficulty);
				newSpeed = Double.toString(speed);
				break;
			case "0.15":
				speed = 0.1;
				Difficulty = "Medium";
				lblDiff.setText(Difficulty);
				newSpeed = Double.toString(speed);
				break;
			default:
				break;
			}
		});

		Text txtControls = new Text("Controls");
		txtControls.setFont(font);
		Text txtCtrlDesc = new Text("Move up:\nMove down:\nMove left:\nMove right:\nPause:\nReturn to menu");
		txtCtrlDesc.setFont(font);
		Text txtCtrlKeys = new Text("W; UP\nS; DOWN\nA; LEFT\nD; RIGHT\nSPACE\nESC");
		txtCtrlKeys.setFont(font);

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

		grid.add(txtControls, 2, 1);
		grid.add(txtCtrlDesc, 1, 2);
		grid.add(txtCtrlKeys, 3, 2);
		grid.add(btnDiff, 1, 3);
		grid.add(lblDiff, 3, 3);
		grid.add(btnBack, 2, 4);

		Scene scene = new Scene(grid, 640, 480);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Whole game's logic controls movement, food and snake's placement
	 * 
	 * @return Pane root, that shows the snake and food
	 */
	public Parent game() {

		Pane root = new Pane();
		root.setPrefSize(WinWidth, WinHeight);

		Group snakeBody = new Group();
		snake = snakeBody.getChildren();

		/**
		 * Creating Rectangle food, calling foodRandom() method
		 */
		Rectangle food = new Rectangle(blocksize, blocksize);
		Methods.foodRandom(food);

		/**
		 * Will generate new frame after specific time in seconds, using
		 * variable speed
		 */
		KeyFrame frame = new KeyFrame(Duration.seconds(speed), event -> {
			if (!running)
				return;

			boolean toRemove = snake.size() > 1;
			Node tail = toRemove ? snake.remove(snake.size() - 1) : snake.get(0);

			/**
			 * Saving tail's last position to add new piece there if necessary
			 */
			double tailX = tail.getTranslateX();
			double tailY = tail.getTranslateY();

			/**
			 * Depending on where the direction is, adding or taking away
			 * blocksize from coordinates
			 */
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

			/**
			 * Adding tail as first to the snake list, making it basically the
			 * head
			 */
			if (toRemove)
				snake.add(0, tail);

			/**
			 * Calling method collision()
			 */
			Methods.collision(tail);

			/**
			 * Calling method foodEating()
			 */
			Methods.foodEating(tail, food, tailX, tailY);

		});

		/**
		 * Adding new frame to timeline, making the cycle indefinite, breakers
		 * are elsewhere
		 */
		timeline.getKeyFrames().add(frame);
		timeline.setCycleCount(Timeline.INDEFINITE);

		root.getChildren().addAll(background(), food, snakeBody);

		return root;
	}

	/**
	 * Sets the prerequisites to start the game scene, gets called out when new
	 * game button is pressed
	 * 
	 * @param gameStage
	 */
	public void game(Stage gameStage) {
		Scene scene = new Scene(game());
		keyPressed(scene, gameStage);
		gameStage.setScene(scene);
		gameStage.show();
		startGame();

	}

	/**
	 * Declaring different keypresses and their actions
	 * @param scene
	 *            The current scene that is active
	 * @param primaryStage
	 *            The whole window, adding scenes to it
	 */
	public void keyPressed(Scene scene, Stage primaryStage) {
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
						keyPressed(scene, primaryStage);
						break;
					case ESCAPE:
						stopGame();
						try {
							start(primaryStage);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					default:
						break;
					}
				});
				break;
			case ESCAPE:
				stopGame();
				try {
					start(primaryStage);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				break;
			default:
				break;
			}
			moved = false;
		});
	}

	/**
	 * Stops game, happens when Esc is hit or restartGame() is used
	 */
	public static void stopGame() {
		running = false;
		timeline.stop();
		snake.clear();
	}

	/**
	 * Starts game, changing variables and spawning snake
	 */
	public static void startGame() {
		Main.direction = Main.Direction.RIGHT;
		Rectangle tail = new Rectangle(Main.blocksize, Main.blocksize);
		tail.setFill(Color.WHITE);

		Main.snake.add(tail);

		Main.timeline.play();
		Main.running = true;
	}

	/**
	 * Restart happens when snake collides, stops and starts game
	 */
	public static void restartGame() {
		stopGame();
		startGame();
	}

	/**
	 * Makes the game's background black
	 * 
	 * @return backGround
	 */
	public Rectangle background() {
		Rectangle backGround = new Rectangle(Main.WinWidth, Main.WinHeight);
		backGround.setFill(Color.BLACK);
		return backGround;
	}

	public static void main(String[] args) {
		launch(args);
	}
}