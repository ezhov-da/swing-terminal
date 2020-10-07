package com.redpois0n.terminal;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class DebugTerminal1 {

    private static Process p;
    private static PrintWriter writer;
    private static JTerminal terminal;

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        terminal = new JTerminal();

        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(terminal);


        terminal.addInputListener(new InputListener() {
            @Override
            public void processCommand(JTerminal terminal, char c) {
//				append(c);
            }

            @Override
            public void onTerminate(JTerminal terminal) {
                try {
                    if (p != null) {
                        p.destroy();
                    }
//					startShell();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("monospaced", Font.PLAIN, 14));

        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    startShell(textArea.getText());
                }
            }
        });


        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(terminal.getKeyListener());
        frame.add(textArea, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(675, 300);
        frame.setVisible(true);

//		terminal.append("JTerminal Test\n");
//		terminal.append("Debug and Example\n\n");

//		startShell();

//		append("bash test.sh\n");
    }

    public static void startShell(String command) {
        try {
            String shell = "D:\\programs\\Git-2.28.0-64\\bin\\sh.exe";

            ProcessBuilder builder = new ProcessBuilder(shell);
            builder.redirectErrorStream(true);
            p = builder.start();
            writer = new PrintWriter(p.getOutputStream(), true);
            writer.println(command);

            new Thread(() -> {
                try {
                    while (true) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                        String line;

                        while ((line = reader.readLine()) != null) {
                            terminal.append(line + "\n");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void append(char c) {
        try {
            if (c == '\n') {
                writer.println();
            } else {
                writer.print(c);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void append(String command) {
        try {
            writer.println(command);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
