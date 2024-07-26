# Token2 PIN+ Release2 FIDO2 Security Keys - Java Applet Source Code

Welcome to the official GitHub repository for our PIN+ Release2 FIDO2 Security Keys Java Applet source code! This repository is intended to make our source code publicly available for review, research, and educational purposes.

## Overview

This repository contains the source code for the Java Applet used in our FIDO2.1.final security keys with PIN complexity enforcement (PIN+). The goal is to provide a transparent view of our implementation and to contribute to the broader community of security key developers and researchers.



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





## Important Notes

### Hardware Compatibility

While the provided code can be compiled and installed on standard JavaCard-compliant smart cards, **specific hardware chips might require** tweaks to ensure proper functionality. The behavior of the applet can vary depending on the JavaCard platform, card firmware, and chip characteristics. This code was implemented and tested on THD 89 and J3H145 (NXP JCOP3) chips only.

### Support and Maintenance

Please note that our team is currently unable to offer support for installation or troubleshooting due to capacity constraints. We encourage the community to experiment with the code and share any findings or improvements via pull requests or issues, but we cannot provide direct assistance with:

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

## Contact

For inquiries related to this project, please reach out via the contact form: [contact us](https://token2.com/contact).

Thank you for your interest in our FIDO2 Security Key project!
