package cn.bluto.easytomcat.http;

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

    public Request(Socket socket) throws IOException {
        this.socket = socket;
        parseRequestString();
        if (requestString == null || requestString.isEmpty())       //若请求为空，无需初始化uri
            return;

        parseUri();
    }

    private void parseRequestString() throws IOException {
        byte[] bytes = MiniBrowser.getBytes(socket.getInputStream());
        requestString = new String(bytes, StandardCharsets.UTF_8);
    }

    private void parseUri() {
        String temp = StrUtil.subBetween(requestString," "," ");              //截取两个空格之间的字符串即为uri
        if (!temp.contains("?")) {                          //请求uri中是否包含?,无问号
            uri = temp;
            return;
        }
        uri = StrUtil.subBefore(temp,"?",false);    //有问号
    }

    public String getRequestString() {
        return requestString;
    }

    public String getUri() {
        return uri;
    }
}
