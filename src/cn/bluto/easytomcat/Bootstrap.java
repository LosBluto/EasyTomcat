package cn.bluto.easytomcat;

import cn.bluto.easytomcat.http.Request;
import cn.bluto.easytomcat.http.Response;
import cn.bluto.easytomcat.util.Constant;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

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
            logJVM();

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
                String uri = request.getUri();
                if (uri == null)
                    continue;
                if (uri.equals("/")) {
                    response.getWriter().println("hello, this is easytomcat!");
                }else {                 //若uri后有路径
                    String temp = StrUtil.removePrefix(uri,"/");
                    File file = new File(Constant.rootFolder,temp);
                    if (file.exists()) {            //存在该文件
                        String fileContent = FileUtil.readUtf8String(file);
                        response.getWriter().println(fileContent);
                    }else {             //文件不存在
                        response.getWriter().println("File not found!");
                    }
                }

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

    /**
     * 输出初始化JVM日志信息
     */
    private static void logJVM() {
        Map<String, String> infos = new LinkedHashMap<>();      //为何用linkedhashmap
        infos.put("Server version","LosBluto EasyTomcat/1.0");
        infos.put("Server built","2022-06-26 11:32");
        infos.put("Server number","1.0.1");
        infos.put("OS Name\t", SystemUtil.get("os.name"));
        infos.put("OS Version",SystemUtil.get("os.version"));
        infos.put("Architecture",SystemUtil.get("os.architecture"));
        infos.put("Java Home",SystemUtil.get("java.home"));
        infos.put("JVM Version",SystemUtil.get("java.runtime.version"));
        infos.put("JVM Vendor",SystemUtil.get("java.vm.specification.vendor"));

        for (String key : infos.keySet()) {
            LogFactory.get().info(key + ":\t\t" + infos.get(key));
        }
    }

}
