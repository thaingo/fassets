package io.github.fasset.fasset;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Main fields for models used in this business doamin
 *
 * @author edwin.njeru
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class DomainModel<U> {

    @GeneratedValue
    @Id
    private int id;

    @Version
    private int version;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @CreatedBy
    private U createdBy;

    @LastModifiedBy
    private U lastModifiedBy;

    public DomainModel() {
    }

    public int getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public U getCreatedBy() {
        return createdBy;
    }

    public U getLastModifiedBy() {
        return lastModifiedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainModel<?> that = (DomainModel<?>) o;
        return id == that.id &&
                version == that.version &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(modifiedAt, that.modifiedAt) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(lastModifiedBy, that.lastModifiedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, createdAt, modifiedAt, createdBy, lastModifiedBy);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DomainModel{");
        sb.append("id=").append(id);
        sb.append(", version=").append(version);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", modifiedAt=").append(modifiedAt);
        sb.append(", createdBy=").append(createdBy);
        sb.append(", lastModifiedBy=").append(lastModifiedBy);
        sb.append('}');
        return sb.toString();
    }
}
