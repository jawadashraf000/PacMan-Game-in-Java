import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class StartScreen extends JPanel {

    private float alpha = 0f;
    private Timer fadeTimer;
    private final Image logo;
    private final Runnable onFinished;

    public StartScreen(Runnable onFinished) {
        setBackground(Color.BLACK);
        this.onFinished = onFinished;

        logo = new ImageIcon(Objects.requireNonNull(getClass().getResource("./logo.png"))).getImage();
        setPreferredSize(new Dimension(App.BOARD_WIDTH, App.BOARD_HEIGHT));

        fadeTimer = new Timer(40, e -> fadeIn());
        fadeTimer.start();
        Sound.play("logo");
    }

    private void fadeIn() {
        alpha += 0.02f;

        if (alpha >= 1f) {
            alpha = 1f;
            fadeTimer.stop();

            new Timer(1000, e -> {
                ((Timer)e.getSource()).stop();
                startFadeOut();
            }).start();
        }

        repaint();
    }

    private void startFadeOut() {
        fadeTimer = new Timer(40, e -> fadeOut());
        fadeTimer.start();
    }

    private void fadeOut() {
        alpha -= 0.02f;

        if (alpha <= 0f) {
            alpha = 0f;
            fadeTimer.stop();
            onFinished.run();
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, alpha
        ));

        int scaledW = (int)(logo.getWidth(null) * 0.1);
        int scaledH = (int)(logo.getHeight(null) * 0.1);

        int x = (getWidth() - scaledW) / 2;
        int y = (getHeight() - scaledH) / 2;

        g2.drawImage(logo, x, y, scaledW, scaledH, null);
    }
}