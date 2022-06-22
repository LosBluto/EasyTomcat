package cn.bluto.easytomcat;

import cn.hutool.core.util.NetUtil;
import cn.hutool.poi.exceptions.POIException;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author LosBluto
 * @version 1.0.0
 * @ClassName Bootstrap.java
 * @Description TODO 一个开始程序，用于启动tomcat
 * @createTime 2022年06月22日 21:37:00
 */
public class Bootstrap {
    public static void main(String[] args) {
        try {
            int port = 80;
            if (!NetUtil.isUsableLocalPort(port)) {
                System.out.println("port " + port + " is used,please change");
                return;
            }

            ServerSocket server = new ServerSocket(port);   //创建服务
            while (true) {
                Socket socket = server.accept();            //获取服务的套接字
                InputStream is = socket.getInputStream();
                byte[] buffer = new byte[1024];             //1024大小的缓冲区,目前直接读入缓冲区
                is.read(buffer);
                String requestString = new String(buffer, StandardCharsets.UTF_8);
                System.out.println("请求信息为:\r\n"+requestString);

                OutputStream os = socket.getOutputStream(); //获取输出流
                String responseHead = "Http/1.1 200 OK\r\n" + "Content-Type:"+"text/html\r\n\r\n";  //消息头和消息体之间间隔了一行
                String responseBody = "hello, this is easytomcat!";
                String responseString = responseHead + responseBody;
                os.write(responseString.getBytes());
                os.flush();
                os.close();
            }
        }catch (IOException e) {
            System.err.println("发生错误");
            e.printStackTrace();
        }


    }

}
