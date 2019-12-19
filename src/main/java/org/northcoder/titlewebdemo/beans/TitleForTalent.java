package org.northcoder.titlewebdemo.beans;

import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.northcoder.titlewebdemo.util.Utils;

/**
 * For a given talent (person) , this represents one title on which the talent
 * worked (e.g as an actor, director, producer, etc.). A collection of these
 * beans represents the talent's TV and filmography.<p>
 *
 * Some people may be listed in thousands of different titles (for example,
 * producers who have been associated with episodes of long-running TV shows).
 * In such cases, we do not select the entire list of titles. Instead we
 * summarize the data by counting titles instead of listing them.
 */
public class TitleForTalent extends DemoBean implements Comparable<TitleForTalent> {

    public TitleForTalent() {
        super();
    }

    public TitleForTalent(String talentID) {
        super();
        this.talentID = talentID;
    }

    /**
     * If the list of titles is lengthy, we attempt to summarize it by counting
     * episodes instead of listing them all.
     */
    public static final String SQL_SELECT_ALL_BY_TALENT = String.join(" ",
            "select",
            "  srs.title_id,",
            "  ct.content_type_name,",
            "  srs.primary_title,",
            "  srs.start_year,",
            "  te.season_number,",
            "  cat.category_name,",
            "  count(t.title_id) as episode_count",
            "from imdb.title_principal tp",
            "inner join imdb.title t",
            "on t.title_id = tp.title_id",
            "left outer join imdb.category cat",
            "on cat.category_id = tp.category_id",
            "inner join imdb.title_episode te",
            "on te.title_id = t.title_id",
            "inner join imdb.title srs",
            "on srs.title_id = te.parent_title_id",
            "inner join imdb.content_type ct",
            "on srs.content_type_id = ct.content_type_id",
            "where tp.talent_id = :talentID",
            "group by srs.title_id, ct.content_type_name,",
            "  srs.primary_title, srs.start_year,",
            "te.season_number, cat.category_name",
            "union",
            "select",
            "  t.title_id,",
            "  ct.content_type_name,",
            "  t.primary_title,",
            "  t.start_year,",
            "  null as season_number,",
            "  cat.category_name,",
            "  null as episodes",
            "from imdb.title_principal tp",
            "left outer join imdb.category cat",
            "on cat.category_id = tp.category_id",
            "inner join imdb.title t",
            "on t.title_id = tp.title_id",
            "inner join imdb.content_type ct",
            "on t.content_type_id = ct.content_type_id",
            "where ct.content_type_id not in (4, 5)",
            "and tp.talent_id = :talentID");

    private transient String talentID;

    @SerializedName("title_id")
    @ColumnName("title_id")
    private String titleID;

    @SerializedName("content_type_name")
    @ColumnName("content_type_name")
    private String contentTypeName;

    @SerializedName("primary_title")
    @ColumnName("primary_title")
    private String primaryTitle;

    @SerializedName("start_year")
    @ColumnName("start_year")
    private Integer startYear;

    @SerializedName("season_number")
    @ColumnName("season_number")
    private Integer seasonNumber;

    @SerializedName("episode_count")
    @ColumnName("episode_count")
    private Integer episodeCount;

    @SerializedName("category_name")
    @ColumnName("category_name")
    private String categoryName;

    public String getTalentID() {
        return talentID;
    }

    public void setTalentID(String talentID) {
        this.talentID = talentID;
    }

    public String getTitleID() {
        return titleID;
    }

    public void setTitleID(String titleID) {
        this.titleID = titleID;
    }

    public String getContentTypeName() {
        return contentTypeName;
    }

    public void setContentTypeName(String contentTypeName) {
        this.contentTypeName = contentTypeName;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }

    public void setPrimaryTitle(String primaryTitle) {
        this.primaryTitle = primaryTitle;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSearchData() {
        return Utils.buildExtraSearchTerms(String.join(" ",
                this.getPrimaryTitle()));
    }

    public static List<String> getColumnHeadings() {
        return Arrays.asList(
                "Title ID", // edit links will be shown in this column, so no heading.
                "Content Type",
                "Title",
                "Year",
                "Season",
                "Episode Count",
                "Category"
        );
    }

    public static List<String> getRowValues() {
        return Arrays.asList( // which bean field to use for each row cell in a table:
                "titleID",
                "contentTypeName",
                "primaryTitle",
                "startYear",
                "seasonNumber",
                "episodeCount",
                "categoryName"
        );
    }

    @Override
    public int compareTo(TitleForTalent other) {
        int thisYear;
        if (startYear == null) {
            thisYear = 9999;
        } else {
            thisYear = startYear;
        }

        int otherYear;
        if (other.getStartYear() == null) {
            otherYear = 9999;
        } else {
            otherYear = other.getStartYear();
        }

        int yearCheck = thisYear - otherYear;

        if (yearCheck == 0) {
            return Utils.compareUsingCollator(this.getPrimaryTitle(), other.getPrimaryTitle());
        }
        return 0;
    }
    
    // Based on the PK of the underlying tables, but also taking account
    // of season/episode data:
    @Override
    public int hashCode() {
        return new HashCodeBuilder(41, 59)
                .append(talentID)
                .append(titleID)
                .append(startYear)
                .append(seasonNumber)
                .append(episodeCount)
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
        TitleForTalent other = (TitleForTalent) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(talentID, other.talentID)
                .append(titleID, other.titleID)
                .append(startYear, other.startYear)
                .append(seasonNumber, other.seasonNumber)
                .append(episodeCount, other.episodeCount)
                .isEquals();
    }
    
}
