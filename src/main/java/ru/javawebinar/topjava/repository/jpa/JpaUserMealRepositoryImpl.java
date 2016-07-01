package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * User: gkisline
 * Date: 26.08.2014
 */

@Repository
@Transactional(readOnly = true)
public class JpaUserMealRepositoryImpl implements UserMealRepository {
        @Converter(autoApply = true)
        public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

            @Override
            public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
                return (locDateTime == null ? null : Timestamp.valueOf(locDateTime));
            }

            @Override
            public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
                return (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime());
            }
        }


    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public UserMeal save(UserMeal userMeal, int userId) {
        User ref = em.getReference(User.class, userId);
        Assert.notNull(ref,"чтото неправильно");
        userMeal.setUser(ref);
        if (userMeal.isNew()) {
            em.persist(userMeal);
            return userMeal;
        } else {
            return em.merge(userMeal);
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(UserMeal.DELETE).setParameter("id", id).setParameter("userId",userId).executeUpdate() != 0;
    }

    @Override
    public UserMeal get(int id, int userId) {
//       return em.find(UserMeal.class, id);
        return em.createNamedQuery(UserMeal.GET, UserMeal.class).setParameter(1, id).setParameter(2,userId).getSingleResult();
    }





    @Override
    public List<UserMeal> getAll(int userId) {
        return em.createNamedQuery(UserMeal.ALL_SORTED, UserMeal.class).setParameter("userId",userId).getResultList();
    }

    @Override
    public List<UserMeal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return em.createNamedQuery(UserMeal.GET_BETWEEN, UserMeal.class).setParameter(1,startDate).setParameter(2,endDate).setParameter(3,userId).getResultList();
    }
}