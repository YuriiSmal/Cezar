package com.cezar.ui;

import com.cezar.utils.CezarUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.cezar.utils.FileUtils.read;

public class CezarUI extends JFrame {

    private final JTextArea input = new JTextArea(5, 30);
    private final JTextArea output = new JTextArea(5, 30);
    private final JTextField keyField = new JTextField(5);

    public CezarUI() {
        setTitle("Cezar Cipher");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton encryptBtn = new JButton("Encrypt");
        JButton decryptBtn = new JButton("Decrypt");
        JButton bruteBtn = new JButton("Brute");

        encryptBtn.addActionListener(e -> handleEncryptDecrypt(true));
        decryptBtn.addActionListener(e -> handleEncryptDecrypt(false));
        bruteBtn.addActionListener(e -> showBruteForceDialog());

        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(new JLabel("Key:"));
        controlPanel.add(keyField);
        controlPanel.add(encryptBtn);
        controlPanel.add(decryptBtn);
        controlPanel.add(bruteBtn);

        add(new JScrollPane(input), BorderLayout.NORTH);
        add(controlPanel, BorderLayout.CENTER);
        add(new JScrollPane(output), BorderLayout.SOUTH);

        setVisible(true);
    }

    private void handleEncryptDecrypt(boolean isEncrypt) {
        String text = input.getText();
        String keyText = keyField.getText();

        if (text.isEmpty()) {
            showError("Input field can't be empty!");
            return;
        }

        if (keyText.isEmpty()) {
            showError("Key field can't be empty!");
            return;
        }

        try {
            int key = Integer.parseInt(keyText);
            String result = isEncrypt ? CezarUtils.encrypt(text, key) : CezarUtils.decrypt(text, key);
            handleResult(result);
        } catch (NumberFormatException ex) {
            showError("Key must be an Integer");
        }
    }

    private void showBruteForceDialog() {
        JDialog dialog = new JDialog(this, "Brute Force Reference Input", true);
        dialog.setSize(500, 300);
        dialog.setLayout(new BorderLayout());

        JTextArea inputArea = new JTextArea();
        JButton loadButton = new JButton("Load from file");
        JButton okButton = new JButton("OK");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(loadButton);
        buttonPanel.add(okButton);

        dialog.add(new JScrollPane(inputArea), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        loadButton.addActionListener(ev -> {
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showOpenDialog(dialog);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    Path path = chooser.getSelectedFile().toPath();
                    inputArea.setText(read(path));
                } catch (IOException ex) {
                    showError("Error reading file: " + ex.getMessage());
                }
            }
        });

        okButton.addActionListener(ev -> {
            String valueToDecrypt = input.getText();
            String frequencySample = inputArea.getText();
            String result = frequencySample.isEmpty()
                    ? CezarUtils.bruteForce(valueToDecrypt)
                    : CezarUtils.bruteForce(valueToDecrypt, frequencySample);
            handleResult(result);
            dialog.dispose();
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void handleResult(String result) {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Do you want to save the result to a file?",
                "Save Output",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save output to file");
            fileChooser.setSelectedFile(new File("output.txt")); // default name

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                Path fileToSave = fileChooser.getSelectedFile().toPath();
                try {
                    Files.writeString(fileToSave, result);
                    JOptionPane.showMessageDialog(this, "Result saved to: " + fileToSave);
                } catch (IOException ex) {
                    showError("Error saving file: " + ex.getMessage());
                }
            }
        } else {
            output.setText(result);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}