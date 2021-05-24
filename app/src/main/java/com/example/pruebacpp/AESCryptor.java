package com.example.pruebacpp;

public class AESCryptor {
    static {
        try {
            System.loadLibrary("native-lib");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static final int ENCRYPT = 0;

    public static final int DECRYPT = 1;

    public native static byte[] crypt(byte[] data, int mode, byte[] cl,int keysize);
    public native static byte[] read(String path, long time);
    public native static double[] memory();

    /**
     *
     *
     * @param hexStr
     * @return
     */
    public static byte[] hexStr2Bytes(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static String bytes2HexStr(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            if (((int) data[i] & 0xff) < 0x10) { /* & 0xffè½¬æ¢æ— ç¬¦å·æ•´åž‹ */
                buf.append("0");
            }
            buf.append(Long.toHexString((int) data[i] & 0xff)); /* è½¬æ¢16è¿›åˆ¶,ä¸‹æ–¹æ³•åŒ */
        }
        return buf.toString();
    }


}
