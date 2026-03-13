package serializable;

import java.util.Map;

public interface Serializable {
    Map<String, Object> serialize();
    void deserialize(Map<String, Object> data);
}
