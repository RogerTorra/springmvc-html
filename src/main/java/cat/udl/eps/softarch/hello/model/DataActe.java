package cat.udl.eps.softarch.hello.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by roger on 24/12/2014.
 */
@Entity
public class DataActe {

    @NotNull
    private Date date;
}
