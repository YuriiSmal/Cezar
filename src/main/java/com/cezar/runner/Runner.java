package com.cezar.runner;

import com.cezar.cli.CLI;
import com.cezar.ui.CezarUI;
import com.cezar.utils.CezarUtils;
import com.cezar.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

public class Runner {
    public static void main(String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("cli")) {
            new CLI().run();
        } else if (args.length >= 2) {
            String command = args[0];
            String path = args[1];
            if (command.equalsIgnoreCase("BRUTE_FORCE")) {
                String ref = args.length >= 3 ? args[2] : "";
                String text = null;
                try {
                    text = FileUtils.read(Path.of(path));
                } catch (IOException e) {
                    System.out.println("Please check if it's correct path: " + path);
                    throw new RuntimeException(e);
                }
                if (ref.isEmpty()) {
                    CezarUtils.bruteForce(text);
                } else {
                    CezarUtils.bruteForce(text, ref);
                }

            } else if (args.length >= 3) {
                int key = Integer.parseInt(args[2]);
                switch (command.toUpperCase()) {
                    case "ENCRYPT" -> CezarUtils.process(path, key, true);
                    case "DECRYPT" -> CezarUtils.process(path, key, false);
                }
            } else {
                System.out.println("Key is required for ENCRYPT/DECRYPT.");
            }
        } else {
            new CezarUI();
        }
    }
}