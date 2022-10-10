package sit.int221.integratedprojectbe.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer bookingId;

    @Column(nullable = false, length = 100)
    private String bookingName;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "categoryId", nullable = false)
    private EventCategory category;

    @Column(nullable = false)
    private Date eventStartTime;

    @Column(nullable = false)
    private Integer eventDuration;

    @Lob
    @Column
    private String eventNotes;
}