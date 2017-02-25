package ru.cwl.mailarc.parser.p;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;

/**
 * Created by vad on 11.04.2015 18:31.
 */
public class CRC {
    static String crc0(byte b[]) {
        return "";
    }

    static String crcAdler32(byte b[]) {
        Adler32 crc = new Adler32();
        crc.update(b);
        return String.format("%X", crc.getValue());
    }

    public static String crcSHA1(byte b[]) {
        MessageDigest md = getMessageDigest("SHA-1");
        md.update(b);
        return getHexString(md.digest());
    }

    static String crcSHA256(byte b[]) {
        // 1:   6900233EA9B101E7B91429DD0F9E3D4E022A5ABB
        // 256: 0748E34F2F40956EBE7ADEF869926B6A89EA3E230B8747A2512A6C1803F015A5
        // 512: 779052587260C28BB43EE0FA77A7F972FF8FB17E46809301E39B6D3B351C3DA3F565B72DAA6FEB97B4F46176B889273EECC18371D90B053275697A5D4E33F55C
        MessageDigest md = getMessageDigest("SHA-256");
        md.update(b);
        return getHexString(md.digest());
    }

    static private MessageDigest getMessageDigest(String hashAlgo) {
        try {
            return MessageDigest.getInstance(hashAlgo);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    private static String getHexString(byte[] mdbytes) {
        StringBuffer hexString = new StringBuffer();
        for (byte mdbyte : mdbytes) {
            hexString.append(String.format("%02X", (0xFF & mdbyte)));
        }
        return hexString.toString();
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

}
