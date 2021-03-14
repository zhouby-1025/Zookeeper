package zookeeper.client;

public class ClusterBase extends StandaloneBase {

    private final static  String CLUSTER_CONNECT_STR="127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183,127.0.0.1:2183";


    private static final  int CLUSTER_SESSION_TIMEOUT=60 * 1000;


    @Override
    protected String getConnectStr() {
        return CLUSTER_CONNECT_STR;
    }

    @Override
    protected int getSessionTimeout() {
        return CLUSTER_SESSION_TIMEOUT;
    }
}
