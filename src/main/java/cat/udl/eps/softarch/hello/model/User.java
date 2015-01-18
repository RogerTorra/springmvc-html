package cat.udl.eps.softarch.hello.model;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
/**
 * Created by roger on 17/01/2015.
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "E-mail cannot be blank")
    @Email(message = "E-mail should be valid")
    private String email;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Acte> actes = new ArrayList<>();

    public User() { }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public long getId() { return id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public List<Acte> getActes() {
        return actes;
    }

    public void addActe(Acte newActe) {
        actes.add(newActe);
    }

    public void removeActe(Acte acte) {
        actes.remove(acte);
    }
}