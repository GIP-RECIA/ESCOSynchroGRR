package fr.recia.grr.batch.synchronisation.entity.dao;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "grr_entry_moderate")
public class GrrEntryModerate implements Serializable {



    /*
     * ===============================================
     * Propriétés de la classe
     * ===============================================
     */

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private GrrRoom room_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repeat_id")
    private GrrRepeat reservationRepeat;

    @Column(name = "end_time")
    private long end_time;



    /*
     * ===============================================
     * Constructeurs de la classe
     * ===============================================
     */


    public GrrEntryModerate() {

    }


    /*
     * ===============================================
     * Getter / Setter de la classe
     * ===============================================
     */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public GrrRoom getRoom_id() {
        return room_id;
    }

    public void setRoom_id(GrrRoom room_id) {
        this.room_id = room_id;
    }

    public GrrRepeat getReservationRepeat() {
        return reservationRepeat;
    }

    public void setReservationRepeat(GrrRepeat reservationRepeat) {
        this.reservationRepeat = reservationRepeat;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

}
