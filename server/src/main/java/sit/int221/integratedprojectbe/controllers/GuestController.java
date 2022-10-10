package sit.int221.integratedprojectbe.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.int221.integratedprojectbe.dtos.CreateEventDTO;
import sit.int221.integratedprojectbe.dtos.EventDetailsDTO;
import sit.int221.integratedprojectbe.entities.Event;
import sit.int221.integratedprojectbe.entities.EventCategory;
import sit.int221.integratedprojectbe.repositories.EventRepository;
import sit.int221.integratedprojectbe.services.EmailService;
import sit.int221.integratedprojectbe.services.EventCategoryService;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EventCategoryService eventCategoryService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addEventGuest(@RequestBody CreateEventDTO newEvent) {

        Event event = modelMapper.map(newEvent, Event.class);
        EventCategory eventCategory = eventCategoryService.getCategoryById(newEvent.getCategoryId());
        event.setCategory(eventCategory);
        event.setEventDuration(eventCategory.getEventDuration());

        EventDetailsDTO guests =modelMapper.map(eventRepository.saveAndFlush(event), EventDetailsDTO.class);
        emailService.sendSimpleMessage(guests);
        return ResponseEntity.ok("Your booking event already save and sent to "+ guests.getUserEmail());
    }
}
