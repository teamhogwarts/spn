import java.util.Arrays;

/**
 * @author Benjamin Brodwolf
 * krysi FS-2019
 * @version 1.0
 */
public class CTRD {

    private int yMinus1;
    private int[] yi;
    private SPN system;

    /**
     * Konstruktor mit dem Verschlüsselungs-System
     *
     * @param system
     */
    public CTRD(SPN system) {
        this.system = system;
    }

    /**
     * Zerlegen des BitStrings Y in Blöcke der Länge "blockSize" zerlegen = y-1, y0, y1 ....yn-1
     *
     * @param Y
     * @param blockSize
     * @return
     */
    public static int[] disjointY(String Y, int blockSize) {
        int amountOfBlocks = Y.length() / blockSize; // 128 / 16 = 8
        int[] result = new int[amountOfBlocks];

        for (int i = 0; i < amountOfBlocks; i++) {
            result[i] = Integer.parseInt(Y.substring(i * blockSize, i * blockSize + blockSize), 2);
        }
        return result;
    }

    /**
     * Methode welche den Algorithmus zum Entschlüsseln im CTR-Modus
     * mit derm Verschlüsselungsystem SPN verwendet
     *
     * @param y
     * @param blockSize
     * @return
     */
    public String deCrypt(String y, int blockSize) {

        // zerlegen des Bitstrings in ein Int-Array
        int[] tempY = disjointY(y, blockSize);

        // herauspicken der benötigten "y-1"
        this.yMinus1 = tempY[0];

        // bereitstellen von Variablen für die Iteration
        this.yi = Arrays.copyOfRange(tempY, 1, tempY.length);
        int[] resultX = new int[yi.length];

        // Iteration des CTR-Modus mit dem SPN-Verschlüsselungssystem
        for (int i = 0; i < yi.length; i++) {
            int xi = (yMinus1 + i) % (1 << blockSize);
            xi = system.startSPN(xi, false);
            xi = xi ^ yi[i];
            resultX[i] = xi;
        }

        // entfernen des letzten Pattern 1 gefüllt mit 0er
        resultX[resultX.length - 1] = Integer.numberOfTrailingZeros(resultX[resultX.length - 1] + 1);

        // extrarieren der ASCII-Zeichen (8 Bit) aus den 16 Bit befüllten Integer.
        char[] chars = new char[resultX.length * 2];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (resultX[i / 2] >>> 8);
            i++;
            chars[i] = (char) (resultX[i / 2] & 0b00000_0000_1111_1111);
        }

        return new String(chars).trim();
    }
}
