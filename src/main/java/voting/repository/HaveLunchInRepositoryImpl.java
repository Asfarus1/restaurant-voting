package voting.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import voting.domain.Lunch;
import voting.domain.Restaurant;
import voting.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;

@Repository
public class HaveLunchInRepositoryImpl implements HaveLunchInRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void haveLunchIn(Long restId, LocalDate date, Long userId) {
        Restaurant restaurant = em.getReference(Restaurant.class, restId);
        User user = em.getReference(User.class, userId);
        Query query = em.createQuery("UPDATE Lunch l SET l.restaurant=?1 WHERE l.date=?2 AND l.user=?3");
        query.setParameter(1, restaurant);
        query.setParameter(2, date);
        query.setParameter(3, user);
        if (query.executeUpdate() == 0) {
            em.persist(new Lunch(user, date, restaurant));
        }
    }
}
