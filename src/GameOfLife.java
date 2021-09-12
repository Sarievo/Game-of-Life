package src;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class GameOfLife extends JFrame {
    // SRC.GameOfLife game = new SRC.GameOfLife();

    public GameOfLife() {
        super("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(size * sqSize + 25, size * sqSize + 85);
        setVisible(true);

        initComponents();
    }

    private final int sqSize = 10;
    private final int size = 100;

    private int iter = 0;
    void setIter() {
        this.iter = 0;
    }
    int getIter() {
        return iter;
    }

    private int lives = 0;
    int getLives() { return lives; }

    private boolean[][] matrix = new boolean[size][size];

    private boolean pause = true;
    void setPause() {
        pause = !pause;
    }

    private boolean reset = false;
    void setReset() {
        reset = !reset;
    }
    boolean isReset() {
        return reset;
    }

    private boolean isExited;
    void setExited() {
        this.isExited = true;
    }

    public void initComponents() {
        setLocationRelativeTo(null);

        JLabel generationLabel = new JLabel("Generation #" + iter);
        generationLabel.setName("GenerationLabel");

        JLabel aliveLabel = new JLabel("Alive: " + lives);
        aliveLabel.setName("AliveLabel");

        JToggleButton playToggleButton = new JToggleButton("▶");
        playToggleButton.setName("PlayToggleButton");
        playToggleButton.addActionListener(e -> setPause());

        JButton resetButton = new JButton("⟳");
        resetButton.setName("ResetButton");
        resetButton.addActionListener(e -> {
            setReset();
            if (pause) {
                setPause();
            }
        });

        JPanel Upper = new JPanel();
        setLayout(new FlowLayout());
        Upper.add(generationLabel);
        Upper.add(aliveLabel);
        Upper.add(playToggleButton);
        Upper.add(resetButton);
        add(Upper);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new Dashboard());
        add(panel);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        Thread t1 = new Thread(() -> {
            boolean[][] m = Main.genMatrix();
            while (!isExited) {
                // let button Reset reset the game
                if (isReset()) {
                    setIter();
                    m = Main.nextGen(Main.genMatrix());
                    setReset();
                }

                boolean[][] mStill = matrix.clone();

                iter++;
                lives = Main.countAlive(m);
                generationLabel.setText("Generation #" + iter);
                aliveLabel.setText("Alive: " + lives);
                matrix = Main.nextGen(m); // Update the matrix
                panel.repaint();

                try {
                    TimeUnit.MILLISECONDS.sleep(150);
                    while (pause && !isReset()) {
                        TimeUnit.MILLISECONDS.sleep(150);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (getIter() > 10 && (getLives() == 0 || mStill == m)) {
                    setExited();
                    break;
                }
            }
        });

        t1.start();
    }

    public class Dashboard extends JComponent {
        public void paint(Graphics g) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (matrix[i][j]) {
                        g.setColor(Color.MAGENTA);
                    } else {
                        g.setColor(Color.white);
                    }
                    g.fillRect(4 + i * sqSize, 4 + j * sqSize, sqSize, sqSize);
                }
            }

            g.setColor(Color.black);
            for (int i = 0; i <= size; i++) {
                g.drawLine(4 + i * sqSize, 4, 4 + i * sqSize, size * sqSize + 4);
                g.drawLine(4, 4 + i * sqSize, size * sqSize + 4, 4 + i * sqSize);
            }
        }
    }
}
