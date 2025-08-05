package ch.token2.fido2;



public class CustomFunction {

    public static final byte UPPER_CASE = 0x01;
    public static final byte LOWER_CASE = 0x02;
    public static final byte NUMBER = 0x03;
    public static final byte SPECIAL_CHAR = 0x04;
    public static final byte OTHER_CHAR = 0x05;

    // Accepts Unicode characters
    public static byte GetFormatType(char ch) {
        if (Character.isUpperCase(ch)) return UPPER_CASE;
        else if (Character.isLowerCase(ch)) return LOWER_CASE;
        else if (Character.isDigit(ch)) return NUMBER;
        else if (isSpecialCharacter(ch)) return SPECIAL_CHAR;
        else return OTHER_CHAR;
    }

    private static boolean isSpecialCharacter(char ch) {
        // Define what you consider "special" — basic example:
        return "!@#$%^&*()_+-=[]{}|;:'\",.<>?/`~\\"
                .indexOf(ch) >= 0;
    }

    // Updated to handle Unicode input
    public static boolean IsCorrectFormat(byte[] ps1Input, short s2Offset, short s2Len, byte s1Type, short s2TypeNum) {
        try {
            String pin = new String(ps1Input, s2Offset, s2Len, "UTF-8");

            boolean hasUpper = false;
            boolean hasLower = false;
            boolean hasNumber = false;
            boolean hasSpecial = false;
            boolean hasOther = false;

            for (int i = 0; i < pin.length(); i++) {
                char ch = pin.charAt(i);
                byte type = GetFormatType(ch);
                switch (type) {
                    case UPPER_CASE: hasUpper = true; break;
                    case LOWER_CASE: hasLower = true; break;
                    case NUMBER:     hasNumber = true; break;
                    case SPECIAL_CHAR: hasSpecial = true; break;
                    case OTHER_CHAR: hasOther = true; break;
                }
            }

            int typeCount = 0;
            if (hasUpper) typeCount++;
            if (hasLower) typeCount++;
            if (hasNumber) typeCount++;
            if (hasSpecial) typeCount++;
            if (hasOther) typeCount++;

            return typeCount >= s2TypeNum;

        } catch (Exception e) {
            return false;
        }
    }
}

