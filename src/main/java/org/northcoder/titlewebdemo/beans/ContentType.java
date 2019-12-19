package org.northcoder.titlewebdemo.beans;

import com.google.gson.annotations.SerializedName;
import javax.validation.constraints.Size;
import org.northcoder.titlewebdemo.util.Utils;
import org.northcoder.titlewebdemo.validation.Mandatory;
import org.northcoder.titlewebdemo.validation.ValidationHandler;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * IMDb content types include "movie" "TV episode", etc.
 */
public class ContentType extends DemoBean implements Comparable<ContentType> {

    public ContentType() {
        super();
    }

    public ContentType(Integer contentTypeID) {
        super();
        this.contentTypeID = contentTypeID;
    }

    public static final String SQL_SELECT_ALL = String.join(" ",
            "select ct.content_type_id, ct.content_type_name",
            "from imdb.content_type ct");

    public static final String SQL_SELECT_BY_ID = String.join(" ",
            SQL_SELECT_ALL, "where ct.content_type_id = :contentTypeID");

    public static final String UPDATE_RECORD = String.join(" ",
            "update imdb.content_type set content_type_name = ?",
            "where content_type_id = :contentTypeID");

    @Mandatory(fieldName = "content type ID", message = ValidationHandler.MANDATORY)
    @SerializedName("content_type_id")
    @ColumnName("content_type_id")
    private Integer contentTypeID;

    @Mandatory(fieldName = "content type name", message = ValidationHandler.MANDATORY)
    @Size(min = 3, max = 100, message = ValidationHandler.MIN_MAX_LENGTH)
    @SerializedName("content_type_name")
    @ColumnName("content_type_name")
    private String contentTypeName;

    public Integer getContentTypeID() {
        return contentTypeID;
    }

    public void setContentTypeID(Integer contentTypeID) {
        this.contentTypeID = contentTypeID;
    }

    public String getContentTypeName() {
        return contentTypeName;
    }

    public void setContentTypeName(String contentTypeName) {
        this.contentTypeName = contentTypeName;
    }

    @Override
    public int compareTo(ContentType other) {
        return Utils.compareUsingCollator(this.getContentTypeName(), other.getContentTypeName());
    }

    // Based on the PK of the underlying table:
    @Override
    public int hashCode() {
        return new HashCodeBuilder(211, 269)
                .append(contentTypeID)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        ContentType other = (ContentType) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(contentTypeID, other.contentTypeID)
                .isEquals();
    }

}
