package com.cdtu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 聊天室服务端
 * @author zxm
 */
public class Server {
    /*
     * 运行在服务端的socket
     * 该类的作用是：
     * 	1.申请服务端口，客户端就是通过它申请的服务端口连接上服务端应用的。
     *  2.监听申请的服务端口感知客户端的连接，并创建一个socket与该客户通信。
     */
    private ServerSocket server;

    //存放所有客户端的输出流的集合，用于广播
    private List<PrintWriter> allOut;

    //将给定的输出流放入共享集合
    private synchronized void addOut(PrintWriter out){
        allOut.add(out);
    }

    //将给定的输出流移除共享集合
    private synchronized void removeOut(PrintWriter out){
        allOut.remove(out);
    }

    //将给定的消息发给多个客户端
    private synchronized void sendMessage(String message) {

        for(PrintWriter out:allOut) {
            out.println(message);
        }
    }

    //构造方法初始化服务端
    public Server() throws IOException {
        //实例化serverSocket的同时，指定服务端的端口号；
        try {
            server = new ServerSocket(8888);
            allOut = new ArrayList<PrintWriter>();
        } catch (Exception e) {
            System.out.println("服务端初始化失败");
            throw e;
        }
    }

    //服务端工作的方法
    public void start() throws IOException {
        /*
         * ServerSocket提供了一个accept的方法，该方法是一个阻塞方法，
         * 用于监听其打开的8888端口；当一个客户端通过该端口与服务端连接时，
         * accept方法就会解除阻塞，然后创建一个socket实例并返回，
         * socket的作用就是与刚刚连接上的客户端进行通信。
         */
        while(true) {
            System.out.println("等待客户端连接...");
            Socket socket = server.accept();
            System.out.println("一个客户端连接了！");
            //启动一个线程来处理客户端的交互工作
            ClientHandler hander = new ClientHandler(socket);
            Thread t = new Thread(hander);
            t.start();
        }
    }


    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.start();
    }

    /**
     * 该线程类是与指定的客户端进行交互工作；
     * @author zxm
     *
     */
    class ClientHandler implements Runnable{
        //当前线程客户端的Socket
        private Socket socket;

        //该客户端的地址
        private String host;

        public ClientHandler(Socket socket) {
            this.socket=socket;
            /*
             * 通过socket获取远程计算机地址
             * 对于服务端而言，远程计算机就是客户端
             */
            InetAddress address = socket.getInetAddress();

            //获取ip地址
            host = address.getHostAddress();
            System.out.println("host"+host);
        }

        @Override
        public void run() {
            PrintWriter pw = null;
            try {
                //广播给所有客户端，当前用户上线了
                sendMessage("["+host+"]上线了");
                OutputStream out = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(out,"UTF-8");
                pw = new PrintWriter(osw,true);

                //将该客户的输出流存入共享集合，以便消息可以广播给该客户端
                addOut(pw);

                //广播当前在线人数
                sendMessage("当前在线人数["+allOut.size()+"]");

                //处理来自客户端的数据
                InputStream in = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(in,"utf-8");
                BufferedReader br = new BufferedReader(isr);
                /*
                 * 服务端读取客户端发送过来的每一句字符时
                 * 由于客户端所在的操作系统不同，这里客户端断开时结果也不同
                 * windows客户端开始br.readLine抛出异常
                 * Linux客户端断开是返回null
                 *
                 */
                String message = null;
                while((message = br.readLine())!=null) {
                    sendMessage(host+"说："+message);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally {
                //将该客户端的输出流从共享集合中删除
                removeOut(pw);

                //广播给所有客户端，当前用户下线
                sendMessage("["+host+"]下线了");

                //广播当前在线人数
                sendMessage("当前在线人数["+allOut.size()+"]");

                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}
