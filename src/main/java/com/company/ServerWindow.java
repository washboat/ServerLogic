package com.company;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerWindow {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel centerPanel;
    private JPanel northPanel;
    private JPanel westPanel;
    private JPanel eastPanel;
    private JPanel southPanel;
    private JTextField ipTextField;
    private JLabel ipLabel;
    private JTextField portTextField;
    private JLabel portLabel;
    private JTextField pathTextField;
    private JLabel pathLabel;
    private JButton repositoryButton;
    private JButton startButton;
    private JButton stopButton;
    private JButton restartButton;
    private File repositoryPath;
    private Server server;
    private volatile boolean serverIsRunning;
    private SwingWorker serverWorker;

    public ServerWindow(JFrame parentFrame){
        frame = new JFrame("ServerWindow");
        mainPanel = new JPanel();

        frame.setSize(500, 500);
        frame.addWindowListener(closeWindow(parentFrame));

        ipLabel.setText("Local IP: ");
        portLabel.setText("Server Port Number: ");

        try {
            ipTextField.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        portTextField.setText("32581");
        pathLabel.setText("Repository Location: ");

        repositoryButton.setText("Choose");
        repositoryButton.addActionListener(this::openRepositoryDialog);

        startButton.setText("Start Server");
        startButton.addActionListener(this::launchServer);

        stopButton.setText("Stop Server");
        stopButton.setEnabled(false);
        stopButton.addActionListener(this::stopServer);

        restartButton.setText("Restart Server");
        restartButton.setEnabled(false);
        restartButton.addActionListener(this::restartServer);

        mainPanel.add(northPanel);
        mainPanel.add(centerPanel);
        mainPanel.add(southPanel);
        mainPanel.add(westPanel);
        mainPanel.add(eastPanel);

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    private void launchServer(ActionEvent e) {
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        restartButton.setEnabled(true);
        int port = Integer.parseInt(portTextField.getText());
        File path = repositoryPath;
        try {
            serverWorker = new Server(port, path);
            serverWorker.execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void stopServer(ActionEvent e){
        serverWorker.cancel(true);
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        restartButton.setEnabled(false);
    }
    private void restartServer(ActionEvent e){
        stopServer(e);
        launchServer(e);
    }
    private void openRepositoryDialog(ActionEvent e){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Open Repository Location");
        int result = chooser.showOpenDialog(centerPanel);
        if (result == JFileChooser.APPROVE_OPTION)
            repositoryPath = chooser.getSelectedFile();
        pathTextField.setText(repositoryPath.toString());
    }
    private WindowAdapter closeWindow(JFrame parentFrame){
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parentFrame.setVisible(true);
                frame.dispose();
            }
        };
    }
}
