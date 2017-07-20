package com.power.cloud.play;


import com.google.common.collect.Maps;
import com.power.cloud.common.utils.AESUtil;
import com.power.cloud.context.CloudContextFactory;
import com.power.cloud.core.ZKClient;
import com.power.cloud.core.recover.ZKRecoverUtil;
import com.power.cloud.play.util.EncryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.PlayPlugin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentMap;

/**
 * 来源于zookeeper的配置文件资源，该配置文件只做第一次装载，不做动态处理？
 * <p/>
 * 创建时间: 14-8-5 下午4:43<br/>
 *
 * @author qyang
 * @since v0.0.1
 */
public class ZookeeperResource extends PlayPlugin {
    private static Properties properties = new Properties();
    private static Logger log = LoggerFactory.getLogger(ZookeeperResource.class);

    public static final String URL_HEADER = "zk://";
    private static final String PATH_FORMAT = "/startconfigs/%s/config";
    /**
     * 多产品线支持 2015-08-19 add
     */
    private static final String CLOUD_PATH_FORMAT = "/startconfigs/%s/%s/config";
    private String path = String.format(PATH_FORMAT, CloudContextFactory.getCloudContext().getApplicationName());
    private String cloud_path = String.format(CLOUD_PATH_FORMAT, CloudContextFactory.getCloudContext().getProductCode(), CloudContextFactory.getCloudContext().getApplicationName());
    ConcurrentMap<String, Object> recoverDataCache = Maps.newConcurrentMap();
    @Override
    public void onLoad() {
        try {
            byte[] data = null;
            try {

                //data = ZKClient.getClient().getData().forPath(path);
                //check cloud path exists
                if (ZKClient.getClient().checkExists().forPath(cloud_path) != null) { // cloud mode, NODE: /startconfigs/%s/%s/config
                    data = ZKClient.getClient().getData().forPath(cloud_path);
                } else if (ZKClient.getClient().checkExists().forPath(path) != null) {// cloud mode, NODE: /startconfigs/%s/config
                    data = ZKClient.getClient().getData().forPath(path);
                } else { //cloud mode
                    log.error("{} and {} none exists", cloud_path, path);
                    System.exit(-1);
                }
            } catch (Exception e) {
                log.error("zk server error", e);
                // 读取cmc配置失败时加载本地备份的配置
                try {
                    data = ZKRecoverUtil.loadRecoverData(cloud_path);
                } catch (Exception e1) {
                    log.error("zk server cloud_path error", e);
                    data = ZKRecoverUtil.loadRecoverData(path);
                }

            }

            // 备份cmc配置到本地
            ZKRecoverUtil.doRecover(data, path, recoverDataCache);
            ZKRecoverUtil.doRecover(data, cloud_path, recoverDataCache);

            log.debug("init get startconfig data {}", new String(data));
            //  add by qyang  2015.10.21
            if (EncryptUtil.isEncrypt(data)) {
                byte[] pureData = new byte[data.length - 2];
                System.arraycopy(data, 2, pureData, 0, data.length - 2);
                String originStr = null;
                try {
                    originStr = AESUtil.aesDecrypt(new String(pureData), EncryptUtil.encryptKey);
                } catch (Exception e) {
                    log.error("decrypt error", e);
                    System.exit(-1);
                }
                properties.load(new ByteArrayInputStream(originStr.getBytes()));
            } else {
                properties.load(new ByteArrayInputStream(data));
            }
        }catch (IOException e){
            log.error("zk init error");
            System.exit(-1);
        }

    }

    public static Properties getProperties() {
        return properties;
    }

    public static void setProperties(Properties properties) {
        ZookeeperResource.properties = properties;
    }


    public static void main(String[] args) {
        ZookeeperResource zookeeperResource = new ZookeeperResource();
        zookeeperResource.onApplicationStart();
    }

}