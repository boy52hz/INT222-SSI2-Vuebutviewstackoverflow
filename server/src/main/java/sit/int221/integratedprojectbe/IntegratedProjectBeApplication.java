package sit.int221.integratedprojectbe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;


import javax.mail.MessagingException;

@SpringBootApplication
public class IntegratedProjectBeApplication {


    public static void main(String[] args) {
        SpringApplication.run(IntegratedProjectBeApplication.class, args);
    }
//    @EventListener(ApplicationReadyEvent.class)
//    public void triggerMail() throws MessagingException {
//        senderService.sendSimpleMail("spectertv6653@gmail.com",
//                "This is email body",
//                "This is email subject");
//
//    }


}
