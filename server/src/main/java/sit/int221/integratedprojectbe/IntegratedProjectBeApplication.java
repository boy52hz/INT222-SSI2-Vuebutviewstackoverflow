package sit.int221.integratedprojectbe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


import javax.mail.MessagingException;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class IntegratedProjectBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntegratedProjectBeApplication.class, args);
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("OASIP-");
        executor.initialize();
        return executor;
    }
}
