package software.siani.orchilla;

import software.siani.orchilla.model.TemporalExpression;
import software.siani.orchilla.quassar.QuassarParser;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class MainQuassar {
    public static void main(String[] args) throws IOException {
        String example1 = "https://quassar.io/commits/ef1e6567-c2c0-4808-b026-a9af17398d07";
        String example2 = "https://quassar.io/commits/aa6f6516-6eda-4085-a0e5-8435deb413b6";
        String example3 = "https://quassar.io/commits/a9b48889-0f45-4eb0-99ad-a80f766a5d71";
        String example4 = "https://quassar.io/commits/8cd94ce5-5bf6-446e-b64f-9de9cc93c914";
        HashMap<String, String> stringMap = new QuassarParser().parse(example1);
        System.out.println(stringMap);
        Map<String, TemporalExpression> map = new HashMap<>();
        for (String key : stringMap.keySet()) {
            map.put(key, new TemporalExpression.Builder().with(null).build(stringMap.get(key)));
        }
        System.out.println(map);
    }
}
