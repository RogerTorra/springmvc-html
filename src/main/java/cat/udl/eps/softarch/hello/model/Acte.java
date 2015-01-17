package cat.udl.eps.softarch.hello.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by roger
 */
@Entity
@XmlRootElement
public class Acte {

    @Id
    private long id;

    @NotBlank(message = "Nom cannot be blank")
    private String name;

    @NotNull
    private String init_date;

    @NotNull
    private String start_time;

    @NotNull
    private List<String> type;

    @NotNull
    private String localization;

    @NotNull
    private String street;

    @NotNull
    private String street_num;

    @NotNull
    private String district;

    @NotNull
    private String CP;

    @NotNull
    private String x;

    @NotNull
    private String y;



    public Acte() {}

    public Acte(String name, String init_date) {
        this.name = name;
        this.init_date = init_date;
    }

    @XmlElement
    public long getId() { return id; }

    public void setId(long id) {
        this.id = id;
    }
    @XmlElement
    public String getName() {
        return name;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet_num() {
        return street_num;
    }

    public void setStreet_num(String street_num) {
        this.street_num = street_num;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCP() {
        return CP;
    }

    public void setCP(String CP) {
        this.CP = CP;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInit_date() { return init_date; }

    public void setInit_date(String init_date) { this.init_date = init_date; }

    @Override
    public String toString() {
        return "Acte{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", init_date='" + init_date + '\'' +
                ", start_time='" + start_time + '\'' +
                ", type='" + type + '\'' +
                ", localization='" + localization + '\'' +
                ", street='" + street + '\'' +
                ", street_num='" + street_num + '\'' +
                ", district='" + district + '\'' +
                ", CP='" + CP + '\'' +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Acte acte = (Acte) o;

        if (id != acte.id) return false;
        if (!name.equals(acte.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        return result;
    }
}
