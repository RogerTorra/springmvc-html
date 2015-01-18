package cat.udl.eps.softarch.hello.model;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import sun.util.calendar.BaseCalendar;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by hellfish90 on 18/01/15.
 */
@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Event cannot be blank")
    @OneToOne // need to review the relationship
    Event event;

    @NotBlank(message = "User cannot be blank")
    @OneToOne // need to review the relationship
    User user;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    Date recordHour;


    public Favorite(Event event, User user, Date recordHour) {
        this.event = event;
        this.user = user;
        this.recordHour = recordHour;
    }


    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Date getRecordHour() {
        return recordHour;
    }

    public void setRecordHour(Date recordHour) {
        this.recordHour = recordHour;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Favorite favorite = (Favorite) o;

        if (!event.equals(favorite.event)) return false;
        if (!recordHour.equals(favorite.recordHour)) return false;
        if (!user.equals(favorite.user)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = event.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + recordHour.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "event=" + event +
                ", user=" + user +
                ", recordHour=" + recordHour +
                '}';
    }
}
