package com.cdtu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class TCPServer {

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        // 1、指定端口，使用ServerSocket创建服务器
        ServerSocket serverSocket = new ServerSocket(8878);
        // 2、阻塞式等待连接 accept
        boolean flag = true;
        while (flag) {
            Socket socket = serverSocket.accept();
            System.out.println("有一个客户端连接了服务器");
            new Thread(new Channel(socket)).start();
        }
        serverSocket.close();
    }

    static class Channel implements Runnable{
        private Socket socket;
        DataInputStream dis;  //输入流
        DataOutputStream dos; //输出流
        public Channel(Socket socket) {
            this.socket = socket;
            try {
                dis = new DataInputStream(this.socket.getInputStream());
                dos = new DataOutputStream(this.socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                release();
            }
        }
        @Override
        public void run() {
            System.out.println("客户端成功连接服务端");
            // 3、操作：输入输出流
            String uName = "";
            String uPwd = "";
            //分析数据
            String[] dataArray = receive().split("&");//split()按照&，拆分字符串
            for (String info : dataArray) {
                String[] userInfo = info.split("=");
                if (userInfo[0].equals("userName")) {
                    System.out.println("你的用户名为：" + userInfo[1]);
                    uName = userInfo[1];
                } else {
                    System.out.println("你的密码为：" + userInfo[1]);
                    uPwd = userInfo[1];
                }
            }
            //输出
            if (matchPassword(uName,uPwd)) {
                send("登录成功"+",欢迎登录"+uName);
            } else {
                send("用户名或密码错误，请重新输入");
            }
            release();
        }
        //接收数据
        private String receive() {
            String datas = "";
            try {
                datas = dis.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return datas;
        }
        //匹配账户密码
        private boolean matchPassword(String uName,String uPassword){
            Connection con=null;
            Statement st=null;
            ResultSet rs=null;
            try {
                con= DriverManager.getConnection("jdbc:mysql://localhost:3306/netweek","root","111111");
                st= con.createStatement();
                String sql="select * from nameword";
                rs=st.executeQuery(sql);
                while (rs.next()){
                    String name=rs.getString("name");
                    String password=rs.getString("password");
                    if (uName.equals(name) && uPassword.equals(password)){
                        return true;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
        private void send(String msg) {
            try {
                dos.writeUTF(msg);
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //释放资源
        private void release() {
            try {
                if (null != dos) {
                    dos.close();
                }
                if (null != dis) {
                    dis.close();
                }
                if (null != socket) {
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}