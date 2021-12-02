/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
/**
 *
 * @author kami
 */
public class View extends JFrame implements ActionListener{
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    
    public View(){
        super("Client Login UDP");
        
        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);
        txtPassword.setEchoChar('*');
        btnLogin = new JButton("Login");
        
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel("Username: "));
        panel.add(txtUsername);
        panel.add(new JLabel("Password"));
        panel.add(txtPassword);
        panel.add(btnLogin);
        
        
        this.setContentPane(panel);
        this.setLocationRelativeTo(null);
        btnLogin.addActionListener(this);
        this.pack();
        
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(btnLogin)){
            Control control = new Control();
            control.openConnection();
            
            User user = new User();
            user.setUsername(txtUsername.getText());
            user.setPassword(txtPassword.getText());
            control.sendData(user);
            
            String result = control.receiveData();
            if(result.equals("ok")){
                showMessage("Login successfully!");
            }
            else{
                showMessage("Wrong username and/or password! Please try again.");
            }
            control.closeConnection();
        }
    }
    
    public void showMessage(String s){
        JOptionPane.showMessageDialog(this, s);
    }
}
