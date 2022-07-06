package cn.bluto.easytomcat.catalina;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.log.LogFactory;

/**
 * @author LosBluto
 * @version 1.0.0
 * @ClassName Context.java
 * @Description TODO 应用对应的上下文，用于保存应用的路径信息
 * @createTime 2022年07月05日 18:58:00
 */
public class Context {

    private String path;         //记录访问的网络路径
    private String docBase;      //记录对应服务器中的路径

    public Context(String path, String docBase) {
        TimeInterval timeInterval= DateUtil.timer();
        this.path = path;
        this.docBase = docBase;
        LogFactory.get().info("Deploying web application directory {}", this.docBase);
        LogFactory.get().info("Deployment of web application directory {} has finished in {} ms", this.docBase,timeInterval.intervalMs());
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDocBase(String docBase) {
        this.docBase = docBase;
    }

    public String getPath() {
        return path;
    }

    public String getDocBase() {
        return docBase;
    }
}
