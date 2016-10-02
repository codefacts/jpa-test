package jpatest.models;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Jango on 10/2/2016.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Supervisor extends Employee {
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Ac ac;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<House> houses;

    public Ac getAc() {
        return ac;
    }

    public void setAc(Ac ac) {
        this.ac = ac;
    }

    public List<House> getHouses() {
        return houses;
    }

    public void setHouses(List<House> houses) {
        this.houses = houses;
    }

    @Override
    public String toString() {
        return "Supervisor{" +
            "ac=" + ac +
            ", houses=" + houses +
            '}';
    }
}
