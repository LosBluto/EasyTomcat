package cn.bluto.easytomcat.http;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author LosBluto
 * @version 1.0.0
 * @ClassName Response.java
 * @Description TODO
 * @createTime 2022年06月24日 16:15:00
 */
public class Response {
    private StringWriter stringWriter;          //用于存放content数据
    private PrintWriter writer;                 //用于向stringwriter写入数据
    private String contentType;

    public Response() {
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        contentType = "text/html";              //先写死
    }

    public String getContentType() {
        return contentType;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public byte[] getBytes() {
        return stringWriter.toString().getBytes(StandardCharsets.UTF_8);
    }
}
