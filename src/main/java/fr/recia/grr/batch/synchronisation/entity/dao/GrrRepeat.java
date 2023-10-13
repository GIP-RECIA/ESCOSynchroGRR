package fr.recia.grr.batch.synchronisation.entity.dao;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;


@Entity
@Table(name = "grr_repeat")
public class GrrRepeat implements Serializable {

    /*
     * ===============================================
     * Propriétés de la classe
     * ===============================================
     */

    @OneToMany(mappedBy = "reservationRepeat")
    private Collection<GrrEntry> reservation;

    @Column(name = "end_date")
    private long end_date;

    /**
     * Constructeur par defaut.
     */
    public GrrRepeat() {
    }

    /*
     * ===============================================
     * Getter / Setter de la classe
     * ===============================================
     */

    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * Getter de la proprieté reservation
     * @return la proprieté reservation
     */
    public Collection<GrrEntry> getReservation() {
        return reservation;
    }

    /**
     * Setter de la proprieté reservation
     * @param reservation
     */
    public void setReservation(Collection<GrrEntry> reservation) {
        this.reservation = reservation;
    }

    public long getEnd_date() {
        return end_date;
    }

    public void setEnd_date(long end_date) {
        this.end_date = end_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
