import javax.swing.*;
import java.awt.*;

/**
 * @author Yongbing Hu
 * @version 0.0.0
 * @time 2019-10-15 1:56 a.m.
 * @description login GUI
 */
public class LoginGUI {
    private JFrame frame;
    private JButton btnLogin;
    private JButton btnCancel;
    private JTextField txtUserName;
    private JPanel panel;
    private JLabel mention;
   
    public LoginGUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
        JLabel lblUsername = new JLabel("Username:");
        txtUserName = new JTextField(20);
        btnLogin = new JButton("Login");
        btnCancel = new JButton("Cancel");
        panel.add(lblUsername);
        panel.add(txtUserName);
        panel.add(btnLogin, BorderLayout.AFTER_LAST_LINE);
        panel.add(btnCancel);

        mention = new JLabel("welcome to the chat");
        panel.add(mention,BorderLayout.SOUTH);
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setSize(600, 600);
        frame.setLocation(300,300);

    }

    public JFrame getFrame() {
        return frame;
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }

    public JTextField getTxtUserName() {
        return txtUserName;
    }

    public JPanel getPanel() {
        return panel;
    }

    //feedback of the login
    public JLabel getMention() {
        return this.mention;
    }

}
