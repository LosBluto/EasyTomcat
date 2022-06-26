package cn.bluto.easytomcat.util.browser;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LosBluto
 * @version 1.0.0
 * @ClassName MiniBrowser.java
 * @Description TODO 模拟一个小型浏览器，解析返回内容，便于测试
 * @createTime 2022年06月23日 10:46:00
 */
public class MiniBrowser {

    public static void main(String[] args) throws Exception {       //测试
        String url = "http://static.how2j.cn/diytomcat.html";
        String contentString= getContentString(url,false);
        System.out.println(contentString);
        String httpString= getHttpString(url,false);
        System.out.println(httpString);
    }

    public static String getContentString(String url) {
        return getContentString(url,false);
    }


    public static String getContentString(String url,boolean gzip) {
        byte[] contentBytes = getContentBytes(url, gzip);
        if (null == contentBytes)
            return null;
        return new String(contentBytes, StandardCharsets.UTF_8).trim();
    }

    public static byte[] getContentBytes(String url) {
        return getContentBytes(url,false);
    }

    public static byte[] getContentBytes(String url, boolean gzip) {
        byte[] response = getHttpBytes(url, gzip);
        byte[] splitBytes = "\r\n\r\n".getBytes();              //header和content的分隔符


        int pos = -1;
        for (int i = 0; i < response.length-splitBytes.length;i++) {        //从结果中寻找header和content的分隔符
            byte[] temp = Arrays.copyOfRange(response,i,i+splitBytes.length);
            if (Arrays.equals(temp,splitBytes)) {
                pos = i;
                break;
            }
        }

        if (-1 == pos) {                //无content信息
            return null;
        }

        pos += splitBytes.length;       //content起始位置

        return Arrays.copyOfRange(response,pos,response.length);            //取出content

    }

    public static String getHttpString(String url) {
        return getHttpString(url,false);
    }

    public static String getHttpString(String url,boolean gzip) {
        return new String(getHttpBytes(url,gzip)).trim();           //去除两头空字符串
    }
    /**
     * 最初的母方法
     */
    public static byte[] getHttpBytes(String url, boolean gzip) {
        byte[] result = null;
        try {
            URL u = new URL(url);
            Socket client = new Socket();
            int port = u.getPort();
            if (-1 == port)                     //若url中无port，则默认80端口
                port = 80;
            InetSocketAddress inetSocketAddress = new InetSocketAddress(u.getHost(),port);
            client.connect(inetSocketAddress,1000);                 //默认一秒超时连接

            String path = u.getPath();
            if (path.length() == 0)
                path = "/";
            String firstLine = "GET " + path + " HTTP/1.1\r\n";             //请求头首行

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Host",u.getHost()+":"+port);
            requestHeaders.put("Accept", "text/html");
            requestHeaders.put("Connection", "close");              //短链接
            requestHeaders.put("User-Agent", "losbluto mini brower / java1.8");

            if(gzip)
                requestHeaders.put("Accept-Encoding", "gzip");

            StringBuilder requestString = new StringBuilder(firstLine);
            for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                requestString.append(entry.getKey()).append(":").append(entry.getValue()).append("\r\n");
            }

            PrintWriter printWriter = new PrintWriter(client.getOutputStream(),true);       //把请求信息刷入socket中
            printWriter.println(requestString);

            InputStream is = client.getInputStream();                   //获取输出信息

            result = getBytes(is);

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
            result = e.toString().getBytes(StandardCharsets.UTF_8);     //异常信息编码并返回
        }

        return result;
    }

    /**
     * 从输入流获取bytes的公共方法
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        while (true) {
            int length = is.read(buffer);
            if (-1 == length)
                break;
            baos.write(buffer,0,length);
            if (length != bufferSize)          //若无读取数据了直接结束，减少一次循环
                break;
        }

        return baos.toByteArray();
    }
}
