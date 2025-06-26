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
            System.out.print("Enter one of the next commands:  ENCRYPT | DECRYPT | BRUTE_FORCE | EXIT ");
            String command = scanner.nextLine().trim();
            if (command.equalsIgnoreCase("EXIT")) break;

            System.out.print("Enter the source file path: ");
            String path = scanner.nextLine().trim();

            String source;
            try {
                source = FileUtils.read(Path.of(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (command.equalsIgnoreCase("BRUTE_FORCE")) {
                System.out.print("Enter reference file path or leave empty for default BRUTE_FORCE action: ");
                String refPath = scanner.nextLine().trim();

                String referenceFileText = "";
                try {
                    referenceFileText = FileUtils.read(Path.of(refPath));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Will continue with default BRUTE_FORCE flow: ");
                }

                if (referenceFileText.isEmpty()) {
                    CezarUtils.bruteForce(source);
                } else {
                    CezarUtils.bruteForce(source, refPath);
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
            if (scanner.nextLine().trim().equalsIgnoreCase("no")) {
                break;
            }
        }
    }
}