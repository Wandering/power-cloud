package com.power.cloud.cmc;

import com.power.cloud.cmc.impl.ZKAliveServer;
import com.power.cloud.dynclient.IChangeListener;

import java.util.List;

/**
 * 集群机器管理client
 * <p/>
 * 创建时间: 14-8-1 下午3:43<br/>
 *
 * @author qyang
 * @since v0.0.1
 */
public interface IClusterManagerClient {
    public static final String FIRST_ADD = "firstadd";

    /**
     * 将本机注册到云管理中心
     * @param appName 业务系统名称
     */
    public ZKAliveServer register(String appName);

    public ZKAliveServer register(String productCode, String appName);

    /**
     * 注册本机到当前上下文业务系统的服务列表中
     */
    public ZKAliveServer register();

    /**
     * 获取某一应用的集群机器列表
     * @param appName
     * @return
     */
    public List<String> getLiveServers(String appName);

    public List<String> getLiveServers(String productCode, String appName);

    /**
     * 获取当前系统的server列表
     * @return
     */
    public List<String> getLiveServers();

    /**
     * 获取所有的业务系统名称
     * @return
     */
    public List<String> getBizSystems();

    /**
     * 根据业务系统名称获取业务系统元数据描述
     * @param appName
     * @return
     */
    public String getBizSystemMetadata(String appName);

    public String getBizSystemMetadata(String productCode, String appName);

    /**
     * 监听服务器变化
     * @param listener
     */
    public void initListenerServerChange(final IChangeListener listener);
}
