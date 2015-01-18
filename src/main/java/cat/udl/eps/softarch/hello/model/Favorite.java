package cat.udl.eps.softarch.hello.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by hellfish90 on 18/01/15.
 */
@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @OneToOne // need to review the relationship
            Acte acte;

    @NotNull
    @OneToOne // need to review the relationship
    User user;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    Date recordHour;

    public Favorite(){

    }

    public Favorite(Acte acte, User user, Date recordHour) {
        this.acte = acte;
        this.user = user;
        this.recordHour = recordHour;
    }

    public void reminderEvent(Date currentData){

        if (acte.getInit_date().equals(currentData)){
            sendMail();
        }

    }

    private void sendMail(){

        String email =user.getEmail();

        /**
         * Review how to send mail
         *
         */

    }

    public long getId() {
        return id;
    }

    public Acte getActe() {
        return acte;
    }

    public void setActe(Acte acte) {
        this.acte = acte;
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

        if (!acte.equals(favorite.acte)) return false;
        if (!recordHour.equals(favorite.recordHour)) return false;
        if (!user.equals(favorite.user)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;

        return result;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "event=" + acte +
                ", user=" + user +
                ", recordHour=" + recordHour +
                '}';
    }
}
