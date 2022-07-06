package cn.bluto.easytomcat.http;

import cn.bluto.easytomcat.Bootstrap;
import cn.bluto.easytomcat.catalina.Context;
import cn.bluto.easytomcat.util.browser.MiniBrowser;
import cn.hutool.core.util.StrUtil;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author LosBluto
 * @version 1.0.0
 * @ClassName Request.java
 * @Description TODO
 * @createTime 2022年06月24日 15:01:00
 */
public class Request {
    private String requestString;
    private String uri;
    private Socket socket;

    private Context context;

    public Request(Socket socket) throws IOException {
        this.socket = socket;
        parseRequestString();
        if (requestString == null || requestString.isEmpty())       //若请求为空，无需初始化uri
            return;

        parseUri();
        parseContext();
        if (!"/".equals(context.getPath()))
            uri = StrUtil.removePrefix(uri,context.getPath());      //重新确定uri，删除父文件目录的最后才是应用的uri
    }

    private void parseRequestString() throws IOException {
        byte[] bytes = MiniBrowser.getBytes(socket.getInputStream());
        requestString = new String(bytes, StandardCharsets.UTF_8);
    }

    private void parseContext() {
        String path = StrUtil.subBetween(uri,"/","/");
        if (null == path)
            path = "/";
        else
            path = "/" + path;

        context = Bootstrap.contextMap.get(path);
        if (null == context)                                //若未找到该目录
            context = Bootstrap.contextMap.get("/");        //获取默认root目录对应的上下文
    }

    private void parseUri() {
        String temp = StrUtil.subBetween(requestString," "," ");              //截取两个空格之间的字符串即为uri
        if (!temp.contains("?")) {                          //请求uri中是否包含?,无问号
            uri = temp;
            return;
        }
        uri = StrUtil.subBefore(temp,"?",false);    //有问号
    }

    public Context getContext() {
        return context;
    }

    public String getRequestString() {
        return requestString;
    }

    public String getUri() {
        return uri;
    }
}
