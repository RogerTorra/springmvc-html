package cat.udl.eps.softarch.hello.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by http://rhizomik.net/~roberto/
 */
@Entity
@XmlRootElement
public class Acte {

    @Id
    @XmlElement
    private long id;

    @XmlElement
    @NotBlank(message = "Nom cannot be blank")
    private String nom;


    @NotNull
    private String date;

    public Acte() {}

    public Acte(String nom, String date) {
        this.nom = nom;
        this.date = date;
    }


    public long getId() { return id; }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }
}
