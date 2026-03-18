import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.io.*;

public class PacMan extends JPanel implements ActionListener, KeyListener {

    class Block {
        int x;
        int y;
        int startX;
        int startY;

        int velocityX = 0;
        int velocityY = 0;

        int height;
        int width;
        Image image;

        char direction = 'U';
        char nextDirection = ' ';

        Block(Image image, int x, int y, int height, int width) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.height = height;
            this.width = width;
            this.startX = x;
            this.startY = y;
        }

        void updateDirection (char direction) {
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();

            for (Block wall : walls) {
                if (collision(this, wall)) {
                    this.x -= velocityX;
                    this.y -= velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity() {
            if ( direction == 'U') {
                this.velocityX = 0;
                this.velocityY = -App.TILE_SIZE / 4;
            } else if ( direction == 'D') {
                this.velocityX = 0;
                this.velocityY = App.TILE_SIZE / 4;
            } else if ( direction == 'L') {
                this.velocityX = -App.TILE_SIZE / 4;
                this.velocityY = 0;
            } else if ( direction == 'R') {
                this.velocityX = App.TILE_SIZE / 4;
                this.velocityY = 0;
            }
        }

        void reset() {
            this.x = this.startX;
            this.y = this.startY;
        }
    }

    static class PopupText {
        int x;
        int y;
        String text;
        int framesLeft;

        PopupText(int x, int y, String text, int durationMs, int tickMs) {
            this.x = x;
            this.y = y;
            this.text = text;
            this.framesLeft = Math.max(1, durationMs / tickMs);
        }
    }

    private final Image wallImage;
    private final Image redGhostImage;
    private final Image blueGhostImage;
    private final Image orangeGhostImage;
    private final Image pinkGhostImage;
    private final Image purpleGhostImage;
    private final Image greenGhostImage;
    private final Image finalGhostImage;

    private final Image pacmanRightImage;
    private final Image pacmanLeftImage;
    private final Image pacmanUpImage;
    private final Image pacmanDownImage;

    private final Image cherryImage;
    private final Image strawberryImage;

    private final String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
            "X              XpboX              X",
            "X   XXX Xc  XX   r   XX   X XXX   X",
            "X     X XXX XX   X   XX XXX X    cX",
            "XX                               XX",
            "X    XXXX  XX  X   X cXX  XXX     X",
            "X       X XOOX X   X XOOX XOOX    X",
            "O       X XOOX X C X XOOX XOOOX   O",
            "OCXX    X XXXX X X X XXXX XOOOX XXO",
            "O    X  X X  X X X X X  X XOOOX   O",
            "X    Xc X X  X X X X X  X XOOX    X",
            "X     XX  X  X  X X  X  X XXXc    X",
            "XX                               XX",
            "X     X XXX XX       XX XXX X     X",
            "X   XXX X   XX X P X XX   X XXX   X",
            "X              X   X              X",
            "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
    };

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    HashSet<Block> cherrys;
    HashSet<Block> strawberrys;
    Block pacman;
    ArrayList<PopupText> popups = new ArrayList<>();

    private final int TICK_MS = 50;
    Timer gameLoop;

    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();

    int level = 2;
    int lives = 3;
    int score = 0;
    int highScore = 0;

    Block greenGhost;
    Block purpleGhost;
    Block finalGhost;
    int greenGhostX = 18 * App.TILE_SIZE;
    int greenGhostY = 2 * App.TILE_SIZE;
    int purpleGhostX = 16 * App.TILE_SIZE;
    int purpleGhostY = 2 * App.TILE_SIZE;

    boolean paused = false;
    boolean gameOver = false;
    boolean startingPause = true;
    private boolean sirenPlaying = false;
    private boolean deathSirenStopped = false;
    private boolean levelSirenRestart = false;

    long startPauseEndTime;
    long startPause = 0;
    long reStartPause = 0;
    long deathPause = 0;
    long levelPause = 0;

    PacMan() {
        setPreferredSize(new Dimension(App.BOARD_WIDTH, App.BOARD_HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        wallImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./wall.png"))).getImage();
        redGhostImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./redGhost.png"))).getImage();
        blueGhostImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./blueGhost.png"))).getImage();
        orangeGhostImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./orangeGhost.png"))).getImage();
        pinkGhostImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./pinkGhost.png"))).getImage();
        greenGhostImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./greenGhost.png"))).getImage();
        purpleGhostImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./purpleGhost.png"))).getImage();
        finalGhostImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./finalGhost.png"))).getImage();

        cherryImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./cherry.png"))).getImage();
        strawberryImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./strawberry.png"))).getImage();

        pacmanRightImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./pacmanRight.png"))).getImage();
        pacmanLeftImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./pacmanLeft.png"))).getImage();
        pacmanUpImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./pacmanUp.png"))).getImage();
        pacmanDownImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./pacmanDown.png"))).getImage();

        loadMap();

        if (pacman != null) {
            pacman.velocityX = 0;
            pacman.velocityY = 0;
        }

        for (Block ghost : ghosts) {
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }

        try {
            File f = new File("highscore.txt");
            if(f.exists()) {
                Scanner in = new Scanner(f);
                highScore = in.nextInt();
                in.close();
            }
        } catch (Exception e) {
            System.out.println("Failed to load high score");
        }

        gameLoop = new Timer(TICK_MS, this);
        gameLoop.start();

        startingPause = true;
        startPauseEndTime = System.currentTimeMillis() + 4500;

        Sound.play("start");
    }

    public void loadMap() {
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        cherrys = new HashSet<Block>();
        strawberrys = new HashSet<Block>();

        for (int r = 0 ; r < App.ROWS ; r++){
            for (int c = 0 ; c < App.COLUMNS ; c++){
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c * App.TILE_SIZE;
                int y = r * App.TILE_SIZE;

                if (tileMapChar == 'X') {
                    Block wall = new Block(wallImage, x, y, App.TILE_SIZE, App.TILE_SIZE);
                    walls.add(wall);
                } else if (tileMapChar == 'r') {
                    Block ghost = new Block(redGhostImage, x, y, App.TILE_SIZE, App.TILE_SIZE);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'b') {
                    Block ghost = new Block(blueGhostImage, x, y, App.TILE_SIZE, App.TILE_SIZE);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'o') {
                    Block ghost = new Block(orangeGhostImage, x, y, App.TILE_SIZE, App.TILE_SIZE);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'p') {
                    Block ghost = new Block(pinkGhostImage, x, y, App.TILE_SIZE, App.TILE_SIZE);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'P') {
                    pacman = new Block(pacmanRightImage, x, y, App.TILE_SIZE, App.TILE_SIZE);
                    pacman.direction = 'R';
                    pacman.nextDirection = ' ';
                } else if (tileMapChar == ' ') {
                    Block food = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                } else if (tileMapChar == 'c') {
                    Block cherry = new Block(cherryImage, x, y, App.TILE_SIZE, App.TILE_SIZE);
                    cherrys.add(cherry);
                } else if (tileMapChar == 'C') {
                    Block strawberry = new Block(strawberryImage, x, y, App.TILE_SIZE, App.TILE_SIZE);
                    strawberrys.add(strawberry);
                }
            }
        }

        if (level == 2 || level == 3) {
            int ghostSize = App.TILE_SIZE + 12;
            finalGhost = new Block(finalGhostImage, 17 * App.TILE_SIZE, 4 * App.TILE_SIZE, ghostSize, ghostSize);

            finalGhost.velocityX = 0;
            finalGhost.velocityY = 0;
        }

        if (level == 3) {
            greenGhost = new Block(greenGhostImage, greenGhostX, greenGhostY, App.TILE_SIZE, App.TILE_SIZE);
            purpleGhost = new Block(purpleGhostImage, purpleGhostX, purpleGhostY, App.TILE_SIZE, App.TILE_SIZE);

            ghosts.add(greenGhost);
            ghosts.add(purpleGhost);
        }

        for (Block ghost : ghosts) {
            ghost.updateDirection(directions[random.nextInt(4)]);
        }

    }

    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw (Graphics g) {
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }
        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }
        g.setColor(Color.WHITE);
        for (Block food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }
        for (Block cherry : cherrys) {
            g.drawImage(cherryImage, cherry.x, cherry.y, cherry.width, cherry.height, null);
        }
        for (Block strawberry : strawberrys) {
            g.drawImage(strawberryImage, strawberry.x, strawberry.y, strawberry.width, strawberry.height, null);
        }
        if ((level == 2 || level == 3) && finalGhost != null) {
            g.drawImage(finalGhostImage, finalGhost.x, finalGhost.y, finalGhost.width, finalGhost.height, null);
        }


        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        for (PopupText p : popups) {
            g.drawString(p.text, p.x, p.y - 10);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 20));

        if (gameOver) {
            g.drawString("Game Over : " + score + "  Level : " + level + "  High Score : " + highScore , 8 , 22);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.YELLOW);
            g.drawString("Press Enter to Restart!", 453, 22);
        } else {
            g.drawString("Level : " + level + "  x" + lives + "  Score : " + score + "  High Score : " + highScore,  8 , 22);
        }

        if (levelPause != 0) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String msg = "Level " + level + " starts!";
            int w = g.getFontMetrics().stringWidth(msg);
            g.setColor(Color.YELLOW);
            g.drawString(msg, (App.BOARD_WIDTH - w) / 2, App.BOARD_HEIGHT / 2);
        }
    }

    public void move() {

        boolean alignedX = (pacman.x % App.TILE_SIZE) == 0;
        boolean alignedY = (pacman.y % App.TILE_SIZE) == 0;

        if (alignedX && alignedY) {
            if (pacman.nextDirection != ' ' && canMove(pacman, pacman.nextDirection)) {
                pacman.updateDirection(pacman.nextDirection);
            }
        }

        switch (pacman.direction) {
            case 'U'-> pacman.image = pacmanUpImage;
            case 'D'-> pacman.image = pacmanDownImage;
            case 'L'-> pacman.image = pacmanLeftImage;
            case 'R'-> pacman.image = pacmanRightImage;
        }

        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        if (pacman.y >= 7 * App.TILE_SIZE && pacman.y < 10 * App.TILE_SIZE) {
            if (pacman.x < -pacman.width) {
                pacman.x = App.BOARD_WIDTH;
            }
            if (pacman.x > App.BOARD_WIDTH) {
                pacman.x = -pacman.width;
            }

            if (pacman.x < 0 || pacman.x > App.BOARD_WIDTH - App.TILE_SIZE) {
                if (pacman.nextDirection == 'U' || pacman.nextDirection == 'D') {
                    pacman.nextDirection = pacman.direction;
                }
            }

        }

        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;

                pacman.velocityX = 0;
                pacman.velocityY = 0;
                break;
            }
        }

        for (Block ghost : ghosts) {
            if (collision(ghost, pacman)) {
                pacman.velocityX = 0;
                pacman.velocityY = 0;
                for (Block g : ghosts) {
                    g.velocityX = 0;
                    g.velocityY = 0;
                }

                deathPause = System.currentTimeMillis() + 2000;
                Sound.stop("siren");
                Sound.play("death");

                lives--;
                if (lives == 0) {
                    gameOver = true;
                }
                return;
            }


            if (ghost.x == 16 * App.TILE_SIZE && ghost.y > 2 * App.TILE_SIZE && ghost.y < 5 * App.TILE_SIZE && ghost.direction != 'R' && ghost.direction != 'L') {
                ghost.updateDirection('L');
            }

            if (ghost.x == 18 * App.TILE_SIZE && ghost.y > 2 * App.TILE_SIZE && ghost.y < 5 * App.TILE_SIZE && ghost.direction != 'R' && ghost.direction != 'L') {
                ghost.updateDirection('R');
            }

            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;

            if (ghost.y >= 7 * App.TILE_SIZE && ghost.y < 10 * App.TILE_SIZE) {
                if (ghost.x < -ghost.width) {
                    ghost.x = App.BOARD_WIDTH;
                }

                if (ghost.x > App.BOARD_WIDTH) {
                    ghost.x = -ghost.width;
                }
            }

            for(Block wall : walls) {
                if (collision(ghost, wall)) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }
        }

        if ((level == 2 || level == 3) && finalGhost != null) {

            if (System.currentTimeMillis() >= deathPause &&
                    System.currentTimeMillis() >= reStartPause) {

                double dx = pacman.x - finalGhost.x;
                double dy = pacman.y - finalGhost.y;

                double distance = Math.sqrt(dx*dx + dy*dy);

                double ux = dx / distance;
                double uy = dy / distance;

                double speed = (level == 2) ? 4.0 : 6.0;

                finalGhost.x += (int)(ux * speed);
                finalGhost.y += (int)(uy * speed);
            }

            if (finalGhost.x < 0) finalGhost.x = 0;
            if (finalGhost.y < 0) finalGhost.y = 0;
            if (finalGhost.x > App.BOARD_WIDTH - finalGhost.width) finalGhost.x = App.BOARD_WIDTH - finalGhost.width;
            if (finalGhost.y > App.BOARD_HEIGHT - finalGhost.height) finalGhost.y = App.BOARD_HEIGHT - finalGhost.height;

            if (collision(finalGhost, pacman)) {

                pacman.velocityX = 0;
                pacman.velocityY = 0;

                for (Block g : ghosts) {
                    g.velocityX = 0;
                    g.velocityY = 0;
                }

                finalGhost.velocityX = 0;
                finalGhost.velocityY = 0;

                deathPause = System.currentTimeMillis() + 2000;
                Sound.play("death");

                lives--;
                if (lives == 0) {
                    gameOver = true;
                }
                return;
            }
        }


        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food;
                score += 10;
                Sound.play("waka");
            }
        }
        foods.remove(foodEaten);

        Block cherryEaten = null;
        for (Block cherry : cherrys) {
            if (collision(pacman, cherry)) {
                cherryEaten = cherry;
                score += 50;
                popups.add(new PopupText(pacman.x, pacman.y, "+50", 500, TICK_MS));
                Sound.play("bonus");
            }
        }
        cherrys.remove(cherryEaten);

        Block strawberryEaten = null;
        for (Block strawberry : strawberrys) {
            if (collision(pacman, strawberry)) {
                strawberryEaten = strawberry;
                score += 100;
                popups.add(new PopupText(pacman.x, pacman.y, "+100", 500, TICK_MS));
                Sound.play("bonus");
            }
        }
        strawberrys.remove(strawberryEaten);

        if (foods.isEmpty()) {
            if (level == 1) level = 2;
            else if (level == 2) level = 3;

            loadMap();
            resetPositions();

            pacman.velocityX = 0;
            pacman.velocityY = 0;
            pacman.direction = ' ';
            pacman.nextDirection = ' ';

            if (finalGhost != null) {
                finalGhost.velocityX = 0;
                finalGhost.velocityY = 0;
            }

            for (Block g : ghosts) {
                g.velocityX = 0;
                g.velocityY = 0;
            }

            levelPause = System.currentTimeMillis() + 4500;
        }
    }

    public boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public boolean canMove(Block p, char dir) {
        int velocityX = 0, velocityY = 0;

        if (dir == 'U') velocityY = -App.TILE_SIZE / 4;
        if (dir == 'D') velocityY = App.TILE_SIZE / 4;
        if (dir == 'L') velocityX = -App.TILE_SIZE / 4;
        if (dir == 'R') velocityX = App.TILE_SIZE / 4;

        pacman.x += velocityX;
        pacman.y += velocityY;

        for (Block wall : walls) {
            if (collision(p, wall)) {
                pacman.x -= velocityX;
                pacman.y -= velocityY;
                return false;
            }
        }
        pacman.x -= velocityX;
        pacman.y -= velocityY;
        return true;
    }

    public void resetPositions() {
        pacman.reset();

        Sound.play("start");

        pacman.velocityX = 0;
        pacman.velocityY = 0;
        pacman.direction = ' ';
        pacman.nextDirection = ' ';

        pacman.image = pacmanRightImage;

        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }

        if ((level == 2 || level == 3) && finalGhost != null) {
            finalGhost.x = finalGhost.startX;
            finalGhost.y = finalGhost.startY;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = popups.size() - 1; i >= 0; i--) {
            PopupText p = popups.get(i);
            p.framesLeft--;
            if (p.framesLeft <= 0) {
                popups.remove(i);
            }
        }

        if (startingPause) {
            if (System.currentTimeMillis() >= startPauseEndTime) {
                startingPause = false;
            } else {
                repaint();
                return;
            }
        }

        if (startPause != 0) {
            if (System.currentTimeMillis() < startPause) {
                repaint();
                return;
            } else {
                startPause = 0;
            }
        }

        if (System.currentTimeMillis() < deathPause) {
            if (!deathSirenStopped) {
                Sound.stop("siren");
                sirenPlaying = false;
                deathSirenStopped = true;
            }
            repaint();
            return;
        }

        if (deathPause != 0) {
            deathPause = 0;
            deathSirenStopped = false;
            reStartPause = System.currentTimeMillis() + 4500;
            resetPositions();
        }

        if (System.currentTimeMillis() < reStartPause) {
            repaint();
            return;
        }

        if (!sirenPlaying) {
            Sound.loop("siren");
            sirenPlaying = true;
        }

        if (System.currentTimeMillis() < levelPause) {

            if (sirenPlaying) {
                Sound.stop("siren");
                sirenPlaying = false;
            }

            levelSirenRestart = true;

            repaint();
            return;
        } else if (levelPause != 0) {
            for (Block g : ghosts) {
                char newDirection = directions[random.nextInt(4)];
                g.updateDirection(newDirection);
            }

            if (finalGhost != null) {

                finalGhost.velocityX = 0;
                finalGhost.velocityY = 0;
            }

            levelPause = 0;
        }

        if (levelSirenRestart) {
            Sound.loop("siren");
            sirenPlaying = true;
            levelSirenRestart = false;
        }

        if (!paused && !gameOver) {
            move();
        }
        repaint();

        if (gameOver) {
            if (score > highScore) {
                highScore = score;
                try (PrintWriter write = new PrintWriter("highscore.txt")) {
                    write.println(highScore);
                } catch (Exception ex){
                    System.out.println("Failed to save high score");
                }
            }
            gameLoop.stop();
        }

        if (!paused && !gameOver) {
            if (!sirenPlaying) {
                Sound.loop("siren");
                sirenPlaying = true;
            }
        } else {
            if (sirenPlaying) {
                Sound.stop("siren");
                sirenPlaying = false;
            }
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                loadMap();
                resetPositions();
                lives = 3;
                score = 0;
                gameOver = false;
                paused = false;
                gameLoop.start();
            }
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            paused = !paused;
            return;
        }

        if (!paused) {
            if (e.getKeyCode() == KeyEvent.VK_UP) pacman.nextDirection = 'U';
            else if (e.getKeyCode() == KeyEvent.VK_DOWN) pacman.nextDirection = 'D';
            else if (e.getKeyCode() == KeyEvent.VK_LEFT) pacman.nextDirection = 'L';
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT) pacman.nextDirection = 'R';
        }
    }
}