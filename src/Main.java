import java.util.Random;

public class Main {
    public static void main(String[] args) {
        new GameOfLife();
    }

    // return the next generation by the input matrix
    static boolean[][] nextGen(boolean[][] m) {
        boolean[][] mT = extendM(m);
        boolean[][] mS = m.clone();
        for (int y = 0; y < m.length; y++) { // for some y
            for (int x = 0; x < m[0].length; x++) { // for some x
                int p = countPop(mT, y + 1, x + 1);
                if (p < 2 || p > 3) {
                    mS[y][x] = false;
                } else if (p == 3) {
                    mS[y][x] = true;
                }
            }
        }
        return mS;
    }

    // return the first matrix
    static boolean[][] genMatrix() {
        boolean[][] matrix = new boolean[100][100];
        // int seed = s.nextInt();
        Random uni = new Random();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = uni.nextBoolean();
            }
        }
        return matrix;
    }

    // return a clone of the original matrix that extended by its margin
    static boolean[][] extendM(boolean[][] m) {
        boolean[][] mE = new boolean[m.length + 2][m.length + 2];
        mE[0][0] = m[m.length - 1][m.length - 1];
        mE[0][mE.length - 1] = m[m.length - 1][0];
        mE[mE.length - 1][0] = m[0][m.length - 1];
        mE[mE.length - 1][mE.length - 1] = m[0][0];
        for (int i = 1; i < m.length + 1; i++) {
            System.arraycopy(m[i - 1], 0, mE[i], 1, m[0].length);

            mE[0][i] = m[m.length - 1][i - 1];
            mE[mE.length - 1][i] = m[0][i - 1];
            mE[i][0] = m[i - 1][m.length - 1];
            mE[i][mE.length - 1] = m[i - 1][0];
        }
        return mE;
    }

    // return the 3x3 Population surrounding one cell
    static int countPop(boolean[][] m, int y, int x) {
        int p = 0;
        if (m[y][x + 1]) p++; // isRightAlive
        if (m[y][x - 1]) p++; // isLeftAlice
        if (m[y - 1][x]) p++; // isUpAlice
        if (m[y + 1][x]) p++; // isDownAlice
        if (m[y - 1][x - 1]) p++; // isUpLeftAlive
        if (m[y + 1][x - 1]) p++; // isDownLeftAlive
        if (m[y - 1][x + 1]) p++; // isUpRightAlive
        if (m[y + 1][x + 1]) p++; // isDownRightAlive
        return p;
    }

    static int countAlive(boolean[][] m) {
        int cAlive = 0;
        for (boolean[] booleans : m) {
            for (boolean bool : booleans) {
                if (bool) {
                    cAlive++;
                }
            }
        }
        return cAlive;
    }
}
