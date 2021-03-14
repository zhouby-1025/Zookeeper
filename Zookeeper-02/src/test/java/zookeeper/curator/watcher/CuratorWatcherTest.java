package zookeeper.curator.watcher;


import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zookeeper.curator.CuratorBaseOperations;
import zookeeper.curator.test.Node;


@Slf4j
public class CuratorWatcherTest extends CuratorBaseOperations{

    private  final static Logger log = LoggerFactory.getLogger(CuratorWatcherTest.class);

    // 使用 watcher 机制， 为一次性监听
    @Test
    public void testWatcher() throws Exception {

        CuratorFramework curatorFramework = getCuratorFramework();
        String path="/test";
        createIfNeed(path);
        byte[] bytes = curatorFramework.getData().usingWatcher(new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getType() == Event.EventType.NodeDataChanged){
                    log.info("node {} data changed!",event.getPath());


                }
            }
        }).forPath(path);
        log.info(" original data: {}",new String(bytes));
        log.info("start to change data.");
        curatorFramework.setData().forPath(path, "test".getBytes());
        log.info("start to change data again! ");
        curatorFramework.setData().forPath(path, "test".getBytes());


    }
}
