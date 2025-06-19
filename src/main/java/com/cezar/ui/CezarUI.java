package com.cezar.ui;

import com.cezar.utils.CezarUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

import static com.cezar.utils.FileUtils.read;

public class CezarUI extends JFrame {

    public CezarUI() {
        setTitle("Caesar Cipher");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextArea input = new JTextArea(5, 30);
        JTextArea output = new JTextArea(5, 30);
        JTextField keyField = new JTextField(5);
        JButton encryptBtn = new JButton("Encrypt");
        JButton decryptBtn = new JButton("Decrypt");
        JButton brute = new JButton("Brute");

        encryptBtn.addActionListener(e -> {
            String text = input.getText().trim();
            String keyText = keyField.getText().trim();

            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Поле вводу тексту не може бути порожнім", "Помилка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (keyText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Поле ключа не може бути порожнім", "Помилка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int key = Integer.parseInt(keyText);
                output.setText(CezarUtils.encrypt(text, key));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ключ має бути цілим числом", "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        });

        decryptBtn.addActionListener(e -> {
            String text = input.getText().trim();
            String keyText = keyField.getText().trim();

            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Поле вводу тексту не може бути порожнім", "Помилка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (keyText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Поле ключа не може бути порожнім", "Помилка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int key = Integer.parseInt(keyText);
                output.setText(CezarUtils.decrypt(text, key));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ключ має бути цілим числом", "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        });

        brute.addActionListener(e -> {
            JDialog dialog = new JDialog((JFrame) null, "Brute Force Input", true);
            dialog.setSize(500, 300);
            dialog.setLayout(new BorderLayout());

            JTextArea inputArea = new JTextArea();
            JButton loadButton = new JButton("Load from file");
            JButton okButton = new JButton("OK");

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
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
                        JOptionPane.showMessageDialog(dialog, "Error reading file: " + ex.getMessage());
                    }
                }
            });

            okButton.addActionListener(ev -> {
                String valueToDecrypt = input.getText();
                String frequencySample = inputArea.getText();
                String result;
                if (frequencySample.isEmpty()) {
                    result = CezarUtils.bruteForce(valueToDecrypt);
                } else {
                    result = CezarUtils.bruteForce(valueToDecrypt, frequencySample);
                }

                output.setText(result);
                dialog.dispose();
            });

            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel("Key:"));
        panel.add(keyField);
        panel.add(encryptBtn);
        panel.add(decryptBtn);
        panel.add(brute);

        add(new JScrollPane(input), BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(new JScrollPane(output), BorderLayout.SOUTH);

        setVisible(true);
    }
}
