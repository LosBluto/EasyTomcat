package cn.bluto.easytomcat.util;

import cn.hutool.system.SystemUtil;

import java.io.File;

/**
 * @author LosBluto
 * @version 1.0.0
 * @ClassName Constant.java
 * @Description TODO
 * @createTime 2022年06月24日 16:16:00
 */
public class Constant {
    public static final String response_head_202 =
            "HTTP/1.1 200 OK\r\n" +
            "Content-Type: %s\r\n\r\n";

    public static final File webAppsFolder = new File(SystemUtil.get("user.dir"),"webapps");
    public static final File rootFolder = new File(webAppsFolder,"ROOT");
}
