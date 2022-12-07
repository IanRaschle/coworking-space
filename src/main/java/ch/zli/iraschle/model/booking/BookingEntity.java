package ch.zli.iraschle.model.booking;

import ch.zli.iraschle.model.user.ApplicationUser;
import io.smallrye.common.constraint.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(readOnly = true)
    private long id;

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @Column(name = "duration", nullable = false)
    @NotNull
    private BookingDuration duration;

    @ManyToOne
    @NotNull
    @Fetch(FetchMode.JOIN)
    private ApplicationUser applicationUser;

    @Column(name = "state", nullable = false)
    @NotNull
    private BookingState state;

    public BookingEntity() {}

    public BookingEntity(LocalDate date, BookingDuration duration, ApplicationUser applicationUser, BookingState state) {
        this.date = date;
        this.duration = duration;
        this.applicationUser = applicationUser;
        this.state = state;
    }

    public BookingEntity(long id, LocalDate date, BookingDuration duration, ApplicationUser applicationUser, BookingState state) {
        this.id = id;
        this.date = date;
        this.duration = duration;
        this.applicationUser = applicationUser;
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BookingDuration getDuration() {
        return duration;
    }

    public void setDuration(BookingDuration duration) {
        this.duration = duration;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public BookingState getState() {
        return state;
    }

    public void setState(BookingState state) {
        this.state = state;
    }
}
