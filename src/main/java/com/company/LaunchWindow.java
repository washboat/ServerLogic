package com.company;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatDraculaIJTheme;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LaunchWindow extends JFrame{
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel centerPanel;
    private JButton serverButton;
    private JButton clientButton;
    private JLabel messageLabel;
    private JButton otherButton;
    private ServerWindow serverWindow;
    private ClientWindow clientWindow;

    public LaunchWindow(){
//        FlatDraculaIJTheme.install();
        FlatDarkPurpleIJTheme.install();
        UIManager.put("Button.arc", 10);
        UIManager.put("Component.arc", 10);
//        FlatLightLaf.install();
        frame = new JFrame("LaunchWindow");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        messageLabel.setText("Choose your service");

        serverButton.setText("Server");
        serverButton.addActionListener(this::openServerWindow);

        clientButton.setText("Client");
        clientButton.addActionListener(this::openClientWindow);

        mainPanel = new JPanel();
        //otherPanel.add(otherButton);
        mainPanel.add(centerPanel);
        mainPanel.add(otherButton);
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new LaunchWindow();
    }

    private void openServerWindow(ActionEvent e){
        frame.setVisible(false);
        serverWindow = new ServerWindow(frame);
    }

    private void openClientWindow(ActionEvent e){
        frame.setVisible(false);
        clientWindow = new ClientWindow(frame);
    }
}
