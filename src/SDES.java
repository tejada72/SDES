import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Simplified Data Encryption Standard is a symmetric-key algorithm for the encryption of electronic data.
 *
 * @author Aaron Alnutt
 * @author Matthew Moore
 * @author Alex Tejada
 *
 */
public class SDES {

    boolean[] key;
    int[] initialPermutation = new int[]{1,5,2,0,3,7,4,6};
    int[] invInitialPermutation = new int[]{3,0,2,4,6,1,7,5};
    int[] expansionPermutation = new int[]{3,0,1,2,1,2,3,0};
    int[] pFourPermutation = new int[]{1,3,2,0};
    int[] keyOnePermutation = new int[]{0,6,8,3,7,2,9,5};
    int[] keyTwoPermutation = new int[]{7,2,5,4,9,1,8,0};


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
            result[index] = encryptByte(Byte.parseByte(input[index]));
        }
        return result;
    }

    /**
     * Convert the given string into a bit array
     *
     * @param input String
     * @return array of boolean
     * @author Alex Tejada
     */
    public byte[] stringToBitArray(String input) {
        String[] splitInput = input.split(" ");
        byte[] result = new byte[splitInput.length];

        for(int index = 0; index < splitInput.length; index++) {
            result[index] = Byte.parseByte(splitInput[index]);
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
    public byte encryptByte(byte input) {
        boolean[] result = byteToBitArray(input, 8);
        boolean[] k1 = expPerm(key, keyOnePermutation);
        boolean[] k2 = expPerm(key, keyTwoPermutation);
        result = expPerm(result,initialPermutation);
        result = f(result,k1);
        result = concat(rh(result),lh(result));
        result = f(result,k2);
        result = expPerm(result,invInitialPermutation);
        return bitArrayToByte(result);
    }

    /** Get a 10 bit key from the scanner. Store it as an array of booleans in a field
     * @param scanner
     * @author Matt Moore
     */
    public void getKey10(Scanner scanner) {
        key = new boolean[10];
        System.out.println("Insert key: ");
        String input = scanner.nextLine();
        input = input.replace(" ","");
        //Storing booleans in the array
        for (int i = 0; i < 10; i++) {
            if (input.charAt(i) == '0')
                key[i] = false;
            else
                key[i] = true;
        }
    }

    /** Decrypts the given byte array
     * @param cipher (Array of bytes representing the cipher text)
     * @return message (Array of bytes representing the original plain text)
     * @author Matt Moore
     */
    public byte[] decrypt(byte[] cipher) {
        byte[] result = new byte[cipher.length];
        //Loops through the cipher text and calls decryptByte on each byte
        for(int i = 0; i < cipher.length; i++) {
            result[i] = decryptByte(cipher[i]);
        }
        return result;
    }

    /** Decrypts a single byte using SDES
     * @param b (byte we will decrypt)
     * @return byte (decrypted single byte)
     * @author Matt Moore
     */
    public byte decryptByte(byte b) {
        boolean[] result = byteToBitArray(b, 8);
        boolean[] k1 = expPerm(key, keyOnePermutation);
        boolean[] k2 = expPerm(key, keyTwoPermutation);
        result = expPerm(result,initialPermutation);
        result = f(result,k2);
        result = concat(rh(result),lh(result));
        result = f(result,k1);
        result = expPerm(result, invInitialPermutation);
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

    /**
     * F(k,x) is a Feistal function F(k,x) = P4 (s0 (L (k xor EP(x))) || s1 (R (k xor EP(x)))
     * @param k (key - boolean array (8))
     * @param x (boolean array (4) - R(x) - right half x)
     * @return result (boolean array (4))
     * @author Matt Moore
     */
    public boolean[] feistel(boolean[] k, boolean[] x) {
        boolean[] result = expPerm(x, expansionPermutation);
        result = xor(k, result);
        boolean[] leftTemp = lh(result);
        boolean[] rightTemp = rh(result);
        boolean[] s00 = {false,false};
        boolean[] s01 = {false,true};
        boolean[] s10 = {true,false};
        boolean[] s11 = {true,true};

        final Map<String, boolean[]> sBoxZero = new HashMap<String, boolean[]>() {{ put("0000",s01); put("0001", s11); put("0010", s00); put("0011", s10); put("0100", s11); put("0101", s01); put("0110", s10);
            put("0111", s00); put("1000", s00); put("1001", s11); put("1010", s10); put("1011", s01); put("1100", s01); put("1101", s11); put("1110", s11); put("1111", s10);}};

        final Map<String, boolean[]> sBoxOne = new HashMap<String, boolean[]>() {{ put("0000",s00); put("0001", s10); put("0010", s01); put("0011", s00); put("0100", s10); put("0101", s01); put("0110", s11);
            put("0111", s11); put("1000", s11); put("1001", s10); put("1010", s00); put("1011", s01); put("1100", s01); put("1101", s00); put("1110", s00); put("1111", s11);}};

        result = concat(sBoxZero.get(feistelHelper(leftTemp)), sBoxOne.get(feistelHelper(rightTemp)));

        return expPerm(result, pFourPermutation);
    }

    /**
     * Replicated Aaron's show method, but actually returning a string rather than void. Used to populate the S-boxes
     * @param inp (boolean array)
     * @return String (representation of boolean array)
     * @author Matt Moore
     */
    public String feistelHelper(boolean[] inp) {
        String returnStr = "";
        for(int i = 0; i < inp.length;i++)
        {
            returnStr += inp[i] ? "1" : "0";
        }
        return returnStr;

    }

    /**
     * Provides the Left Half of x, L(x)
     * @param inp (array of booleans)
     * @return leftHalf (a bit array, which is the left half of the parameter, input)
     * @author Matt Moore
     */
    public boolean[] lh(boolean[] inp) {
        boolean[] leftHalf = new boolean[4];

        for (int i = 0; i < leftHalf.length; i++) {
            leftHalf[i] = inp[i];
        }

        return leftHalf;
    }

    /**
     * Provides the Right Half of x, R(x)
     * @param inp (array of booleans)
     * @return rightHalf (a bit array, which is the right half of the parameter, input)
     * @author Matt Moore
     */
    public boolean[] rh(boolean[] inp) {
        boolean[] rightHalf = new boolean[4];

        for (int i = 0; i <rightHalf.length; i++) {
            rightHalf[i] = inp[i + 4];
        }

        return rightHalf;
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

    /**
     * System.out.println the given boolean[]
     *
     * @param inp booealn array to print.
     * @author Aaron Alnutt
     */
    public void show(boolean[] inp)
    {
        String returnStr = "";
        for(int i = 0; i < inp.length;i++)
        {
            returnStr += inp[i] ? "1" : "0";
        }
        System.out.println(returnStr);
    }

    /**
     * System.out.println the given byte[]
     *
     * @param inp byte array to print.
     * @author Aaron Alnutt
     */
    public void show(byte[] inp)
    {
        String returnStr = "";
        for(int i = 0; i < inp.length;i++)
        {
            returnStr += inp[i] + " ";
        }
        //using hex-notation to make it more readable
        System.out.println(returnStr);
    }

    /**Converts the given byte array to a String
     * @param inp (array of bytes - hopefully storing the codes of printable characters)
     * @return String ('Chars' as a string)
     * @author Matt Moore
     */
    public String byteArrayToString(byte[] inp) {
        String charString = "";
        for (int i = 0; i < inp.length; i++) {
            charString+=(char)inp[i];
        }
        return charString;
    }

}
