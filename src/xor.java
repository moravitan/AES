public class xor {
    public static byte[][] xorFunction(byte[][]matrix, byte[][] keys){
        byte[][] xorMatrix =new byte[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                xorMatrix[i][j] = (byte) (matrix[i][j] ^ keys[i][j]);
        return  xorMatrix;
    }

    public static void test(){
        byte[][] matrix = new byte[4][4];
        matrix = new byte[][]{{1, 1, 1, 1}, {0, 1, 0, 1}, {0, 0, 0, 0}, {0, 0, 1, 1}};
        byte[][]key = new byte[4][4];
        key = new byte[][]{{1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {0, 0, 1, 1}};
        byte[][] temp = xorFunction(matrix, key);
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                System.out.print(temp[i][j]);


    }

    public static void main(String[] args) {
        test();
    }
}

