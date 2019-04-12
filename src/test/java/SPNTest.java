import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SPNTest {

    @Test
    void keysTest() {
        //given
        SPN spn = new SPN();
        final int fullKey = 0b0011_1010_1001_0100_1101_0110_0011_1111;
        final String[] fullKeyString = {
                "0011101010010100"
                , "1010100101001101"
                , "1001010011010110"
                , "0100110101100011"
                , "1101011000111111"};

        int r = 4;
        int n = 4;
        int m = 4;

        //when
        int[] keys = spn.generateSpnKeys(fullKey, r, n);
        String[] keysZerosPadding = new String[keys.length];

        for (int i = 0; i < keys.length; i++) {
            keysZerosPadding[i] = spn.bitPaddingWithZeros(Integer.toBinaryString(keys[i]), n, m); //fill the left padding with Zeros
        }

        //then
        for (int i = 0; i < keys.length; i++) {
            assertEquals(keysZerosPadding[i], fullKeyString[i]);
        }
    }

    @Test
    void intToArrayTest(){
        //given
        SPN spn = new SPN();

        int x = 0b1010_1111_0011_1100;
        //when

        int[] arrX = spn.intTOIntArray(x);
        //then
        System.out.println(Arrays.toString(arrX));
    }

    @Test
    void sboxTest() {
        //given
        SPN spn = new SPN();
        int[] actual = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        int[] expected = new int[]{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7};

        //when
        int[] spnSBOXED = spn.sbox(actual, false);

        //then
        assertEquals(spnSBOXED.length, 16);
        assertArrayEquals(spnSBOXED, expected);
    }

    @Test
    void sboxReversTest() {
        //given
        SPN spn = new SPN();
        int[] actual = new int[]{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7};
        int[] expected = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

        //when
        int[] spnSBOXED = spn.sbox(actual, true);

        //then
        assertEquals(spnSBOXED.length, 16);
        assertArrayEquals(spnSBOXED, expected);
    }

    @Test
    void bitpermutationTest() {
        //given
        SPN spn = new SPN();
        int[] bitpermutationValues = new int[]{0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};

        int n = 4;
        int m = 4;
        int actual   = 0b0011_1010_1001_0100;
        int expected = 0b0110_0001_1100_1010;

        //when
        int bitsAferBP = spn.bitpermutation(actual, n, m, bitpermutationValues);

        //then
        assertEquals(expected, bitsAferBP);
    }
}