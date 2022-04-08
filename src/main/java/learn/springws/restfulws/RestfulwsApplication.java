package learn.springws.restfulws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class RestfulwsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestfulwsApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     This application context bean is added to config in order to initialize RestfulwsApplicationContext bean
     and be able to use its static fields in static method calls, e.g to get beans in AuthenticationFilter.
     */
    @Bean
    public RestfulwsApplicationContext restfulwsApplicationContext() {
        return new RestfulwsApplicationContext();
    }
}
