package zookeeper.curator.cache;


import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zookeeper.curator.CuratorStandaloneBase;
import zookeeper.curator.test.CuratorBarrierTest;

@Slf4j
public class NodeCacheTest extends CuratorStandaloneBase{

    private  final Logger log = LoggerFactory.getLogger(NodeCacheTest.class);

    public static final String NODE_CACHE="/node-cache";

    @Test
    public void testNodeCacheTest() throws Exception {

        CuratorFramework curatorFramework = getCuratorFramework();

        createIfNeed(NODE_CACHE);
        NodeCache nodeCache = new NodeCache(curatorFramework, NODE_CACHE);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                log.info("{} path nodeChanged: ",NODE_CACHE);
                printNodeData();
            }
        });

        nodeCache.start();
    }


    public void printNodeData() throws Exception {
        CuratorFramework curatorFramework = getCuratorFramework();
        byte[] bytes = curatorFramework.getData().forPath(NODE_CACHE);
        log.info("data: {}",new String(bytes));
    }
}