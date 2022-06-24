package cn.bluto.easytomcat;

import cn.bluto.easytomcat.http.Request;
import cn.bluto.easytomcat.http.Response;
import cn.bluto.easytomcat.util.Constant;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NetUtil;

import java.io.IOException;
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
            int port = 8080;
            if (!NetUtil.isUsableLocalPort(port)) {
                System.out.println("port " + port + " is used,please change");
                return;
            }

            ServerSocket server = new ServerSocket(port);   //创建服务
            while (true) {
                Socket socket = server.accept();            //获取服务的套接字
                Request request = new Request(socket);
                System.out.println("请求信息为:\r\n"+request.getRequestString());
                System.out.println("请求URI为:\r\n"+request.getUri());

                Response response = new Response();
                response.getWriter().println("hello, this is easytomcat!");

                handle200(socket,response);
            }
        }catch (IOException e) {
            System.err.println("发生错误");
            e.printStackTrace();
        }
    }

    private static void handle200(Socket socket, Response response) throws IOException {
        String contentType = response.getContentType();
        String headText = Constant.response_head_202;
        headText = String.format(headText, contentType);

        byte[] head = headText.getBytes(StandardCharsets.UTF_8);
        byte[] content = response.getBytes();

        byte[] result = new byte[head.length + content.length];
        ArrayUtil.copy(head,0,result,0,head.length);
        ArrayUtil.copy(content,0,result,head.length,content.length);


        OutputStream os = socket.getOutputStream();
        os.write(result);
        os.close();
    }

}
