import com.cezar.utils.CezarUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CezarUtilsTest {

    @Test
    void testEncryptAndDecryptEnglish() {
        String original = "hello, world!";
        int key = 6;

        String encrypted = CezarUtils.encrypt(original, key);
        String decrypted = CezarUtils.decrypt(encrypted, key);

        assertEquals(original, decrypted, "Decryption should return original English text");
    }

    @Test
    void testEncryptAndDecryptUkrainian() {
        String original = "Привіт, Світ!";
        int key = 3;

        String encrypted = CezarUtils.encrypt(original, key);
        String decrypted = CezarUtils.decrypt(encrypted, key);

        assertEquals(original, decrypted, "Decryption should return original Ukrainian text");
    }

    @Test
    void testBruteForceEnglish() {
        String reference = "This is a test sentence used as frequency sample.";
        String text = "Secret message";
        int key = 7;

        String encrypted = CezarUtils.encrypt(text, key);
        String result = CezarUtils.bruteForce(encrypted, reference);

        assertTrue(result.contains(text), "Brute force with reference should recover the original text");
    }

    @Test
    void testShiftWrapAround() {
        String alphabet = "abc";
        String text = "cab";
        int key = 1;

        String result = callPrivateShift(text, key, alphabet);
        assertEquals("abc", result);
    }

    @Test
    void testDetectAlphabetEnglish() {
        String text = "The quick brown fox jumps over the lazy dog.";
        String alphabet = callPrivateDetectAlphabet(text);
        assertTrue(alphabet.startsWith("abcdefghijklmnopqrstuvwxyz"));
    }

    @Test
    void testDetectAlphabetUkrainian() {
        String text = "Швидкий бурий лис стрибає через лінивого пса.";
        String alphabet = callPrivateDetectAlphabet(text);
        assertTrue(alphabet.startsWith("абвгґдеєжзиіїйклмнопрстуфхцчшщьюя"));
    }

    // ==== ✳ Private method helpers ==== //

    private String callPrivateShift(String text, int key, String alphabet) {
        try {
            var method = CezarUtils.class.getDeclaredMethod("shift", String.class, int.class, String.class);
            method.setAccessible(true);
            return (String) method.invoke(null, text, key, alphabet);
        } catch (Exception e) {
            throw new RuntimeException("Unable to call shift", e);
        }
    }

    private String callPrivateDetectAlphabet(String text) {
        try {
            var method = CezarUtils.class.getDeclaredMethod("detectAlphabet", String.class);
            method.setAccessible(true);
            return (String) method.invoke(null, text);
        } catch (Exception e) {
            throw new RuntimeException("Unable to call detectAlphabet", e);
        }
    }
}
