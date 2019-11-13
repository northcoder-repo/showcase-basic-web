package org.northcoder.titlewebdemo.beans;

import com.google.gson.annotations.SerializedName;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.Nested;
import org.northcoder.titlewebdemo.validation.ValidationHandler;
import org.northcoder.titlewebdemo.validation.Mandatory;
import org.northcoder.titlewebdemo.validation.FirstOrBoth;
import org.northcoder.titlewebdemo.validation.NotLessThanOther;
import org.northcoder.titlewebdemo.util.Utils;
import javax.annotation.Nullable;
import javax.validation.constraints.Size;
import javax.validation.constraints.Digits;
import org.hibernate.validator.constraints.Range;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * IMDb movies, TV episodes, etc.
 */
@FirstOrBoth(firstField = "start year", secondField = "end year", message = ValidationHandler.FIRST_OR_BOTH_MESSAGE)
@NotLessThanOther(firstField = "Start Year", secondField = "End Year", message = ValidationHandler.NOT_LESS_THAN_OTHER_MESSAGE)
public class Title extends DemoBean implements Comparable<Title> {

    public Title() {
        super();
    }

    public Title(String titleID) {
        super();
        this.titleID = titleID;
    }

    public static final String SQL_SELECT_ALL = String.join(" ",
            "select t.title_id, ct.content_type_id, t.primary_title,",
            "t.original_title, t.is_adult, t.start_year, t.end_year,",
            "t.runtime_minutes, ct.content_type_name,",
            "srs.primary_title as series_name,",
            "te.season_number,",
            "te.episode_number",
            "from imdb.title t",
            "inner join imdb.content_type ct",
            "on t.content_type_id = ct.content_type_id",
            "left outer join imdb.title_episode te",
            "on t.title_id = te.title_id",
            "left outer join imdb.title srs",
            "on srs.title_id = te.parent_title_id");

    public static final String SQL_SELECT_BY_ID = String.join(" ",
            SQL_SELECT_ALL, "where t.title_id = :titleID");

    public static final String SQL_UPDATE_BY_ID = String.join(" ",
            "update imdb.title set content_type_id = :contentTypeID,",
            "primary_title = :primaryTitle,",
            "original_title = :originalTitle,",
            "is_adult = :isAdult,",
            "start_year = :startYear,",
            "end_year = :endYear,",
            "runtime_minutes = :runtimeMinutes",
            "where title_id = :titleID");

    @Mandatory(fieldName = "title ID", message = ValidationHandler.MANDATORY)
    @SerializedName("title_id")
    @ColumnName("title_id")
    private String titleID;

    @Mandatory(fieldName = "content type", message = ValidationHandler.MANDATORY)
    @ColumnName("content_type_id")
    private transient Integer contentTypeID;

    @Mandatory(fieldName = "primary title", message = ValidationHandler.MANDATORY)
    @Size(min = 1, max = 500, message = ValidationHandler.MIN_MAX_LENGTH)
    @SerializedName("primary_title")
    @ColumnName("primary_title")
    private String primaryTitle;

    @Nullable
    @Size(min = 1, max = 500, message = ValidationHandler.MIN_MAX_LENGTH)
    @SerializedName("original_title")
    @ColumnName("original_title")
    private String originalTitle;

    @Nullable
    @SerializedName("is_adult")
    @ColumnName("is_adult")
    private Integer isAdult;

    @Nullable
    @SerializedName("start_year")
    @ColumnName("start_year")
    @Digits(integer = 4, fraction = 0, message = "{org.northcoder.titlewebdemo.validation.Integer.message}")
    @Range(min = 1890, max = 2030, message = "{org.northcoder.titlewebdemo.validation.Year.message}")
    private Integer startYear;

    @Nullable
    @SerializedName("end_year")
    @ColumnName("end_year")
    @Digits(integer = 4, fraction = 0, message = "{org.northcoder.titlewebdemo.validation.Integer.message}")
    @Range(min = 1890, max = 2030, message = "{org.northcoder.titlewebdemo.validation.Year.message}")
    private Integer endYear;

    @Nullable
    @SerializedName("runtime_minutes")
    @ColumnName("runtime_minutes")
    @Digits(integer = 5, fraction = 0, message = "{org.northcoder.titlewebdemo.validation.Integer.message}")
    @Range(min = 1, max = 99999, message = "{org.northcoder.titlewebdemo.validation.IntegerRange.message}")
    private Integer runtimeMinutes;

    @Nullable
    @SerializedName("series_name")
    @ColumnName("series_name")
    private String seriesName;

    @Nullable
    @SerializedName("season_number")
    @ColumnName("season_number")
    private Integer seasonNumber;

    @Nullable
    @SerializedName("episode_number")
    @ColumnName("episode_number")
    private Integer episodeNumber;

    //@Nested - see getter!
    @SerializedName("content_type")
    private ContentType contentType;

    @SerializedName("talent")
    private final List<TalentInTitle> talentListForTitle = new ArrayList();

    public String getTitleID() {
        return titleID;
    }

    public void setTitleID(String titleID) {
        this.titleID = titleID;
    }

    public Integer getContentTypeID() {
        return contentTypeID;
    }

    public void setContentTypeID(Integer contentTypeID) {
        this.contentTypeID = contentTypeID;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }

    public void setPrimaryTitle(String primaryTitle) {
        this.primaryTitle = primaryTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Integer getIsAdult() {
        return isAdult;
    }

    public void setIsAdult(Integer isAdult) {
        this.isAdult = isAdult;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public Integer getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public void setRuntimeMinutes(Integer runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
    }

    @Nested
    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public List<TalentInTitle> getTalentListForTitle() {
        return talentListForTitle;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getDisplayTitle() {
        StringBuilder sb = new StringBuilder();
        if (getSeriesDetails() != null && !getSeriesDetails().isBlank()) {
            sb.append(getSeriesDetails()).append(" - ");
        }
        sb.append(primaryTitle);
        return sb.toString();
    }

    public String getSeriesDetails() {
        // Not production code - don't do work in a getter; a season
        // may have more than 999 episodes; and so on... Just a demo!
        StringBuilder sb = new StringBuilder();
        if (seriesName != null && !seriesName.isBlank()) {
            sb.append(seriesName);
            if (seasonNumber != null && seasonNumber > 0
                    && episodeNumber != null && episodeNumber > 0) {
                sb.append(" S").append(String.format("%02d", seasonNumber));
                sb.append(" E").append(String.format("%03d", episodeNumber));
            }
        }
        return sb.toString();
    }

    public String getDisplayYear() {
        // see above notes for display title.
        StringBuilder sb = new StringBuilder();

        if (startYear != null && startYear > 1890) {
            sb.append(startYear);
            if (endYear != null && endYear > startYear) {
                sb.append("-").append(endYear);
            }
        }
        return sb.toString();
    }

    public String getTitleWithYear() {
        if (getDisplayYear() != null && !getDisplayYear().isBlank()) {
            return String.format("%s (%s)", primaryTitle, getDisplayYear());
        } else {
            return primaryTitle;
        }
    }

    public String getSearchData() {
        return Utils.buildExtraSearchTerms(this.getDisplayTitle());
    }

    public static List<String> getColumnHeadings() {
        return Arrays.asList(
                "ID", // edit links will be shown in this column, so no heading.
                "Content Type",
                "Title",
                "Year(s)",
                "Run-time (mins)",
                "hiddenColumn"
        );
    }

    public static List<String> getRowValues() {
        return Arrays.asList( // which bean field to use for each row cell in a table:
                "titleID",
                "contentType.contentTypeName",
                "displayTitle",
                "displayYear",
                "runtimeMinutes",
                "searchData"
        );
    }

    @Override
    public int compareTo(Title other) {
        return Utils.compareUsingCollator(this.getOriginalTitle(), other.getOriginalTitle());
    }

}
