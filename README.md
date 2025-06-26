# Cezar Encryption-Decryption App

## Features

- CLI and GUI support
- Encrypt / Decrypt using Cezar Cipher
- Brute-force decryption with optional frequency analysis
- Auto-detects alphabet (Ukrainian or English)
- Java 23, Gradle project structure

## Usage as command line: 
```bash
java -jar CezarApp.jar ENCRYPT "path/to/file.txt" 5
java -jar CezarApp.jar DECRYPT "path/to/file.txt" 5
java -jar CezarApp.jar BRUTE_FORCE "path/to/encrypted.txt" "path/to/reference.txt"
```
