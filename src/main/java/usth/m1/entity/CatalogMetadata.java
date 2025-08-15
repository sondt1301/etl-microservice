package usth.m1.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(
        name = "catalog_metadata",
        uniqueConstraints = @UniqueConstraint(name = "uk_cm_image_id", columnNames = "image_id"),
        indexes = @Index(name = "idx_cm_datetime", columnList = "datetime_utc")
)
public class CatalogMetadata extends PanacheEntity {
    @Column(name = "image_id", nullable = false, unique = true)
    private String imageId;

    private String collection;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> geometry;

    @Column(columnDefinition = "jsonb")
    private String bbox;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> properties;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> assets;

    @Column(columnDefinition = "jsonb")
    private String links;

    private String trueColorImgPath;

    private String rawImgPath;

    @Column(name="datetime_utc")
    private OffsetDateTime datetimeUtc;

    @CreationTimestamp
    @Column(name="created_at")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private OffsetDateTime updatedAt;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public Map<String, Object> getGeometry() {
        return geometry;
    }

    public void setGeometry(Map<String, Object> geometry) {
        this.geometry = geometry;
    }

    public String getBbox() {
        return bbox;
    }

    public void setBbox(String bbox) {
        this.bbox = bbox;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Map<String, Object> getAssets() {
        return assets;
    }

    public void setAssets(Map<String, Object> assets) {
        this.assets = assets;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getTrueColorImgPath() {
        return trueColorImgPath;
    }

    public void setTrueColorImgPath(String trueColorImgPath) {
        this.trueColorImgPath = trueColorImgPath;
    }

    public String getRawImgPath() {
        return rawImgPath;
    }

    public void setRawImgPath(String rawImgPath) {
        this.rawImgPath = rawImgPath;
    }

    public OffsetDateTime getDatetimeUtc() {
        return datetimeUtc;
    }

    public void setDatetimeUtc(OffsetDateTime datetimeUtc) {
        this.datetimeUtc = datetimeUtc;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
