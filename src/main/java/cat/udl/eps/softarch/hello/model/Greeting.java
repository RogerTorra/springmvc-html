package cat.udl.eps.softarch.hello.model;

import java.util.Date;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by http://rhizomik.net/~roberto/
 */
@Entity
public class Greeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Content cannot be blank")
    @Size(max = 256, message = "Content maximum length is {max} characters long")
    private String content;

    @NotNull(message = "E-mail cannot be empty")
    @Email(message = "E-mail should be valid")
    private String email;

    @NotNull
    private String date;

    public Greeting() {}

    public Greeting(String content, String email, String date) {
        this.content = content;
        this.email = email;
        this.date = date;
    }

    public long getId() { return id; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }
}
