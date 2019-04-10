import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SPNTest {

    @Test
    void sboxTest(){
        //given
        SPN spn = new SPN();
        int[] actual = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        int[] expected = new int[]{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7};


        //when
        int[] spnSBOXED = spn.sbox(actual);

        //then
        assertEquals(spnSBOXED.length, 16);
        assertArrayEquals(spnSBOXED, expected);
    }

    @Test
    void bitpermutationTest(){
        //given
        SPN spn = new SPN();
        int[] actual = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        int[] expected = new int[]{0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};


        //when
        int[] spnBP = spn.bitpermutation(actual);

        //then
        assertEquals(spnBP.length, 16);
        assertArrayEquals(spnBP, expected);
    }

}