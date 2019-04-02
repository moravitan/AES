import java.io.*;
import java.nio.file.Files;
import java.security.SecureRandom;

public class main {



    public static void main(String[] args) throws IOException {

        args = new String [7];
/*        args [0] = "-e";
        args [1] = "-k";
        args [2] = "C:\\Users\\איתן אביטן\\Downloads\\לימודים\\אבטחת מחשבים ורשתות תקשורת\\עבודות הגשה\\עבודה 1\\חלק ב\\AES3_test_files\\key_short";
        args [3] = "-i";
        args [4] = "C:\\Users\\איתן אביטן\\Downloads\\לימודים\\אבטחת מחשבים ורשתות תקשורת\\עבודות הגשה\\עבודה 1\\חלק ב\\AES3_test_files\\message_short";
        args [5] = "-o";
        args [6] = "C:\\Users\\איתן אביטן\\Downloads\\לימודים\\אבטחת מחשבים ורשתות תקשורת\\עבודות הגשה\\עבודה 1\\חלק ב\\AES3_test_files\\test";*/
        args [0] = "-b";
        args [1] = "-m";
        args [2] = "C:\\Users\\איתן אביטן\\Downloads\\לימודים\\אבטחת מחשבים ורשתות תקשורת\\עבודות הגשה\\עבודה 1\\חלק ב\\AES3_test_files\\message_short";
        args [3] = "-c";
        args [4] = "C:\\Users\\איתן אביטן\\Downloads\\לימודים\\אבטחת מחשבים ורשתות תקשורת\\עבודות הגשה\\עבודה 1\\חלק ב\\AES3_test_files\\cipher_short";
        args [5] = "-o";
        args [6] = "C:\\Users\\איתן אביטן\\Downloads\\לימודים\\אבטחת מחשבים ורשתות תקשורת\\עבודות הגשה\\עבודה 1\\חלק ב\\AES3_test_files\\test";

        if (args[0] != "-b"){
            // get all the plaintext and split them into blocks
            File file = new File(args[4]);
            byte[] plaintext = Files.readAllBytes(file.toPath());
            byte [][][] allPlaintext = getText(plaintext);
            // get all the keys and split them into blocks
            file = new File(args[2]);
            byte[] keys = Files.readAllBytes(file.toPath());
            byte [][][] allKeys = getText(keys);
            byte [][][] results = new byte [allPlaintext.length][4][4];
            // encrypt
            if (args[0] == "-e"){
                results = encryptOrDecryption(allPlaintext,allKeys,true,0);
                results = encryptOrDecryption(results,allKeys,true,1);
                results = encryptOrDecryption(results,allKeys,true,2);
            }
            // decrypt
            if (args[0] == "-d") {
                results = encryptOrDecryption(allPlaintext, allKeys, false, 2);
                results = encryptOrDecryption(results, allKeys, false, 1);
                results = encryptOrDecryption(results, allKeys, false, 0);
            }
            writeOutputToFile(results,args[6]);
        }
        else{
            // get all the plaintext and split them into blocks
            File file = new File(args[2]);
            byte[] plaintext = Files.readAllBytes(file.toPath());
            byte [][][] allPlaintext = getText(plaintext);
            // get all the ciphertexts and split them into blocks
            file = new File(args[4]);
            byte[] ciphertext = Files.readAllBytes(file.toPath());
            byte [][][] allCiphertext= getText(ciphertext);
            byte [][][] keys = generateRandomKeys();

            byte [][][] c1 = stepOne(allPlaintext,keys[0]);
            byte [][][] c2 = stepOne(c1,keys[1]);
            getThirdKey(c2,allCiphertext,keys);
            writeOutputToFile(keys,args[6]);
        }
        check();
    }

    private static void check() throws IOException {
        File file = new File("C:\\Users\\איתן אביטן\\Downloads\\לימודים\\אבטחת מחשבים ורשתות תקשורת\\עבודות הגשה\\עבודה 1\\חלק ב\\AES3_test_files\\test");
        byte[] result = Files.readAllBytes(file.toPath());
        file = new File("C:\\Users\\איתן אביטן\\Downloads\\לימודים\\אבטחת מחשבים ורשתות תקשורת\\עבודות הגשה\\עבודה 1\\חלק ב\\AES3_test_files\\cipher_short");
        byte[] ciphetext = Files.readAllBytes(file.toPath());
        for (int i = 0; i < result.length; i++) {
            if (result[i] != ciphetext[i]) {
                System.out.println("bad");
                return;
            }
        }
        System.out.println("good");

    }

    private static void getThirdKey(byte[][][] c2, byte[][][] allCiphertext, byte[][][] keys) {
        for (int i = 0; i < c2.length; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    keys[2][j][k] = (byte) (c2[i][j][k] ^ allCiphertext[i][j][k]);
                }
            }
        }
    }

    /**
     *
     * @param bytes
     * @return
     */
    public static byte [][][] getText(byte[] bytes){
        int bytesIndex = 0;
        int numberOfBlocks = (bytes.length * 8) / 128;
        byte [][][] plainText = new byte[numberOfBlocks][4][4];
        for (int i = 0; i < plainText.length; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    plainText[i][k][j] = bytes[bytesIndex];
                    bytesIndex++;
                }
            }
        }
        return plainText;
    }

    private static void writeOutputToFile(byte[][][] results, String filePath) throws IOException {
        OutputStream outputStream = new FileOutputStream(filePath);
        for (int i = 0; i < results.length; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    outputStream.write(results[i][k][j]);
                }
            }
        }
    }

    /**
     *
     * @param allPlaintext
     * @param allKeys
     * @param isEncryption
     */
    private static byte[][][]  encryptOrDecryption(byte[][][] allPlaintext, byte[][][] allKeys,boolean isEncryption, int keyIndex) {
        byte[][][] results = new byte [allPlaintext.length][4][4];
        for (int i = 0; i < allPlaintext.length; i++) {
                if (isEncryption) {
                    shiftRows(allPlaintext[i], true);
                    results[i] = addRoundKey(allPlaintext[i], allKeys[keyIndex]);
                } else {
                    results[i] = addRoundKey(allPlaintext[i], allKeys[keyIndex]);
                    shiftRows(results[i], false);
                }
        }
        return results;
    }



    /**
     *
     * @param matrix
     * @param isEncryption
     */
    public static void shiftRows(byte [][] matrix, boolean isEncryption){

        int counter = 1;
        if (!isEncryption)
            counter = 3;

        for (int i = 1; i < matrix.length; i++) {
            byte [] shift = new byte [counter];
            int j = 0;
            while (j < counter){
                shift[j] = matrix[i][j];
                j++;
            }

            int k = 0;
            for (; k < matrix.length - counter; k++) {
                matrix[i][k] = matrix[i][j];
                j++;
            }
            j = 0;
            while (k < matrix.length){
                matrix[i][k] = shift[j];
                j++;
                k++;
            }
            if (!isEncryption)
                counter--;
            else
                counter++;

        }
    }

    /**
     *
     * @param matrix
     * @param keys
     * @return
     */
    public static byte[][] addRoundKey (byte[][]matrix, byte[][] keys){
        byte[][] xorMatrix =new byte[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                xorMatrix[i][j] = (byte) (matrix[i][j] ^ keys[i][j]);
        return  xorMatrix;
    }


    /**
     *
     * @return three dimension array contain 2 random keys
     */
    public static byte [][][]  generateRandomKeys(){
        byte [][][] keys = new byte[3][4][4];
        for (int i = 0; i < 3; i++) {
            keys [i] = new byte[4][4];
        }
        for (int i = 0; i < 2; i++) {
            SecureRandom random = new SecureRandom();
            byte [] key = new byte [16];// 128 bits are converted to 16 bytes;
            random.nextBytes(key);
            keys[i] = toTwoDimension(key);
        }
        return keys;
    }

    /**
     * This methos takes one dimensional array and convert it to two dimensional array
     * @param byteArray
     * @return
     */
    public static byte[][] toTwoDimension(byte [] byteArray){
        byte [][] twoDimensionArray = new byte[4][4];
        int counter = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                twoDimensionArray[j][i] = byteArray[counter];
                counter++;
            }
        }
        return twoDimensionArray;
    }

    public static byte [][][] stepOne (byte [][][] plainText, byte [][] key){
        byte [][][] c1 = new byte [plainText.length][4][4];
        int counter = 0;
        for (int j = 0; j < plainText.length; j++) {
            shiftRows(plainText[j], true);
            byte[][] temp = addRoundKey(plainText[j], key);
            c1[counter] = temp;
            counter++;
        }
        return c1;
    }


    public static void test(){
        byte [][] matrix = new byte[4][4];
        byte k = 1;
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("{");
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] = k;
                k++;
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("}");
        }
        System.out.println("*****************");
        shiftRows(matrix,true);
        printMatrix(matrix);
        System.out.println("*****************");
        shiftRows(matrix,false);
        printMatrix(matrix);

    }


    public static void printMatrix (byte [][]matrix){
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("{");
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("}");

        }
    }
}
