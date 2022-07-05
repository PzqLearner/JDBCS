package com.cdtu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * 聊天室服务端
 * @author zxm
 */
public class Client {

    //用于与服务端通信的Socket
    private Socket socket;

    public Client() throws Exception {
        /*
         * 初始化Socket的同时要制定服务端的IP地址和端口号
         * ip地址用于我们在网络上找到服务端的所在计算在
         * 端口号用于找到服务器上的服务端应用程序。
         *
         * *实例化Socket的过程就是连接服务端的过程若
         * 服务端无响应，这里得构造方法得抛出异常。
         *
         */
        try {
            System.out.println("正在连接服务器......");
            //localhost 127.0.0.1
            socket = new Socket("192.168.43.246",8888);
            System.out.println("与服务端连接完毕");
        } catch (Exception e) {
            System.out.println("初始化失败");
            throw e;
        }
    }

    public void start() throws Exception {
        /*
         * 客户端开始工作的方法
         */
        try {
            //启动用于读取服务端发送消息的线程
            ServerHandler handler = new ServerHandler();
            //ServerHandler是自己写的类，实现Runnable接口,有多线程功能
            Thread t = new Thread(handler);
            t.start();

            //将数据发送到服务端
            OutputStream out = socket.getOutputStream();//获取输出流对象
            OutputStreamWriter osw = new OutputStreamWriter(out,"utf-8");//转化成utf-8格式
            PrintWriter pw = new PrintWriter(osw,true);
            Scanner scan = new Scanner(System.in);
            while(true) {
                String message = scan.nextLine();//得到键盘录入的信息
                pw.println(message);//把信息输出到服务端
            }

        } catch (Exception e) {
            System.out.println("客户端运行失败");
            throw e;
        }

    }

    public static void main(String[] args) throws Exception {

        try {
            Client client = new Client();
            client.start();
        } catch (Exception e) {
            System.out.println("客户端运行失败");
            e.printStackTrace();
        }


    }

    class ServerHandler implements Runnable{
        /**
         * 该线程用于读取服务端发送过来的消息，并输出到
         * 客户端的控制台上
         * @author zxm
         *
         */
        @Override
        public void run() {
            try {
                InputStream in = socket.getInputStream();//输入流
                InputStreamReader isr = new InputStreamReader(in,"UTF-8");//以utf-8读
                BufferedReader br = new BufferedReader(isr);
                String message = null;
                while((message=br.readLine())!=null) {
                    System.out.println(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

