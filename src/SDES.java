/**
 * Simplified Data Encryption Standard is a symmetric-key algorithm for the encryption of electronic data.
 *
 * @author Aaron Alnutt
 * @author Matthew Moore
 * @author Alex Tejada
 *
 */
public class SDES {

    /**
     * Exclusive Or function
     *
     * @param x array of boolean
     * @param y array of boolean
     * @return the result of x XOR y
     */
    public boolean[] xor(boolean[] x, boolean[] y) {
        int range = getSmallerLength(x,y);
        int index = 0;
        boolean[] result = new boolean[range];

        while(index < range) {
            //1 XOR 1 == 0
            //1 XOR 0 == 1
            //0 XOR 1 == 1
            //0 XOR 0 == 0
            result[index] = x[index] && !y[index] || !x[index] && y[index];
            index++;
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
