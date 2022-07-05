package Jtest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class ClientChat extends JFrame implements ActionListener , KeyListener {
    public static void main(String[] args) {
        new ClientChat();
    }
    //属性
    private JTextArea jta;
    private JScrollPane jsp;
    private JPanel jp;
    private JTextField jtf;
    private JButton jb;

    private BufferedWriter bw=null;

    private static String clientIp;
    private static int clientPort;

    static {
        Properties properties=new Properties();
        try {
            properties.load(new FileReader("E:\\JDBCS\\JTest\\src\\Jtest\\chat.properties"));
            clientIp=properties.getProperty("clientIp");
            clientPort=Integer.parseInt(properties.getProperty("clientPort"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public ClientChat(){
        //初始化
        jta=new JTextArea();
        jta.setEditable(false);
        jsp=new JScrollPane(jta);
        jp=new JPanel();
        jtf=new JTextField(20);
        jb=new JButton("发送");
        jp.add(jtf);
        jp.add(jb);

        this.add(jsp, BorderLayout.CENTER);
        this.add(jp,BorderLayout.SOUTH);
        this.setTitle("客户端");
        this.setSize(400,400);
        this.setLocation(500,200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        //客户端
        //监听事件
        jb.addActionListener(this);
        jtf.addKeyListener(this);
        try {
           Socket socket= new Socket(clientIp, clientPort);
           //输入流
           BufferedReader br= new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
           //输出流
           bw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8));
            String line;
            while ((line=br.readLine())!=null){
                jta.append(line + System.lineSeparator());
            }
        socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void sendDataToSocket(){
        String text=jtf.getText();
        text="客户端对用户端说："+text;
        jta.append(text+ System.lineSeparator());
        try {
            bw.write(text);
            bw.newLine();
            bw.flush();
            jtf.setText("");
        }catch (Exception e1){e1.printStackTrace();}}
    @Override
    public void actionPerformed(ActionEvent e) {
        sendDataToSocket();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        //回车
        if(e.getKeyCode()==KeyEvent.VK_ENTER){
            sendDataToSocket();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }



    @Override
    public void keyReleased(KeyEvent e) {

    }

}
