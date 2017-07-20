package com.power.cloud.monitor.model;

/**
 * TODO 一句话描述该类用途
 * <p/>
 * 创建时间: 14/10/28 下午10:10<br/>
 *
 * @author qyang
 * @since v0.0.1
 */
public interface Event {
    public static final String SPACE = " ";

    public void add(String property, String value);
    /**
     * 数据进行镜像
     * @return
     */
    public String snapshot();

    /**
     * 获取事件类型    主要应用场景: 全局可能一个Event实例,需要区分多个共享Event的打点内容
     *
     * @return 事件类型
     */
    public String getEventType();
}
