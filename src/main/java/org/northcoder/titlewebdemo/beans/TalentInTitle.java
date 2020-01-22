package org.northcoder.titlewebdemo.beans;

import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.northcoder.titlewebdemo.util.Utils;

/**
 * For a given title, this represents one of the talent (actors, director, etc.)
 * who worked on the title. A collection of these beans represents the
 * top-billed cast and crew for the title.
 * <p>
 *
 * A title may have up to approximately 10 people listed as associated with it
 * (when using the 'title_principal' table). Therefore the talent listing for
 * any one title will be relatively short.
 */
public class TalentInTitle extends DemoBean implements Comparable<TalentInTitle> {

    public TalentInTitle() {
        super();
    }

    public TalentInTitle(String titleID) {
        super();
        this.titleID = titleID;
    }

    public static final String SQL_SELECT_ALL_BY_TITLE = String.join(" ",
            "select",
            "  t.title_id,",
            "  tal.talent_id,",
            "  tp.ord as \"order\",",
            "  tal.talent_name,",
            "  cat.category_name as category,",
            "  tp.job,",
            "  tp.role_names",
            "from imdb.title t",
            "inner join imdb.title_principal tp",
            "on t.title_id = tp.title_id",
            "inner join imdb.talent tal",
            "on tp.talent_id = tal.talent_id",
            "left outer join imdb.category cat",
            "on tp.category_id = cat.category_id",
            "where t.title_id = :titleID");

    private transient String titleID;

    @SerializedName("talent_id")
    @ColumnName("talent_id")
    private String talentID;

    @SerializedName("order")
    @ColumnName("order")
    private Integer order;

    @SerializedName("talent_name")
    @ColumnName("talent_name")
    private String talentName;

    @SerializedName("category")
    @ColumnName("category")
    private String category;

    @SerializedName("job")
    @ColumnName("job")
    private String job;

    @SerializedName("role_names")
    @ColumnName("role_names")
    private String roleNames;

    public String getTitleID() {
        return titleID;
    }

    public void setTitleID(String titleID) {
        this.titleID = titleID;
    }

    public String getTalentID() {
        return talentID;
    }

    public void setTalentID(String talentID) {
        this.talentID = talentID;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getTalentName() {
        return talentName;
    }

    public void setTalentName(String talentName) {
        this.talentName = talentName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public String getSearchData() {
        return Utils.buildExtraSearchTerms(String.join(" ",
                this.getTalentName(),
                this.getRoleNames()));
    }

    public static List<String> getColumnHeadings() {
        return Arrays.asList(
                "Talent ID", // edit links will be shown in this column, so no heading.
                "Order",
                "Talent Name",
                "Category",
                "Job",
                "Role Name(s)"
        );
    }

    public static List<String> getRowValues() {
        return Arrays.asList( // which bean field to use for each row cell in a table:
                "talentID",
                "order",
                "talentName",
                "category",
                "job",
                "roleNames"
        );
    }

    @Override
    public int compareTo(TalentInTitle other) {
        return this.getOrder().compareTo(other.getOrder());
    }
    
    // Based on the PK of the underlying Title Principal table:
    @Override
    public int hashCode() {
        return new HashCodeBuilder(37, 79)
                .append(titleID)
                .append(talentID)
                .append(order)
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
        TalentInTitle other = (TalentInTitle) obj;
        return new EqualsBuilder()
                .append(titleID, other.titleID)
                .append(talentID, other.talentID)
                .append(order, other.order)
                .isEquals();
    }
    
}
