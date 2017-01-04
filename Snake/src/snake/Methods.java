package snake;

import java.util.ListIterator;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Methods {

	// Method to randomize the placement of food
	public static void foodRandom(Rectangle food) {
		food.setTranslateX((int) (Math.random() * Main.WinWidth) / Main.blocksize * Main.blocksize);
		food.setTranslateY((int) (Math.random() * Main.WinHeight) / Main.blocksize * Main.blocksize);
		System.out.println(food.getTranslateX() + "; " + food.getTranslateY());
		food.setFill(Color.RED);
		foodReset(food, Main.snake);
	}

	// Method to check if the random placement of food is under the snake if it
	// is, RNG it again
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

	// Collision with itself and with walls
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
			// System.out.println(tail.getTranslateX() + " " +
			// tail.getTranslateY());
			Main.restartGame();
		}
	}

	//Adding extra piece to the snake after eating
	public static void addLength(double tailX, double tailY) {
		Rectangle extra = new Rectangle(Main.blocksize, Main.blocksize);
		extra.setTranslateX(tailX);
		extra.setTranslateY(tailY);
		extra.setFill(Color.WHITE);
		Main.snake.add(extra);
	}

}
