
public class main {


    public static void main(String[] args) {
        test();

        byte [] blocks = new byte[3];


    }


    public static void shiftRowsEncryption(byte [][] matrix, boolean isEncryption){

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

    public static void addRoundKey(byte [][]matrix, byte[][] key){

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
        shiftRowsEncryption(matrix,true);
        printMatrix(matrix);
        System.out.println("*****************");
        shiftRowsEncryption(matrix,false);
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
