import java.util.ArrayList;

/**
 * Simplified Data Encryption Standard is a symmetric-key algorithm for the encryption of electronic data.
 *
 * @author Aaron Alnutt
 * @author Matthew Moore
 * @author Alex Tejada
 *
 */
public class SDES {

    //TODO
    boolean[] key;


    /**
     * Encrypt the given string using SDES and returns the cipher text.
     *
     * @param plainText Input text to encrypt
     * @return The cipher text
     */
    public byte[] encrypt(String plainText) {
        String[] input = plainText.split(" ");
        byte[] result = new byte[input.length];

        for(int index = 0; index < input.length; index++) {
            result[index] = encrypt(Byte.parseByte(input[index]));
        }
        return result;
    }


    /**
     * Encrypt a single byte using SDES
     *
     * @param input a single byte as plain text
     * @return a single byte as cipher text
     */
    //TODO finish this method
    public byte encrypt(byte input) {
        int[] permutation = new int[]{1,5,2,0,3,7,4,6}; //TODO Can someone double check me on this permutation?
        boolean[] result = byteToBitArray(input, 8);
        result = expPerm(result,permutation);
        result = f(result,key);
        result = concat(rh(result),lh(result));
        result = f(result,key);
        result = expPerm(result,permutation);
        return bitArrayToByte(result);
    }

    //TODO Documentation & implementation
    public boolean[] byteToBitArray(byte b, int size) {
        return null;
    }

    //TODO Documentation & implementation
    public boolean[] f(boolean[] x, boolean[] key) {
        return null;
    }

    //TODO Documentation & implementation
    public boolean[] lh(boolean[] input) {
        return null;
    }

    //TODO Documentation & implementation
    public boolean[] rh(boolean[] input) {
        return null;
    }

    //TODO Documentation & implementation
    public boolean[] expPerm(boolean[] inp, int[] epv) {
        return null;
    }

    //TODO Documentation & implementation
    public byte bitArrayToByte(boolean[] inp) {
        return 0;
    }

    /**
     * Exclusive Or function
     *
     * @param x first array of boolean
     * @param y second array of boolean
     * @return the result of x XOR y
     */
    public boolean[] xor(boolean[] x, boolean[] y) {
        int range = getSmallerLength(x,y);
        int index = 0;
        boolean[] result = new boolean[range];

        while(index < range) {
            result[index] = Boolean.logicalXor(x[index],y[index]);
            index++;
        }
        return result;
    }

    /**
     * Concatenate the second array at the end of the first array.
     *
     * @param x first array of boolean
     * @param y second array of boolean
     * @return a boolean[] with the concatenation of the first array and second array
     */
    public boolean[] concat(boolean[] x, boolean[] y) {
        int range = x.length+y.length;
        boolean[] result = new boolean[range];

        for(int index = 0;index < range;index++) {
            if(index < x.length)
                result[index] = x[index];
            else
                result[index] = y[index-x.length];
        }

        return result;
    }

    /**
     * Return the smaller length of 2 array of booleans.
     *
     * @param x array of boolean
     * @param y array of boolean
     * @return int representing the smaller length
     */
    private int getSmallerLength(boolean[] x, boolean[] y) {
        if(x.length > y.length)
            return y.length;
        return x.length;
    }

}
