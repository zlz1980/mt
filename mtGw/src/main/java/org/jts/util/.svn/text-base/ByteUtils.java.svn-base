package org.jts.util;

public class ByteUtils {

    // 16进制字符数组
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * 将4个字节的数组转换为整数
     * @param bytes 4个字节
     * @return 整数
     */
    public static int byteArrayToInt(byte[] bytes) {
        if (bytes.length != 4) {
            throw new IllegalArgumentException("Byte array must be 4 bytes long");
        }
        return (bytes[0] & 0xFF) << 24 |
                (bytes[1] & 0xFF) << 16 |
                (bytes[2] & 0xFF) << 8  |
                (bytes[3] & 0xFF);
    }

    /**
     * 将4个字节的数组转换为无符号整数
     * @param bytes 4个字节
     * @return 无符号整数
     */
    public static long byteArrayToUnsignedInt(byte[] bytes) {
        if (bytes.length != 4) {
            throw new IllegalArgumentException("Byte array must be 4 bytes long");
        }
        return ((bytes[0] & 0xFFL) << 24) |
                ((bytes[1] & 0xFFL) << 16) |
                ((bytes[2] & 0xFFL) << 8)  |
                (bytes[3] & 0xFFL);
    }

    /**
     * 将16进制字符串转换为字节数组
     * @param hex 十六进制字符串
     * @return 字节数组
     */
    public static byte[] heToBytes(String hex) {
        int len = hex.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("十六进制字符串长度必须为偶数");
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 将第一个字符左移4位，然后加上第二个字符的值
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * 将字节数组转换为16进制字符串
     * @param bytes 字节数组
     * @return 16进制字符串
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int val = bytes[i] & 0xFF;
            // 高4位
            hexChars[i*2] = HEX_ARRAY[val >>> 4];
            // 低4位
            hexChars[i*2+1] = HEX_ARRAY[val & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value
        };
    }
}
