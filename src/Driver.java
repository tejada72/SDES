import java.util.*;
/**
 * Test the implementation of SDES
 *
 * @author (sdb)
 * @version (Oct 2018)
 */
public class Driver
{
    public static void main(String[] args) {
        SDES sdes = new SDES();

        Scanner scanner = new Scanner (System.in);
        sdes.getKey10(scanner);
        String cipherText = "x";
        //System.out.println ("Enter plain text, or hit 'Enter' to terminate");
        System.out.println ("Enter cipher text, or hit 'Enter' to terminate");
        cipherText = scanner.nextLine();
        byte [] cipher;
        String cipherString;
        /**byte [] cipher = {-115, -17, -47, -113, -43, -47, 15, 84, -43, -113, -17, 84, -43, 79, 58, 15, 64, -113, -43, 65, -47, 127, 84, 64, -43,
         -61, 79, -43, 93, -61, -14, 15, -43, -113, 84, -47, 127, -43, 127, 84, 127, 10, 84, 15, 64, 43};*/
        while (cipherText.length() > 0)
        {
            //cipher = sdes.encrypt (plain);
            //System.out.print ("Cipher is ");
            //sdes.show(cipher);
            cipher = sdes.stringToBitArray(cipherText);
            byte[] plainText = sdes.decrypt(cipher);
            System.out.print ("Plain is ");
            sdes.show(plainText);
            //sdes.show(sdes.decrypt (cipher));
            System.out.println ("Enter plain text, or hit 'Enter' to terminate");
            cipherString = scanner.nextLine();
            System.out.print("Cipher is ");
            sdes.show(sdes.encrypt(cipherString));
            System.out.println();
            System.out.println ("Enter cipher text, or hit 'Enter' to terminate");
        }
    }
}