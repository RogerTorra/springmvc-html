package cat.udl.eps.softarch.hello.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by http://rhizomik.net/~roberto/
 */
@Entity
@XmlRootElement
public class Acte {

    @Id
    private long id;

    @NotBlank(message = "Nom cannot be blank")
    private String nom;


    @NotNull
    private String data_inici;

    public Acte() {}

    public Acte(String nom, String data_inici) {
        this.nom = nom;
        this.data_inici = data_inici;
    }

    @XmlElement
    public long getId() { return id; }

    public void setId(long id) {
        this.id = id;
    }
    @XmlElement
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getData_inici() { return data_inici; }

    public void setData_inici(String data_inici) { this.data_inici = data_inici; }

    @Override
    public String toString() {
        return "Acte{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", data_inici='" + data_inici + '\'' +
                '}';
    }
}
