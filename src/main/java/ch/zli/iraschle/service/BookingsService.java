package ch.zli.iraschle.service;

import ch.zli.iraschle.model.booking.BookingEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class BookingsService {
    @Inject
    EntityManager entityManager;

    @Transactional
    public BookingEntity createBooking(BookingEntity bookingEntity) {
      entityManager.persist(bookingEntity);
      return entityManager.find(BookingEntity.class, bookingEntity);
    }
}
