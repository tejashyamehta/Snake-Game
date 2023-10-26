import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame3 extends JPanel implements KeyListener, ActionListener {
    private LinkedList<Point> snake;
    private Point fruit;
    private int direction;
    private Timer timer;
    private int collisionType;
    private int score;

    public SnakeGame3() {
        snake = new LinkedList<>();
        snake.add(new Point(10, 10)); // Initial position of snake
        fruit = generateFruit();
        direction = KeyEvent.VK_RIGHT;
        score = 0;

        timer = new Timer(250, this);
        timer.start();
    }

    private Point generateFruit() {
        Random random = new Random();
        int x = random.nextInt(20);
        int y = random.nextInt(20);
        return new Point(x, y);
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead = (Point) head.clone();

        switch (direction) {
            case KeyEvent.VK_UP:
                newHead.translate(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                newHead.translate(0, 1);
                break;
            case KeyEvent.VK_LEFT:
                newHead.translate(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                newHead.translate(1, 0);
                break;
        }

        snake.addFirst(newHead);

        if (newHead.equals(fruit)) {
            fruit = generateFruit();
            score += 10;
        } else {
            snake.removeLast();
        }

        if (collision()) {
            timer.stop();
            resetGame();
        }
    }

    private boolean collision() {
        Point head = snake.getFirst();
        if (head.x < 0 || head.x >= 20 || head.y < 0 || head.y >= 20) {
            collisionType = 1; // Hit the boundary
            return true;
        }
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                collisionType = 2; // Hit itself
                return true;
            }
        }
        return false;
    }

    private void resetGame() {
        snake.clear();
        snake.add(new Point(10, 10));
        fruit = generateFruit();
        direction = KeyEvent.VK_RIGHT;

        String message = "";
        if (collisionType == 1) {
            message = "You hit the boundary!";
        } else if (collisionType == 2) {
            message = "You hit yourself!";
        }

        int choice = JOptionPane.showConfirmDialog(this, "Game Over! \n" + message + " \n Your final score is: " + score + " \n Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            score = 0;
            timer.start();
        } else {
            System.exit(0);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        for (int i = 0; i < 20; i++) {
            g.drawLine(i * 20, 0, i * 20, 400);
            g.drawLine(0, i * 20, 400, i * 20);
        }

        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x * 20, point.y * 20, 20, 20);
        }

        g.setColor(Color.RED);
        g.fillRect(fruit.x * 20, fruit.y * 20, 20, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_LEFT) && (direction != KeyEvent.VK_RIGHT)) {
            direction = KeyEvent.VK_LEFT;
        } else if ((key == KeyEvent.VK_RIGHT) && (direction != KeyEvent.VK_LEFT)) {
            direction = KeyEvent.VK_RIGHT;
        } else if ((key == KeyEvent.VK_UP) && (direction != KeyEvent.VK_DOWN)) {
            direction = KeyEvent.VK_UP;
        } else if ((key == KeyEvent.VK_DOWN) && (direction != KeyEvent.VK_UP)) {
            direction = KeyEvent.VK_DOWN;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame3 snakeGame = new SnakeGame3();
        frame.add(snakeGame);
        frame.setSize(420, 440);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(snakeGame);
    }
}
