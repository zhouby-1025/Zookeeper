package zookeeper.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 实现基于ZK节点的注册中心配置，节点内容发生变更的时候，应用自动监听，并可作出响应处理
 */
@Slf4j
public class ConfigCenter {

    private  final static Logger log = LoggerFactory.getLogger(ConfigCenter.class);

    private final static  String CONNECT_STR="127.0.0.1:2181";

    private final static Integer  SESSION_TIMEOUT=30*1000;

    private static ZooKeeper zooKeeper=null;


    private static CountDownLatch countDownLatch=new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {



        zooKeeper=new ZooKeeper(CONNECT_STR, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                    if (event.getType()== Event.EventType.None
                            && event.getState() == Event.KeeperState.SyncConnected){
                        log.info("连接已建立");
                        countDownLatch.countDown();
                    }
            }
        });
        countDownLatch.await();


        MyConfig myConfig = new MyConfig();
        myConfig.setKey("anykey");
        myConfig.setName("anyName");

        ObjectMapper objectMapper=new ObjectMapper();

        byte[] bytes = objectMapper.writeValueAsBytes(myConfig);

        String s = zooKeeper.create("/myconfig", bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);


        Watcher watcher = new Watcher() {
            @SneakyThrows
            @Override
            public void process(WatchedEvent event) {
                if (event.getType()== Event.EventType.NodeDataChanged
                        && event.getPath()!=null && event.getPath().equals("/myconfig")){
                    log.info(" PATH:{}  发生了数据变化" ,event.getPath());

                    byte[] data = new byte[0];
                    try {
                        data = zooKeeper.getData("/myconfig", this, null);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    MyConfig newConfig = null;
                    try {
                        newConfig = objectMapper.readValue(new String(data), MyConfig.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    log.info("数据发生变化: {}",newConfig);

                }


            }
        };

        byte[] data = zooKeeper.getData("/myconfig", watcher, null);
        MyConfig originalMyConfig = objectMapper.readValue(new String(data), MyConfig.class);
        log.info("原始数据: {}", originalMyConfig);


        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }




}
