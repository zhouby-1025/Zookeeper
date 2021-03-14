package zookeeper.client;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 模拟配置信息类
 */
@Data
@ToString
@NoArgsConstructor
public class MyConfig {
    private String key;
    private String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
