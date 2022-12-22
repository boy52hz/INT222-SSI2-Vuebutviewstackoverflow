package sit.int221.integratedprojectbe.entities;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EventCategoryHasUserKey implements Serializable {
    private static final long serialVersionUID = 4562678557785329253L;
    @Column(name = "categoryId", nullable = false)
    private Integer categoryId;

    @Column(name = "email", nullable = false)
    private String email;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventCategoryHasUserKey entity = (EventCategoryHasUserKey) o;
        return Objects.equals(this.email, entity.email) &&
                Objects.equals(this.categoryId, entity.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, categoryId);
    }
}