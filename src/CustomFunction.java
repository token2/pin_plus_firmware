package ch.token2.fido2;

import java.nio.charset.StandardCharsets;

public class CustomFunction {

    public static final byte UPPER_LETTER = 0x01;
    public static final byte LOWER_LETTER = 0x02;
    public static final byte DIGIT_CHAR = 0x04;
    public static final byte OTHER_CHAR = 0x08;
    public static final byte INVALID_CHAR = (byte) 0x80;

    // Main complexity and character category check
    public static boolean IsCorrectFormat(byte[] ps1Input, short s2Offset, short s2Len, byte s1Type, short s2TypeNum) {
        try {
            String pin = new String(ps1Input, s2Offset, s2Len, StandardCharsets.UTF_8);

            byte s1CurType = 0x00;
            short s2CurTypeNum = 0;

            for (int i = 0; i < pin.length(); i++) {
                byte s1TempType = GetFormatType(pin.charAt(i));
                if (s1TempType == INVALID_CHAR) {
                    return false;
                }
                if ((s1Type & s1TempType) == 0x00) {
                    return false;
                }
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

    // Updated to support Unicode characters
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

    // Define what you consider "other allowed" characters (e.g., symbols, emoji, etc.)
    private static boolean isOtherAllowed(char ch) {
        // Allow everything printable that's not a letter/digit
        // Adjust this as needed
        return !Character.isISOControl(ch) && !Character.isSurrogate(ch);
    }
}
