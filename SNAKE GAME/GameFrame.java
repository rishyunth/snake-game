import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    GameFrame(String username) {

        GamePanel gamePanel = new GamePanel(username);

        this.add(gamePanel);
        this.setVisible(true);
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.pack();
    }
}