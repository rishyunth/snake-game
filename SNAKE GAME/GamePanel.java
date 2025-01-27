import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 1500;
    static final int SCREEN_HEIGHT = 800;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
    static final int DELAY = 100;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 2;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    private Clip musicClip;

    private String playerName;
    int score;
    private int initialPos = 75;

    int currentDelay = DELAY;

    int Id = 1;
    public DatabaseHandler databaseHandler;

    GamePanel(String playerName){
        this.playerName = playerName;
        //this.databaseHandler = databaseHandler;

        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.setVisible(true);
        startGame();
       // System.out.println("Game started");
    }

    public void startGame() {
        score = 0;
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
        playMusic("snakeSong.wav");
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 50 - i * UNIT_SIZE;  // Adjust the initial x-position as needed
            y[i] = initialPos;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if(running) {

//			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
//				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
//				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
//			}

            int lineY = 70;  // Adjust the vertical position of the line as needed
            g.setColor(Color.BLUE);
            g.drawLine(0, lineY, SCREEN_WIDTH, lineY);

            g.setColor(Color.yellow);
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            g.drawString("Player: " + playerName, SCREEN_WIDTH - 200, 30);
            g.drawString("Score: " + score, SCREEN_WIDTH - 200, 60);

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i< bodyParts;i++) {
                if(i == 0) {
                    g.setColor(Color.yellow);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(45,180,0));
                    //g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
//            g.setColor(Color.red);
//            g.setFont( new Font("Ink Free",Font.BOLD, 40));
//            FontMetrics metrics = getFontMetrics(g.getFont());
//            g.drawString("Score: "+applesEaten*5, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());

            // Heading starts
            g.setColor(Color.pink);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            String heading = "SNAKE GAME";
            g.drawString(heading, (SCREEN_WIDTH - metrics.stringWidth(heading)) / 2, (100 - metrics.getHeight()) / 2);
        }
        else {
            gameOver(g);
        }

    }
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = (random.nextInt((int) ((SCREEN_HEIGHT - initialPos - UNIT_SIZE) / UNIT_SIZE)) * UNIT_SIZE) + initialPos;
        //appleY = random.nextInt(SCREEN_HEIGHT-100) + initialPos;
        //appleY = random.nextInt((int)(SCREEN_HEIGHT - initialPos))*UNIT_SIZE;
    }
    public void move(){
        for(int i = bodyParts;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }
    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            score = score + 1;
            newApple();
            if (currentDelay > 20){
                currentDelay = currentDelay - 3;
            }
        }
    }
    public void checkCollisions() {
        //checks if head collides with body
        for(int i = bodyParts;i>0;i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;

            }
        }
        //check if head touches left border
        if(x[0] < 0) {
            running = false;
        }
        //check if head touches right border
        if(x[0] > SCREEN_WIDTH - 25) {
            running = false;
        }
        //check if head touches top border
        if(y[0] < initialPos) {
            running = false;
        }
        //check if head touches bottom border
        if(y[0] > SCREEN_HEIGHT - 25) {
            running = false;
        }

        if(!running) {
            timer.stop();
            stopMusic();
        }
    }

    public void playMusic(String musicFilePath) {
        try {
            URL musicUrl = getClass().getResource(musicFilePath);
            if (musicUrl == null) {
                System.err.println("Music file not found: " + musicFilePath);
                return;
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicUrl);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioInputStream);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
        }
    }
    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.YELLOW);
        g.setFont( new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Your Score: "+applesEaten*5, (SCREEN_WIDTH - metrics1.stringWidth("Your Score: "+applesEaten))/2, g.getFont().getSize());
        //Game Over text
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

//        int score = applesEaten * 5;
//        DatabaseHandler databaseHandler = new DatabaseHandler();
//        databaseHandler.insertData(playerName,score);
//        System.out.println("Player Name: "+playerName);
//        System.out.println("Your Score: "+score);
        sendScore();

    }
    
    public void sendScore()
    {
        int score = applesEaten * 5;
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.insertData(playerName,score);
        System.out.println("Player Name: "+playerName);
        System.out.println("Your Score: "+score);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(running) {
            move();
            checkApple();
            checkCollisions();
            timer.setDelay(currentDelay);
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
