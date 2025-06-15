🔐 FileEncryptorSwing
A simple Java Swing-based GUI application to encrypt and decrypt files or folders using AES (Advanced Encryption Standard). Ideal for personal use to secure sensitive data.

✅ Encrypt/Decrypt individual files

✅ Encrypt/Decrypt entire folders

✅ Auto-generates and saves AES key securely alongside encrypted files

✅ Simple, lightweight Java Swing interface

🛠️ Technologies Used
Java SE 8

Java Swing for GUI

Java Cryptography Extension (JCE) for AES encryption

FileEncryptorSwing/
├── GUI
├── src/
│   └── FileEncryptorSwing.java
├── .gitignore
├── Readme.md
├── FileEncryptorSwing.ctxt
└── .vscode/
    └── settings.json

🧪 How to Run
🔧 Compile

javac -d bin src/FileEncryptorSwing.java   

▶️ Run 

java -cp bin FileEncryptorSwing
⚠️ Make sure javac and java are available in your system’s PATH. 

📎 Notes
Encrypted files get overwritten with encrypted content.

Keys are saved in the same directory as <filename>_key.txt.

Keep the key file safe — it is essential for decryption.
