package ch.zli.iraschle.service;

import ch.zli.iraschle.model.booking.BookingEntity;
import ch.zli.iraschle.model.booking.BookingState;
import ch.zli.iraschle.model.user.ApplicationUserEntity;
import ch.zli.iraschle.model.user.Role;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static ch.zli.iraschle.util.WebApplicationExceptionFactory.*;

@ApplicationScoped
public class BookingService {
    @Inject
    EntityManager entityManager;

    @Inject
    ApplicationUserService applicationUserService;

    public List<BookingEntity> getBookings() {
        var query = entityManager.createQuery("FROM BookingEntity", BookingEntity.class);
        return query.getResultList();
    }

    public List<BookingEntity> getBookingsSorted() {
        List<BookingEntity> bookings = getBookings();
        return bookings.stream().sorted(Comparator.comparing(BookingEntity::getDate)).toList();
    }

    public BookingEntity getBooking(long id) {
        BookingEntity bookingEntity = entityManager.find(BookingEntity.class, id);
        if (bookingEntity == null) {
            throw NO_BOOKING_WITH_ID;
        }
        return bookingEntity;
    }

    @Transactional
    public BookingEntity createBooking(BookingEntity bookingEntity, String email) {
        ApplicationUserEntity applicationUserWithEmail = applicationUserService.getApplicationUserWithEmail(email);
        if (applicationUserWithEmail == null) {
            throw NO_USER_WITH_ID;
        }
        bookingEntity.setApplicationUser(applicationUserWithEmail);
        Set<BookingEntity> bookings = applicationUserWithEmail.getBookings();
        bookings.add(bookingEntity);
        applicationUserWithEmail.setBookings(bookings);
        entityManager.persist(bookingEntity);
        return bookingEntity;
    }

    public BookingState getBookingStateOf(long id, String role, String email) {
        BookingEntity bookingEntity = getBooking(id);
        if (bookingEntity == null) {
            throw NO_BOOKING_WITH_ID;
        }
        if (!role.equals(Role.ADMINISTRATOR.name()) && !email.equals(bookingEntity.getApplicationUser().getEmail())) {
            throw NOT_YOUR_BOOKING;
        }
        return bookingEntity.getState();
    }

    @Transactional
    public void deleteBooking(Long id, String role, String email) {
        BookingEntity bookingEntity = getBooking(id);
        if (bookingEntity == null) {
            throw NO_BOOKING_WITH_ID;
        }
        if (!role.equals(Role.ADMINISTRATOR.name()) && !email.equals(bookingEntity.getApplicationUser().getEmail())) {
            throw NOT_YOUR_BOOKING;
        }
        entityManager.remove(bookingEntity);
    }

    @Transactional
    public BookingEntity updateBooking(BookingEntity newBookingEntity) {
        BookingEntity bookingEntity = getBooking(newBookingEntity.getId());
        if (bookingEntity == null) {
            throw NO_BOOKING_WITH_ID;
        }
        entityManager.merge(newBookingEntity);
        return bookingEntity;
    }

    @Transactional
    public BookingEntity changeBookingState(long id, BookingState state) {
        BookingEntity bookingEntity = getBooking(id);
        if (bookingEntity == null) {
            throw NO_BOOKING_WITH_ID;
        }
        bookingEntity.setState(state);
        entityManager.persist(bookingEntity);
        return bookingEntity;
    }
}
