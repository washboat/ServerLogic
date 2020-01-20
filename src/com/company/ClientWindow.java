package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;

public class ClientWindow {
    private File pathToMonitor;
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel southPanel;
    private JPanel northPanel;
    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel centerPanel;
    private JTextField ipTextField;
    private JTextField portTextField;
    private JTextField pathTextField;
    private JLabel ipLabel;
    private JLabel portLabel;
    private JLabel pathLabel;
    private JButton pathButton;
    private JButton startButton;

    public ClientWindow(JFrame parentFrame){
        frame = new JFrame("ClientWindow");
        frame.setSize(500,500);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parentFrame.setVisible(true);
                frame.dispose();
                //super.windowClosing(e);
            }
        });

        Dimension dimension = new Dimension(55, 55 );
        int columns = 20;
        ipLabel.setText("Server IP: ");
        ipTextField.setColumns(columns);
        try {
            ipTextField.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        portLabel.setText("Port Number: ");
        portTextField.setColumns(columns);
        portTextField.setText("32581");

        pathLabel.setText("Folder To Watch:");
        pathTextField.setColumns(columns);
        pathButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setDialogTitle("Directory to Watch");
            int result = chooser.showOpenDialog(centerPanel);
            if (result == JFileChooser.APPROVE_OPTION)
                pathToMonitor = chooser.getSelectedFile();
            pathTextField.setText(pathToMonitor.toString());
        });

        startButton.setText("Start");
        startButton.addActionListener(this::startMonitor);

        mainPanel = new JPanel();
        mainPanel.add(centerPanel);
        mainPanel.add(northPanel);
        mainPanel.add(eastPanel);
        mainPanel.add(westPanel);
        mainPanel.add(southPanel);
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    private void startMonitor(ActionEvent e){
        int port = Integer.parseInt(portTextField.getText());
        String ip = ipTextField.getText();
        Path path = pathToMonitor.toPath();
        try {
            SwingWorker worker = new DirectoryMonitor(path, true, ip, port);
            worker.execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
