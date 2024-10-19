package org.java;


public class DESManual {

    private final String data;

    public DESManual(String data) {
        this.data = data;
    }

    public byte[] getEncriptado(){
        String key = "ok:uo1IN";
        byte[] bytesLLave = key.getBytes();// Convert key to byte array
        byte[] bytesData = data.getBytes(); // Convert data to byte array

        // Encrypt and Decrypt using DES
        return Encriptar(bytesData, bytesLLave);
    }

    private static final int[] tablaParidad = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    private static final int[] tablaShift = {
            1, 1, 2, 2, 2, 2, 2, 2,
            1, 2, 2, 2, 2, 2, 2, 1
    };

    private static final int[] tablaCompresionLlave = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    private static final int[] tablaPermutacionInicial = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    private static final int[] tablaPermutacionExpansion = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    private static final int[] tablaPermutacionPbox = {
            16, 7, 20, 21, 29, 12, 28, 17,
            1, 15, 23, 26, 5, 18, 31, 10,
            2, 8, 24, 14, 32, 27, 3, 9,
            19, 13, 30, 6, 22, 11, 4, 25
    };

    private static final int[] tablaPermutacionFinal = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };

    private static final int[][][] tablaSboxes = {
            { // S1
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },
            { // S2
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            },
            { // S3
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },
            { // S4
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            },
            { // S5
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            },
            { // S6
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
            },
            { // S7
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            { // S8
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
    };

    /**
     * Este método procesa los datos utilizando el algoritmo DES (Data Encryption Standard).
     * Realiza las operaciones de permutación, sustitución con S-boxes y múltiples rondas de procesamiento,
     * siguiendo las especificaciones del algoritmo, ya sea para encriptar o desencriptar los datos de entrada.
     */
    private static byte[] ProcesarDes(byte[] dataEntrada, byte[] llaveEncriptacion) {
        byte[] dataProcesada = new byte[dataEntrada.length];
        int conteoBloques = dataEntrada.length / 8;
        byte[][] llavesRondas = GenerateKeys(llaveEncriptacion);
        byte[] bloqueBuffer = new byte[8];
        byte[] mitadIzq = new byte[4];
        byte[] mitadDer = new byte[4];
        byte[] derechaExpandida;
        byte[] mitadDerSub = new byte[4];
        byte[] tempMitadDer;

        for (int bloque = 0; bloque < conteoBloques; bloque++) {
            System.arraycopy(dataEntrada, bloque * 8, bloqueBuffer, 0, 8);
            bloqueBuffer = Permutar(bloqueBuffer, tablaPermutacionInicial);

            for (int ronda = 0; ronda < 16; ronda++) {
                // Split block into left and right halves
                System.arraycopy(bloqueBuffer, 0, mitadIzq, 0, 4);
                System.arraycopy(bloqueBuffer, 4, mitadDer, 0, 4);

                derechaExpandida = Permutar(mitadDer, tablaPermutacionExpansion);

                derechaExpandida = XOR(derechaExpandida, llavesRondas[ronda]);

                for (int section = 0; section < 8; section++) {
                    int valorSBox = getValorBox(derechaExpandida, section);
                    for (int bitIndex = 0; bitIndex < 4; bitIndex++) {
                        SetBitAt(mitadDerSub, section * 4 + bitIndex, (valorSBox >> (3 - bitIndex)) & 1);
                    }
                }

                mitadDerSub = Permutar(mitadDerSub, tablaPermutacionPbox);

                tempMitadDer = XOR(mitadIzq, mitadDerSub);

                if (ronda != 15) {
                    System.arraycopy(mitadDer, 0, bloqueBuffer, 0, 4);
                    System.arraycopy(tempMitadDer, 0, bloqueBuffer, 4, 4);
                } else {
                    System.arraycopy(tempMitadDer, 0, bloqueBuffer, 0, 4);
                    System.arraycopy(mitadDer, 0, bloqueBuffer, 4, 4);
                }
            }

            bloqueBuffer = Permutar(bloqueBuffer, tablaPermutacionFinal);
            System.arraycopy(bloqueBuffer, 0, dataProcesada, bloque * 8, 8);
        }

        return dataProcesada;
    }

    private static int getValorBox(byte[] expandedRightHalf, int section) {
        int row = (GetBitAt(expandedRightHalf, section * 6) << 1) | GetBitAt(expandedRightHalf, section * 6 + 5);
        int column = 0;
        for (int bitIndex = 0; bitIndex < 4; bitIndex++) {
            column |= GetBitAt(expandedRightHalf, section * 6 + bitIndex + 1) << (3 - bitIndex);
        }

        return tablaSboxes[section][row][column];
    }

    public static byte[] Encriptar(byte[] data, byte[] key) {
        if (key.length != 8) {
            throw new IllegalArgumentException("La llave debe de ser minimo de 8 bytes");
        }

        data = AddPkcs7Padding(data, 8);

        return ProcesarDes(data, key);
    }

    private static byte[][] GenerateKeys(byte[] LlaveInicial) {
        byte[][] llavesRondas = new byte[16][];
        byte[] llavePermutada = Permutar(LlaveInicial, tablaParidad);

        for (int round = 0; round < 16; round++) {
            byte[] mitadIzq = SelectBits(llavePermutada, 0);
            byte[] mitadDer = SelectBits(llavePermutada, 28);

            mitadIzq = LeftShift(mitadIzq, tablaShift[round]);
            mitadDer = LeftShift(mitadDer, tablaShift[round]);

            byte[] llaveCombinada = JoinKey(mitadIzq, mitadDer);
            llavesRondas[round] = Permutar(llaveCombinada, tablaCompresionLlave);
            llavePermutada = llaveCombinada;
        }

        return llavesRondas;
    }

    private static byte[] Permutar(byte[] source, int[] table) {
        int length = (table.length - 1) / 8 + 1;
        byte[] resultado = new byte[length];

        for (int i = 0; i < table.length; i++) {
            SetBitAt(resultado, i, GetBitAt(source, table[i] - 1));
        }

        return resultado;
    }

    private static byte[] LeftShift(byte[] data, int shift) {
        byte[] outer = new byte[(28 - 1) / 8 + 1];

        for (int i = 0; i < 28; i++) {
            int val = GetBitAt(data, (i + shift) % 28);
            SetBitAt(outer, i, val);
        }

        return outer;
    }

    private static byte[] XOR(byte[] first, byte[] second) {
        byte[] resultado = new byte[first.length];

        for (int i = 0; i < first.length; i++) {
            resultado[i] = (byte)(first[i] ^ second[i]);
        }

        return resultado;
    }

    private static int GetBitAt(byte[] data, int position) {
        int posByte = position / 8;
        int posBit = position % 8;
        return (data[posByte] >> (7 - posBit)) & 1;
    }

    private static void SetBitAt(byte[] data, int position, int value) {
        int posByte = position / 8;
        int posBit = position % 8;

        if (value == 1) {
            data[posByte] |= (byte)(1 << (7 - posBit));
        } else {
            data[posByte] &= (byte) ~(1 << (7 - posBit));
        }
    }

    private static byte[] SelectBits(byte[] source, int start) {
        byte[] result = new byte[(28 - 1) / 8 + 1];

        for (int i = 0; i < 28; i++) {
            SetBitAt(result, i, GetBitAt(source, start + i));
        }

        return result;
    }

    private static byte[] JoinKey(byte[] leftHalf, byte[] rightHalf) {
        byte[] result = new byte[7];

        System.arraycopy(leftHalf, 0, result, 0, 3);

        for (int i = 0; i < 4; i++) {
            int val = GetBitAt(leftHalf, 24 + i); // 24-27
            SetBitAt(result, 24 + i, val);
        }

        for (int i = 0; i < 28; i++) {
            int val = GetBitAt(rightHalf, i);
            SetBitAt(result, 28 + i, val);
        }

        return result;
    }

    public static byte[] AddPkcs7Padding(byte[] data, int blockSize) {
        if (data == null) {
            throw new NullPointerException("Data no puede ser nulo");
        }

        if (blockSize <= 0) {
            throw new IllegalArgumentException("El tamaño del bloque debe ser mayor a 0");
        }

        int count = data.length;
        int paddingRemainder = count % blockSize;
        int paddingSize = blockSize - paddingRemainder;

        if (paddingSize == 0) {
            paddingSize = blockSize;
        }

        byte[] paddedData = new byte[data.length + paddingSize];
        System.arraycopy(data, 0, paddedData, 0, data.length);

        byte paddingByte = (byte) paddingSize;
        for (int i = data.length; i < paddedData.length; i++) {
            paddedData[i] = paddingByte;
        }

        return paddedData;
    }

}


