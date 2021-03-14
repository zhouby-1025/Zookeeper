package zookeeper.curator;


public  class CuratorClusterBase extends CuratorStandaloneBase {

    private final static  String CLUSTER_CONNECT_STR="127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183,127.0.0.1:2184";

    public   String getConnectStr() {
        return CLUSTER_CONNECT_STR;
    }
}
