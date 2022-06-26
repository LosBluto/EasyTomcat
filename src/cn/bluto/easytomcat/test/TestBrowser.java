package cn.bluto.easytomcat.test;

import cn.bluto.easytomcat.util.browser.MiniBrowser;
import cn.hutool.core.util.NetUtil;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author LosBluto
 * @version 1.0.0
 * @ClassName Test.java
 * @Description TODO 自动化单元测试
 * @createTime 2022年06月23日 10:46:00
 */

public class TestBrowser {
    private static final int port = 8080;
    private static final String host = "127.0.0.1";

    /**
     * 测试前检测服务是否启动
     */
    @BeforeClass
    public static void before() {               //前序方法必须为静态
        if (NetUtil.isUsableLocalPort(port)) {  //检查端口号是否正在使用
            System.err.println("please start the server in port:" +port);
            System.exit(1);
        }else
            System.out.println("start success!");
    }

    /**
     * 测试hello
     */
    @Test
    public void testHello() {
        String result = getContentString("/");
        Assert.assertEquals(result,"hello, this is easytomcat!");
    }

    @Test
    public void testaHtml() {
        String result = getContentString("/a.html");
        Assert.assertEquals(result,"hello, easytomcat from a.html!");
    }

    private String getContentString(String uri) {
        String url = String.format("http://%s:%s%s",host,port,uri);
        return MiniBrowser.getContentString(url);
    }
}
