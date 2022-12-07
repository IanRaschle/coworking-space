package ch.zli.iraschle.service;

import ch.zli.iraschle.model.booking.BookingEntity;
import ch.zli.iraschle.model.booking.BookingState;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;

@ApplicationScoped
public class BookingService {
    @Inject
    EntityManager entityManager;

    public List<BookingEntity> getBookings() {
        var query = entityManager.createQuery("FROM BookingEntity", BookingEntity.class);
        return query.getResultList();
    }

    public List<BookingEntity> getBookingsSorted() {
        List<BookingEntity> bookings = getBookings();
        return bookings.stream().sorted(Comparator.comparing(BookingEntity::getDate)).toList();
    }

    public BookingEntity getBooking(long id) {
        return entityManager.find(BookingEntity.class, id);
    }

    @Transactional
    public BookingEntity createBooking(BookingEntity bookingEntity) {
        entityManager.persist(bookingEntity);
        return bookingEntity;
    }

    public BookingState getBookingStateOf(long id) {
        BookingEntity bookingEntity = getBooking(id);
        return bookingEntity.getState();
    }

    @Transactional
    public void deleteBooking(Long id) {
        entityManager.remove(getBooking(id));
    }

    @Transactional
    public BookingEntity updateBooking(BookingEntity bookingEntity) {
        entityManager.merge(bookingEntity);
        return bookingEntity;
    }

    @Transactional
    public BookingEntity changeBookingState(long id, BookingState state) {
        BookingEntity bookingEntity = getBooking(id);
        bookingEntity.setState(state);
        entityManager.persist(bookingEntity);
        return bookingEntity;
    }
}
