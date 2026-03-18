import javax.swing.JFrame;

public class App {
    final static int ROWS = 17;
    final static int COLUMNS = 35;
    final static int TILE_SIZE = 32;
    final static int BOARD_WIDTH = COLUMNS * TILE_SIZE;
    final static int BOARD_HEIGHT = ROWS * TILE_SIZE;

    public static void main(String[] args) {
        Sound.loadAll();

        JFrame frame = new JFrame("Pac Man");
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        StartScreen start = new StartScreen(() -> {
            frame.getContentPane().removeAll();

            PacMan pacmanGame = new PacMan();
            frame.add(pacmanGame);
            frame.pack();

            pacmanGame.requestFocusInWindow();
        });

        frame.add(start);
        frame.pack();
        frame.setVisible(true);
    }
}