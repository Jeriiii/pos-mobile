package pos.android.User;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Kódování a dekódování řetězce.
 *
 * Created by Petr on 16.6.2015.
 */
public class Coder {

    /**
     * Zakodování řetězce.
     * @param plainText Řetězec co se má zakódovat.
     * @return Zakódovaný řetězec.
     */
    public static String encode(String plainText) {
        byte[] data = new byte[0];
        try {
            data = plainText.getBytes("UTF-8");
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            return  base64;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Dekódování řetězce.
     * @param base64 Řetězec co se má rozkódovat.
     * @return Řetězec v původním formátu.
     */
    public static String decode(String base64) {
        // Receiving side
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        try {
            String text = new String(data, "UTF-8");
            return text;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
