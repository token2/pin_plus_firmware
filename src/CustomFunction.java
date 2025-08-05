package ch.token2.fido2;

import java.nio.charset.StandardCharsets;

public class CustomFunction {

    public static final byte UPPER_LETTER = 0x01;
    public static final byte LOWER_LETTER = 0x02;
    public static final byte DIGIT_CHAR = 0x04;
    public static final byte OTHER_CHAR = 0x08;
    public static final byte INVALID_CHAR = (byte) 0x80;

    // === Complexity & format validation ===
    public static boolean IsCorrectFormat(byte[] ps1Input, short s2Offset, short s2Len, byte s1Type, short s2TypeNum) {
        try {
            String pin = new String(ps1Input, s2Offset, s2Len, StandardCharsets.UTF_8);

            byte s1CurType = 0x00;
            short s2CurTypeNum = 0;

            for (int i = 0; i < pin.length(); i++) {
                byte s1TempType = GetFormatType(pin.charAt(i));
                if (s1TempType == INVALID_CHAR) return false;
                if ((s1Type & s1TempType) == 0x00) return false;
                if ((s1CurType & s1TempType) == 0x00) {
                    s1CurType |= s1TempType;
                    s2CurTypeNum++;
                }
            }

            return s2CurTypeNum >= s2TypeNum;

        } catch (Exception e) {
            return false;
        }
    }

    public static byte GetFormatType(char ch) {
        if (Character.isUpperCase(ch)) {
            return UPPER_LETTER;
        } else if (Character.isLowerCase(ch)) {
            return LOWER_LETTER;
        } else if (Character.isDigit(ch)) {
            return DIGIT_CHAR;
        } else if (isOtherAllowed(ch)) {
            return OTHER_CHAR;
        } else {
            return INVALID_CHAR;
        }
    }

    private static boolean isOtherAllowed(char ch) {
        // Allow any visible non-control character
        return !Character.isISOControl(ch) && !Character.isSurrogate(ch);
    }

    // === Repetition Check: No more than s2MaxTimes same char in a row ===
    public static boolean IsNonContinuousChar(byte[] ps1Input, short s2Offset, short s2Len, short s2MaxTimes) {
        try {
            String pin = new String(ps1Input, s2Offset, s2Len, StandardCharsets.UTF_8);
            if (pin.isEmpty()) return true;

            char prevChar = pin.charAt(0);
            short count = 1;

            for (int i = 1; i < pin.length(); i++) {
                char ch = pin.charAt(i);
                if (ch == prevChar) {
                    count++;
                    if (count > s2MaxTimes) return false;
                } else {
                    prevChar = ch;
                    count = 1;
                }
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // === Delta Check: no constant increments like "abcd" or "4321" ===
    public static boolean IsCorrectDelta(byte[] ps1Input, short s2Offset, short s2Len) {
        try {
            String pin = new String(ps1Input, s2Offset, s2Len, StandardCharsets.UTF_8);
            if (pin.length() < 2) return true;

            int delta = pin.charAt(0) - pin.charAt(1);
            for (int i = 1; i < pin.length() - 1; i++) {
                int currentDelta = pin.charAt(i) - pin.charAt(i + 1);
                if (currentDelta != delta) return true;
            }

            return false; // Pattern was sequential
        } catch (Exception e) {
            return false;
        }
    }

    // === Mirror Check: not a palindrome ===
    public static boolean IsNonMirrored(byte[] ps1Input, short s2Offset, short s2Len) {
        try {
            String pin = new String(ps1Input, s2Offset, s2Len, StandardCharsets.UTF_8);
            int len = pin.length();

            for (int i = 0; i < len / 2; i++) {
                if (pin.charAt(i) != pin.charAt(len - 1 - i)) {
                    return true;
                }
            }

            return false; // It's a palindrome
        } catch (Exception e) {
            return false;
        }
    }
}
