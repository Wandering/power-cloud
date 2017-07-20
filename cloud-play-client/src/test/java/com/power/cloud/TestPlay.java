package com.power.cloud;

import com.power.cloud.play.ZookeeperResource;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Administrator on 2017/7/13.
 */
public class TestPlay extends TestCase{
    @Test
    public void  testPlay(){
        ZookeeperResource zookeeperResource = new ZookeeperResource();
        zookeeperResource.onApplicationStart();
//        Assert.assertTrue(ZookeeperResource.getProperties().stringPropertyNames().size()>0);
    }
}
