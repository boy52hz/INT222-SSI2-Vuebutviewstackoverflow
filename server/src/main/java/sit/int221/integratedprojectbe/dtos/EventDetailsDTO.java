package sit.int221.integratedprojectbe.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDetailsDTO {
    private Integer bookingId;
    private String bookingName;
    private EventDetailCategoryDTO category;
    private Date eventStartTime;
    private Integer eventDuration;
    private String userEmail;
    private String eventNotes;
}
