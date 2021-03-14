package zookeeper.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetConfigBuilder;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.server.util.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zookeeper.curator.watcher.CuratorWatcherTest;

import java.util.Scanner;

@Slf4j
public class CuratorReconfig {

    private  final static Logger log = LoggerFactory.getLogger(CuratorReconfig.class);

    private static CuratorFramework curatorFramework;

    private final  static  String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183,127.0.0.1:2184";

    private  static  RetryPolicy  retryPolicy =new ExponentialBackoffRetry(5*1000,5);

    public static void main(String[] args) throws Exception {

         curatorFramework = CuratorFrameworkFactory.newClient(connectString, retryPolicy);

        curatorFramework.getConnectionStateListenable().addListener(((client, newState) -> {
            if (newState== ConnectionState.CONNECTED){
                log.info(" 连接建立");
            }
        }));
        curatorFramework.start();

        Scanner scanner=new Scanner(System.in);
        while (true){


            GetConfigBuilder config = curatorFramework.getConfig();
            byte[] bytes = config.forEnsemble();

            String clientConfigStr = ConfigUtils.getClientConfigStr(new String(bytes));
            log.info("CONFIG: {}",clientConfigStr);

            scanner.next();

        }

    }

}
