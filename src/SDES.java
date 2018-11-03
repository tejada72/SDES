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
     * @author Alex Tejada
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
     * @author Alex Tejada
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

     /**
     * Convert the given byte to a bit array, of the given size.
     *
     * @param b byte to convert.
     * @param size The size of the resulting bit array. The operator >>> can be used for an unsigned right shift.
     * @return the boolean array from given byte b.
     * @author Aaron Alnutt
     */
    public boolean[] byteToBitArray(byte b, int size) {
        boolean[] returnBoolArray = new boolean[size];
        for (int i = 0; i < size; i++)
        {
            returnBoolArray[i] = (b & (int)Math.pow(2, (double)i)) != 0;
        }
        return returnBoolArray;
    }

    /**
     *  This is the 'round' function.
     *  It is its own inverse. f(x,k) = (L(x) xor F(R(x), k)) || R(x)
     *
     * @param x first boolean array
     * @param k second boolean array to be use as the key
     * @return The resulting boolean array after applying the round function in the SDES algorithm.
     * @author Alex Tejada
     */
    public boolean[] f(boolean[] x, boolean[] k) {
        // L(x) XOR F(k,x) || R(x)
       return concat(xor(lh(x),feistel(k,rh(x))),rh(x));
    }

    //TODO Documentation & implementation
    public boolean[] lh(boolean[] input) {
        return null;
    }

    //TODO Documentation & implementation
    public boolean[] rh(boolean[] input) {
        return null;
    }

    /** 
     * Expand and/or permute and/or select from the bit array, inp, producing an expanded/permuted/selected bit array.
     * @param inp A bit array represented as booleans, true=1, false=0.
     * @param epv An expansion and/or permutation and/or selection vector; all numbers in epv must be in the range 0..inp.length-1, i.e. they must be valid subscripts.
     * @return The permuted/expanded/selected bit array, or null if there is an error.
     * @author Aaron Alnutt
     */
    public boolean[] expPerm(boolean[] inp, int[] epv) {
        boolean[] returnPerm = new boolean[epv.length];
        for(int i = 0; i < epv.length; i++)
        {
            returnPerm[i] = inp[epv[i]];
        }
        return returnPerm;
    }

    /** 
     * Convert the given bit array to a single byte.
     * @param inp array of booleans
     * @return the byte from inp
     * @author Aaron Alnutt
     */
    public byte bitArrayToByte(boolean[] inp) {
        byte returnByte = 0;
        for (int i = 0; i < inp.length; i++)
        {
            returnByte |= 
                (inp[i] ? 1 : 0) << i;
        }
        return returnByte;
    }

    //TODO Documentation & implementation
    public boolean[] feistel(boolean[] k, boolean[] x) {
        return null;
    }

    /**
     * Exclusive Or function
     *
     * @param x first boolean array
     * @param y second boolean array
     * @return the result of x XOR y
     * @author Alex Tejada
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
     * @param x first boolean array
     * @param y second boolean array
     * @return a boolean[] with the concatenation of the first array and second array
     * @author Alex Tejada
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
     * Return the smaller length of the 2 boolean array.
     *
     * @param x boolean array
     * @param y boolean array
     * @return int representing the smaller length
     * @author Alex Tejada
     */
    private int getSmallerLength(boolean[] x, boolean[] y) {
        if(x.length > y.length)
            return y.length;
        return x.length;
    }

}
