package sit.int221.integratedprojectbe.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDTO {
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 100, message = "size must be between 1 and 100")
    private String bookingName;

    @NotNull(message = "must not be null")
    private Integer userId;

    @NotNull(message = "must not be null")
    private Integer categoryId;

    @NotNull(message = "must not be null")
    @Future(message = "must be a future date")
    private Date eventStartTime;

    @Size(max = 500, message = "size must be between 0 and 500")
    private String eventNotes;
}
