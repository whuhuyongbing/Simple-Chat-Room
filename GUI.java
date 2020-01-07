import javax.accessibility.AccessibleContext;
import javax.swing .*;
import java.awt .*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Yongbing Hu
 * @version 0.0.0
 * @time 2019-10-14 11:30 p.m.
 * @description GUI for the chat 
 */
public class GUI {
    private JTextArea textArea;
    private JButton send;
    private JButton connect;
    private JTextField textField;
    private JFrame frame;

    public JFrame getFrame() {
        return frame;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public JButton getSend() {
        return send;
    }

    public JButton getConnect() {
        return connect;
    }

    public JTextField getTextField() {
        return textField;
    }

    public GUI() {
        frame = new JFrame("Chat room");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        JPanel jPanel = new JPanel(); // the panel is not visible in output
        JLabel label = new JLabel("Enter Text");
        this.textField = new JTextField(20); // accepts upto 10 characters
        this.send = new JButton("Send");
        this.connect = new JButton("Disconnect");

        jPanel.add(label);
        jPanel.add(textField);
        jPanel.add(send);
        jPanel.add(connect);

        this.textArea = new JTextArea();
        frame.getContentPane().add(BorderLayout.SOUTH, jPanel);
        frame.getContentPane().add(BorderLayout.CENTER, textArea);
        frame.setLocation(300,300);
    }
}
