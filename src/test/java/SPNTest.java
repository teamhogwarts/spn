
import org.junit.jupiter.api.Test;


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SPNTest {

    @Test
    void keysTest() {
        //given
        SPN spn = new SPN();
        final int fullKey = 0b0011_1010_1001_0100_1101_0110_0011_1111;
        final String[] fullKeyStringRegular = {
                "0011101010010100"
                , "1010100101001101"
                , "1001010011010110"
                , "0100110101100011"
                , "1101011000111111"};


        final String[] fullKeyStringReverse = {
                "1101011000111111"
                , "0100111000110101"
                , "1010011100011010"
                , "1101001110000101"
                , "0011101010010100"};


        int r = 4;
        int n = 4;
        int m = 4;

        //when

        int[] keys = spn.generateSpnKeys(fullKey, r, n);
        String[] keysZerosPadding = new String[keys.length];

//        int[] bitpermutationValues = {0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};
//        int[] keysReverse = spn.generateSpnKeysReverse(keys, n , m);


        for (int i = 0; i < keys.length; i++) {
            keysZerosPadding[i] = spn.bitPaddingWithZeros(Integer.toBinaryString(keys[i]), n, m); //fill the left padding with Zeros
        }

        //then

        //regular Keys

        assertArrayEquals(keysZerosPadding, fullKeyStringRegular);


        //reverse Keys
    }


    @Test
    void intToArrayTest() {
        //given
        SPN spn = new SPN();

        int x = 0b1010_1111_0011_1100;
        //when

        int[] arrX = spn.intTOIntArray(x);
        //then
        System.out.println(Arrays.toString(arrX));
        assertEquals(0b1010, arrX[0]);
        assertEquals(0b1111, arrX[1]);
        assertEquals(0b0011, arrX[2]);
        assertEquals(0b1100, arrX[3]);

    }

    @Test
    void intArrayToIntTest() {
        //given
        SPN spn = new SPN();

        int[] intArray = {0b1010, 0b1111, 0b0011, 0b1100};
        int expect = 0b1010_1111_0011_1100;


        //when
        int result = spn.intArryToInt(intArray);


        //then
        assertEquals(expect, result);
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
        int actual = 0b0011_1010_1001_0100;
        int expected = 0b0110_0001_1100_1010;

        //when
        int bitsAferBP = spn.bitpermutation(actual, n, m, bitpermutationValues);

        //then
        assertEquals(expected, bitsAferBP);
    }

    @Test
    void bitpermutationTest2() {
        //given
        SPN spn = new SPN();
        int[] bitpermutationValues = new int[]{0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};

        int n = 4;
        int m = 4;
        int actual = 0b1110_0001_0110_1000;
        int expected = 0b1001_1010_1010_0100;

        //when
        int bitsAferBP = spn.bitpermutation(actual, n, m, bitpermutationValues);

        //then
        assertEquals(expected, bitsAferBP);
    }

    @Test
    void roundSPNTest() {
        //given
        int r = 4;      //Rundenschlüssel
        int n = 4;      //Anzahl Bit eines Blocks
        int m = 4;      //Anzahl Blöcke
        int s = 32;     //Länge des Schlüssel in Bit
        int[] bitpermutationValues = new int[]{0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};

        final int testKey = 0b0001_0001_0010_1000_1000_1100_0000_0000;
        final int testX = 0b0001_0010_1000_1111;

        SPN spn = new SPN(r, n, m, s, testKey, bitpermutationValues);

        //when
        int result = spn.rounds(testX, false);

        int expected = 0b1010_1110_1011_0100;
        //then

        assertEquals(expected, result);

    }

    @Test
    void paddinWithZeroTest(){

        //given
        String given = "10";
        int n = 4;
        int m = 4;

        SPN spn = new SPN();

        //when
        String actual = spn.bitPaddingWithZeros(given, n,m);
        String expected = "0000000000000010";

        //then
        assertEquals(expected,actual);
    }
}