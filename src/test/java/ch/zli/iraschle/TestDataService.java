package ch.zli.iraschle;

import ch.zli.iraschle.model.booking.BookingDuration;
import ch.zli.iraschle.model.booking.BookingEntity;
import ch.zli.iraschle.model.booking.BookingState;
import ch.zli.iraschle.model.user.ApplicationUserEntity;
import static ch.zli.iraschle.util.PasswortHashing.*;

import ch.zli.iraschle.model.user.Role;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;

@ApplicationScoped
@IfBuildProfile("test")
public class TestDataService {
    @Inject
    EntityManager entityManager;

    @Transactional
    public void loadTestData(@Observes StartupEvent startupEvent) {
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE bookingentity RESTART IDENTITY").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE applicationuserentity RESTART IDENTITY").executeUpdate();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();

        ApplicationUserEntity hans = getTestAdmin();

        ApplicationUserEntity jonas = getTestMember();

        BookingEntity booking1 = new BookingEntity();
        booking1.setDate(LocalDate.of(2022, 12, 1));
        booking1.setState(BookingState.ACCEPTED);
        booking1.setDuration(BookingDuration.FULLDAY);
        booking1.setApplicationUser(hans);

        BookingEntity booking2 = new BookingEntity();
        booking2.setDate(LocalDate.of(2022, 12, 10));
        booking2.setState(BookingState.PENDING);
        booking2.setDuration(BookingDuration.MORNING);
        booking2.setApplicationUser(hans);

        BookingEntity booking3 = new BookingEntity();
        booking3.setDate(LocalDate.of(2022, 12, 3));
        booking3.setState(BookingState.ACCEPTED);
        booking3.setDuration(BookingDuration.AFTERNOON);
        booking3.setApplicationUser(jonas);

        BookingEntity booking4 = new BookingEntity();
        booking4.setDate(LocalDate.of(2022, 12, 5));
        booking4.setState(BookingState.DENIED);
        booking4.setDuration(BookingDuration.FULLDAY);
        booking4.setApplicationUser(jonas);

        entityManager.persist(hans);
        entityManager.persist(jonas);

        entityManager.persist(booking1);
        entityManager.persist(booking2);
        entityManager.persist(booking3);
        entityManager.persist(booking4);
    }

    public ApplicationUserEntity getTestAdmin() {
        ApplicationUserEntity applicationUser = new ApplicationUserEntity();
        applicationUser.setFirstname("Hans-ueli");
        applicationUser.setLastname("Bra");
        applicationUser.setEmail("hans.bra@gmail.com");
        applicationUser.setPassword(hashPassword("admin"));
        applicationUser.setBookings(Collections.emptySet());
        applicationUser.setRole(Role.ADMINISTRATOR);
        return applicationUser;
    }

    public ApplicationUserEntity getTestMember() {
        ApplicationUserEntity applicationUser = new ApplicationUserEntity();
        applicationUser.setFirstname("Jonas");
        applicationUser.setLastname("Lukas");
        applicationUser.setEmail("jonas.lukas@gmail.com");
        applicationUser.setPassword(hashPassword("chronist"));
        applicationUser.setBookings(Collections.emptySet());
        applicationUser.setRole(Role.MEMBER);
        return applicationUser;
    }
}
