package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * GKislin
 * 11.01.2015.
 */

@NamedQueries({
        @NamedQuery(name = UserMeal.DELETE, query = "DELETE FROM UserMeal um WHERE um.id=:id AND um.user.id=:userId"),
        @NamedQuery(name = UserMeal.ALL_SORTED, query = "SELECT um FROM UserMeal um WHERE um.user.id=:userId ORDER BY um.dateTime DESC"),
        @NamedQuery(name = UserMeal.GET_BETWEEN, query = "SELECT um FROM UserMeal um WHERE um.dateTime BETWEEN ?1 AND ?2 AND um.user.id = ?3 ORDER BY um.user.id DESC ")
})
@Entity
@Table(name = "meals", uniqueConstraints = {@UniqueConstraint(columnNames = "user_id", name = "meals_unique_user_datetime_idx"),
        @UniqueConstraint(columnNames = "date_time", name = "meals_unique_user_datetime_idx")})

public class UserMeal extends BaseEntity {

    public static final String DELETE = "UserMeal.delete";
    public static final String ALL_SORTED = "UserMeal.getAllSorted";
    public static final String GET_BETWEEN = "UserMeal.getBetween";

    @Column (name = "date_time", nullable = false, unique = true,columnDefinition = "timestamp default now()")
    @NotNull
    private LocalDateTime dateTime;

    @Column (name = "description", nullable = false)
    @NotEmpty
    private String description;

    @Column (name = "calories", nullable = false)
    @Digits(fraction = 0, integer = 4)
    protected int calories;



//    @CollectionTable(name = "users", joinColumns = @JoinColumn(name = "id"))
//    @Column (name="id")
//    @NotEmpty
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



    public UserMeal() {
    }

    public UserMeal(UserMeal um)
    {
        this(um.getId(),um.getDateTime(), um.getDescription(), um.getCalories());
    }
    public UserMeal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public UserMeal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserMeal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
