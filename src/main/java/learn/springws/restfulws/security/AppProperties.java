package learn.springws.restfulws.security;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

    private final Environment env;

    public AppProperties(Environment env) {
        this.env = env;
    }

    public String getTokenSecret() {
        return env.getProperty("tokenSecret");
    }
}
