package snake;

import java.util.ListIterator;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Methods {

	/**
	 * Randomize the placement of food, calls out foodReset()
	 * @param food food A Rectangle that presents food for the snake
	 */
	public static void foodRandom(Rectangle food) {
		food.setTranslateX((int) (Math.random() * Main.WinWidth) / Main.blocksize * Main.blocksize);
		food.setTranslateY((int) (Math.random() * Main.WinHeight) / Main.blocksize * Main.blocksize);
		food.setFill(Color.WHITE);
		foodReset(food, Main.snake);
	}

	/**
	 * Check if the random food placement is under snake or not, if is, calls out foodRandom() if necessary
	 * @param food A Rectangle that presents food for the snake
	 * @param snake A list that allows to add Nodes to it
	 */
	public static void foodReset(Rectangle food, ObservableList<Node> snake) {
		boolean flag = true;
		while (flag) {
			flag = false;
			ListIterator<Node> current = snake.listIterator();
			while (current.hasNext()) {
				Node i = current.next();
				boolean match = i.getTranslateX() == food.getTranslateX() && i.getTranslateY() == food.getTranslateY();
				if (match) {
					foodRandom(food);
					while (current.hasPrevious()) {
						current.previous();
					}
				}
			}
		}

	}

	/**
	 * Collision detection with itself and with walls
	 * @param tail A part of @param snake that actually represents snake's head
	 */
	public static void collision(Node tail) {
		for (Node current : Main.snake) {
			if (current != tail && tail.getTranslateX() == current.getTranslateX()
					&& tail.getTranslateY() == current.getTranslateY()) {
				Main.restartGame();
				break;
			}
		}
		if (tail.getTranslateX() < 0 || tail.getTranslateX() >= Main.WinWidth || tail.getTranslateY() < 0
				|| tail.getTranslateY() >= Main.WinHeight) {
			Main.restartGame();
		}
	}

	/**
	 * Adds length to snake, called out by foodEating()
	 * @param tailX Actually snake's head location on X coordinates
	 * @param tailY Actually snake's head location on Y coordinates
	 */
	public static void addLength(double tailX, double tailY) {
		Rectangle extra = new Rectangle(Main.blocksize, Main.blocksize);
		extra.setTranslateX(tailX);
		extra.setTranslateY(tailY);
		extra.setFill(Color.WHITE);
		Main.snake.add(extra);
	}
	
	/**
	 * Checks if @param tail is at the same X and Y coordinates as @param food, calls out foodRandom() and addLength() if necessary
	 * @param tail A part of @param snake that actually represents snake's head
	 * @param food A Rectangle that presents food for the snake
	 * @param tailX Actually snake's head location on X coordinates
	 * @param tailY Actually snake's head location on Y coordinates
	 */
	public static void foodEating(Node tail, Rectangle food, double tailX, double tailY){
		if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {
			Methods.foodRandom(food);
			Methods.addLength(tailX, tailY);
		}
		
	}
	
}
