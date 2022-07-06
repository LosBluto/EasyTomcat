package cn.bluto.easytomcat.test;

import cn.bluto.easytomcat.util.browser.MiniBrowser;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.NetUtil;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.CountDownLatch;

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
        Assert.assertEquals("hello, this is easytomcat!",result);
    }

    @Test
    public void testaHtml() {
        String result = getContentString("/a.html");
        Assert.assertEquals("hello, easytomcat from a.html!",result);
    }

    @Test
    public void testTimeConsume() throws InterruptedException {
        TimeInterval timeInterval = DateUtil.timer();         //开启计时器
        CountDownLatch countDownLatch = new CountDownLatch(3);  //用于同步线程工作

        for (int i = 0; i< 3; i++) {
            new Thread(new Runnable() {                     //启动三个线程访问tomcat，
                @Override
                public void run() {
                    getContentString("/timeConsume.html");
                    countDownLatch.countDown();
                }
            },"Thread " + i).start();
        }

        countDownLatch.await();         //同步线程
        long duration = timeInterval.intervalMs();          //终止计时器
        System.out.println(duration);
        Assert.assertTrue(duration < 3000);         //看看总时间是否超过3000，超过3000表示tomcat是单线程工作,<3000表示多线程工作
    }

    @Test
    public void testaIndex() {
        String result = getContentString("/a/index.html");
        Assert.assertEquals("hello, easytomcat from index.html@a",result);
    }

    @Test
    public void test() {
        String fileName = "a/index.html";
        String parent = "D:/Project/JAVA/EasyTomcat/webapps/ROOT/a";

        String result = new File(parent,fileName).getAbsolutePath();
        System.out.println(result);
    }

    private String getContentString(String uri) {
        String url = String.format("http://%s:%s%s",host,port,uri);
        return MiniBrowser.getContentString(url);
    }
}
