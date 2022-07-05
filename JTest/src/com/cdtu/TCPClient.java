package com.cdtu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    static class Send { //发送消息
        private DataOutputStream dos;
        private Socket socket;
        private Scanner scanner;
        private String msg;
        public Send(Socket socket) {
            this.scanner = new Scanner(System.in);
            this.msg = this.init();
            this.socket = socket;
            try {
                dos = new DataOutputStream(this.socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private void send() {
            try {
                dos.writeUTF(this.msg);
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private String  init() {
            System.out.println("请输入用户名");
            String userName = this.scanner.nextLine();
            System.out.println("请输入密码");
            String userPwd = this.scanner.nextLine();
            return "userName=" + userName + "&" + "userPwd=" + userPwd;
        }
    }
    static class Receive {
        //接收消息
        private DataInputStream dis;
        Socket socket;
        public Receive(Socket socket) {
            this.socket = socket;
            try {
                dis = new DataInputStream(this.socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void receive() {
            String result = null;
            try {
                result = dis.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(result);
        }
    }
    public static void main(String[] args) throws IOException {

        // 1、建立连接 使用Socket创建客户端+服务器地址和端口
        Socket socket = new Socket("127.0.0.1",8878);
        // 2、操作：输入输出流
        new Send(socket).send();
        new Receive(socket).receive();
        // 3、释放资源
        socket.close();
    }
}
