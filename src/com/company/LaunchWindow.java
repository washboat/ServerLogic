package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LaunchWindow extends JFrame{
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel centerPanel;
    private JButton serverButton;
    private JButton clientButton;
    private JLabel messageLabel;
    private ServerWindow serverWindow;
    private ClientWindow clientWindow;

    public LaunchWindow(){
        frame = new JFrame("LaunchWindow");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel = new JPanel();

        messageLabel.setText("Choose your service");

        serverButton.setText("Server");
        serverButton.addActionListener(this::openServerWindow);

        clientButton.setText("Client");
        clientButton.addActionListener(this::openClientWindow);

        mainPanel.add(centerPanel);
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
