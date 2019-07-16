package ui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.OutputStream;
import java.io.PrintStream;

import static java.awt.event.WindowEvent.WINDOW_CLOSING;
import static java.lang.String.valueOf;

public class PrintStreamJFrame {
    private final Runnable onClose;

    private final JFrame frame;
    private final JTextArea textArea;
    private final PrintStream printStream;

    public PrintStreamJFrame(Runnable onClose) {
        this.onClose = onClose;
        textArea = createTextArea();
        printStream = createPrintStream(textArea);
        frame = initializeLayout();
    }

    public void show() {
        frame.setVisible(true);
    }

    public PrintStream getPrintStream() {
        return printStream;
    }

    private PrintStream createPrintStream(JTextArea textArea) {
        return new PrintStream(new OutputStream() {
            @Override
            public void write(int bytes) {
                textArea.append(valueOf((char) bytes));
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
        });
    }

    private void clear() {
        Document document = textArea.getDocument();
        try {
            document.remove(0, document.getLength());
        } catch (BadLocationException ignored) {
        }
    }

    private JTextArea createTextArea() {
        JTextArea textArea = new JTextArea(50, 10);
        textArea.setEditable(false);
        return textArea;
    }

    private JFrame initializeLayout() {
        JFrame jFrame = new JFrame();
        jFrame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.WEST;

        JButton clearButton = new JButton("Clear logs");
        clearButton.addActionListener(event -> clear());
        jFrame.add(clearButton, constraints);

        constraints.gridx = 1;

        JButton closeButton = new JButton("Disconnect all clients and Close");
        closeButton.addActionListener(event -> {
            onClose.run();
            closeWindow(jFrame);
        });
        jFrame.add(closeButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;

        jFrame.add(new JScrollPane(textArea), constraints);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(480, 320);
        jFrame.setLocationRelativeTo(null);
        return jFrame;
    }

    private void closeWindow(JFrame jFrame) {
        jFrame.dispatchEvent(new WindowEvent(jFrame, WINDOW_CLOSING));
    }
}
