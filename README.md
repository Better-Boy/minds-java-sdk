# minds-java-sdk

```java
import com.mindsdb.models.Mind;
import com.mindsdb.services.Minds;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String apiKey = "";
        MindsDb.init(apiKey);
        List<Mind> mindList = Minds.list();
        System.out.println(mindList);
    }
}
```