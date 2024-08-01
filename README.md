# Token2 PIN+ Release2 FIDO2 Security Keys - Java Applet Source Code
<img height="100"    src="https://www.token2.com/img/blinking-fido2.gif" align="right">
Welcome to the official GitHub repository for our PIN+ Release2 FIDO2 Security Keys Java Applet source code! This repository is intended to make our source code publicly available for review, research, and educational purposes.

## Overview

This repository contains the source code for the Java Applet used in our FIDO2.1. security keys with PIN complexity enforcement (PIN+) and the 300 passkey storage capacity (Release 2). The goal is to provide a transparent view of our implementation and to contribute to the broader community of security key developers and researchers.



## Storage Architecture

The applet uses a structured approach to manage passkey storage, designed for efficiency given JavaCard's limitations. Specifically, the PIN+ R2 300 passkey management is implemented as follows:

- The `short` data type on JavaCard allows for a maximum of 32,767 bytes.
- Each passkey requires approximately 400 bytes of storage.
- To accommodate 300 passkeys, they are divided into 6 classes, with each class containing 50 passkeys.

The storage for each class is allocated as:

- **1st 50 passkeys:** `new[20000]`
- **2nd 50 passkeys:** `new[20000]`
- **3rd 50 passkeys:** `new[20000]`
- **4th 50 passkeys:** `new[20000]`
- **5th 50 passkeys:** `new[20000]`
- **6th 50 passkeys:** `new[20000]`

Each class's allocation (`20000 bytes`) fits within the `short` limit, allowing for efficient storage management and retrieval.


## PIN Complexity Implementation

The `CustomFunction.java` file implements the PIN complexity rules for the FIDO2 security keys. Here’s an explanation of the main methods used:

- **IsCorrectDelta**: This method checks if the differences between consecutive characters in the PIN are not identical. This helps prevent simple patterns like "1234" or "abcd".

  ```java
  public static boolean IsCorrectDelta(byte[] ps1Input, short s2Offset, short s2Len) {
      final byte s1Delta = (byte)(ps1Input[s2Offset] - ps1Input[s2Offset + 1]);
      for (short i = s2Offset + 1; i < s2Offset + s2Len - 1; i++) {
          if ((byte)(ps1Input[i] - ps1Input[i + 1]) != s1Delta) {
              return true;
          }
      }
      return false;
  }
  ```

- **IsNonMirrored**: This function checks that the PIN is not a mirror of itself, i.e., it prevents palindromic PINs like "1221" or "abccba".

  ```java
  public static boolean IsNonMirrored(byte[] ps1Input, short s2Offset, short s2Len) {
      short s2Head = s2Offset;
      short s2Tail = (short)(s2Offset + s2Len - 1);
      while (s2Tail > s2Head) {
          if (ps1Input[s2Head] != ps1Input[s2Tail]) {
              return true;
          }
          s2Head++;
          s2Tail--;
      }
      return false;
  }
  ```

- **IsNonContinuousChar**: This method ensures that there are no sequences of the same character repeated more than a specified number of times. For example, it prevents "aaaa" or "1111".

  ```java
  public static boolean IsNonContinuousChar(byte[] ps1Input, short s2Offset, short s2Len, short s2MaxTimes) {
      byte s1CurChar = ps1Input[s2Offset];
      short s2CurTimes = 1;
      for (short i = s2Offset + 1; i < s2Offset + s2Len; i++) {
          if (ps1Input[i] == s1CurChar) {
              s2CurTimes++;
              if (s2CurTimes > s2MaxTimes) {
                  return false;
              }
          } else {
              s1CurChar = ps1Input[i];
              s2CurTimes = 1;
          }
      }
      return true;
  }
  ```

- **IsCorrectFormat**: This function checks that the PIN contains a mix of character types, such as uppercase letters, lowercase letters, digits, and other characters, and verifies the number of distinct character types used.

  ```java
  public static boolean IsCorrectFormat(byte[] ps1Input, short s2Offset, short s2Len, byte s1Type, short s2TypeNum) {
      byte s1CurType = 0x00;
      short s2CurTypeNum = 0;
      for (short i = s2Offset; i < s2Offset + s2Len; i++) {
          byte s1TempType = GetFormatType(ps1Input[i]);
          if (s1TempType == INVALID_CHAR) {
              return false;
          }
          if ((s1Type & s1TempType) == 0) {
              return false;
          }
          if ((s1CurType & s1TempType) == 0) {
              s1CurType |= s1TempType;
              s2CurTypeNum++;
          }
      }
      return s2CurTypeNum >= s2TypeNum;
  }
  ```

- **GetFormatType**: This helper method determines the type of a character (uppercase letter, lowercase letter, digit, or other).

  ```java
  public static byte GetFormatType(byte s1Char) {
      if (s1Char >= 0x41 && s1Char <= 0x5A) {
          return UPPER_LETTER;
      } else if (s1Char >= 0x61 && s1Char <= 0x7A) {
          return LOWER_LETTER;
      } else if (s1Char >= 0x30 && s1Char <= 0x39) {
          return DIGIT_CHAR;
      } else if (s1Char >= 0x20 && s1Char <= 0x7E) {
          return OTHER_CHAR;
      } else {
          return INVALID_CHAR;
      }
  }
  ```



## Important Notes

### Hardware Compatibility

While the provided code can be compiled and installed on standard JavaCard-compliant smart cards, **specific hardware chips might require** tweaks to ensure proper functionality. The behavior of the applet can vary depending on the JavaCard platform, card firmware, and chip characteristics. This code was implemented and tested on THD 89 and J3H145 (NXP JCOP3) chips only.

### USB and NFC Functionality
Some of our FIDO2 security keys support both NFC and USB interfaces. The USB bridging is handled by a third-party solution, and the manufacturer has not released the source code. However, Linux users can use the Linux Virtual USB-HID FIDO2 device project to access USB functionality:  [FIDO2 HID Bridge](https://github.com/BryanJacobs/fido2-hid-bridge/) .

### Support and Maintenance

Please note that our team is currently unable to offer support for installation or troubleshooting due to capacity constraints. We encourage the community to experiment with the code and share any findings or improvements, but we cannot provide direct assistance with:

- Debugging installation issues.
- Modifying the code for specific hardware compatibility.
- General troubleshooting.

We appreciate your understanding.

## License

This project is licensed under the  MIT License - see the LICENSE file for details.

## Acknowledgements

Portions of this code are used from the [FIDO2Applet](https://github.com/BryanJacobs/FIDO2Applet) project. We extend our thanks to the authors and contributors of this project.

## Disclaimer

The code is provided "as-is" and without warranty of any kind. Users are responsible for ensuring the security and suitability of the code for their specific use case.

## Future Enhancements and Code Scope
The actual code running on our devices may include future enhancements, such as additional applets like OpenPGP and increased storage capabilities. The code provided in this repository specifically pertains to the PIN+ Release2 series. Future developments and updates may not be reflected here. If we decide to open-source future releases, they may be hosted in separate repositories.

## Contact

For inquiries related to this project, please reach out via the contact form: [contact us](https://token2.com/contact).

Thank you for your interest in our project!
