package com.cezar.cli;

import com.cezar.utils.CezarUtils;
import com.cezar.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class CLI {
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter command (ENCRYPT/DECRYPT/BRUTE_FORCE/EXIT): ");
            String command = scanner.nextLine().trim();
            if (command.equalsIgnoreCase("EXIT")) break;

            System.out.print("Enter file path: ");
            String path = scanner.nextLine().trim();

            if (command.equalsIgnoreCase("BRUTE_FORCE")) {
                System.out.print("Enter reference file path or leave empty: ");
                String ref = scanner.nextLine().trim();
                String toDecrypt;
                try {
                    toDecrypt = FileUtils.read(Path.of(path));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (ref.isEmpty()) {
                    CezarUtils.bruteForce(toDecrypt);
                } else {
                    CezarUtils.bruteForce(toDecrypt, ref);
                }

            } else {
                System.out.print("Enter key: ");
                int key = Integer.parseInt(scanner.nextLine().trim());
                switch (command.toUpperCase()) {
                    case "ENCRYPT" -> CezarUtils.process(path, key, true);
                    case "DECRYPT" -> CezarUtils.process(path, key, false);
                }
            }

            System.out.print("Do you wanna continue? Type Yes or No ...");
            if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                break;
            }
        }
    }
}