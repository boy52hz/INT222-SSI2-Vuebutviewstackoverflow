package sit.int221.integratedprojectbe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import sit.int221.integratedprojectbe.dtos.EventDetailsDTO;

@Component
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}") private String sender;
    public  EventDetailsDTO sendSimpleMessage(EventDetailsDTO details) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(details.getUserEmail());
        message.setSubject("CategoryS-" +details.getCategory().getCategoryName());
        message.setText("You event was create" +
                "\n" +
                "Booking name: " +details.getBookingName()
        + "\n"+
        "Event notes: "+details.getEventNotes()
        + "\n"
        + "Event duration: "+details.getEventDuration()
        +"\n"
        +"Event start time: "+details.getEventStartTime());
        emailSender.send(message);
        return details;

    }
}