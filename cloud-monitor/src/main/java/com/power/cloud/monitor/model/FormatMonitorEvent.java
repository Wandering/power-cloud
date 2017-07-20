package com.power.cloud.monitor.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * 监控事件，所有的监控使用此事件模型处理
 * <p/>
 * 创建时间: 14/10/28 下午6:14<br/>
 *
 * @author qyang
 * @since v0.0.1
 */
public class FormatMonitorEvent implements Event{
    private Map<String, String> allProperties;
    private MetaData metadata;

    private FormatMonitorEvent(Map<String, String> allProperties, MetaData metadata) {
        this.allProperties = allProperties;
        this.metadata = metadata;
    }

    @Override
    public void add(String property, String value) {
        //allProperties
    }

    @Override
    public String snapshot() {
        StringBuilder stringBuilder = new StringBuilder();
        for(String key : metadata.getMetadata()){
            stringBuilder.append(SPACE)
                    .append(allProperties.get(key));
        }

        return stringBuilder.toString();
    }

    @Override
    public String getEventType() {
        //不根据 eventType 分开不同的文件
        return null;
    }

    public static class MetaData{
        private String eventName;
        private List<String> metadata = Lists.newArrayList();

        public MetaData(List<String> newMetadata, String eventName) {
            this.eventName = eventName;
            metadata.add("name");
            metadata.add("event_type");
            metadata.add("tid");
            metadata.add("external_user_id");

            for(String key : newMetadata){
                metadata.add(key);
            }
        }

        public List<String> getMetadata() {
            return metadata;
        }

        public String getEventName() {
            return eventName;
        }

        /**
         * a,b,c,d
         * @return
         */
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder();
            for(String metadataStr : metadata){
                stringBuilder.append(metadataStr)
                        .append(MonitorEventContainer.SPLIT_STR);
            }
            return stringBuilder.subSequence(0, stringBuilder.length() - 1).toString();
        }
    }

    public static class Builder {
        /** 事件名称 */
        private final String name;
        /** event元数据 props的key列表，在log中不显示，减少log文件大小 */
        private final MetaData metadata;
        /** traceid 链式的追踪id */
        private final String tid;
        private final String eventType;
        /** 业务操作的主体方 */
        private final String externalUserId;
        /** 附带信息 */
        private Map<String, String> properties;

        public Builder(String name, MetaData metadata, String tid, String eventType, String externalUserId, Map<String, String> properties) {
            this.name = name;
            this.metadata = metadata;
            this.tid = tid;
            this.eventType = eventType;
            this.externalUserId = externalUserId;
            this.properties = properties;
        }

        public Builder add(String key, String value) {
            properties.put(key, value);
            return this;
        }

        public FormatMonitorEvent build() {
            Map<String, String> allProperties = Maps.newHashMap();
            allProperties.putAll(properties);
            allProperties.put("name", name);
            allProperties.put("event_type", eventType);
            allProperties.put("tid", tid);
            allProperties.put("external_user_id", externalUserId);
            return new FormatMonitorEvent(allProperties, metadata);
        }
    }
}
