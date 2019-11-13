package org.northcoder.titlewebdemo.beans;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.northcoder.titlewebdemo.util.Utils;
import org.northcoder.titlewebdemo.validation.FirstOrBoth;
import org.northcoder.titlewebdemo.validation.Mandatory;
import org.northcoder.titlewebdemo.validation.NotLessThanOther;
import org.northcoder.titlewebdemo.validation.ValidationHandler;

/**
 * IMDb actors, directors, producers, etc.
 */
@FirstOrBoth(firstField = "year of birth", secondField = "year of death", message = ValidationHandler.FIRST_OR_BOTH_MESSAGE)
@NotLessThanOther(firstField = "Year of Birth", secondField = "Year of Death", message = ValidationHandler.NOT_LESS_THAN_OTHER_MESSAGE)

public class Talent extends DemoBean implements Comparable<Talent> {

    public Talent() {
        super();
    }

    public Talent(String talentID) {
        super();
        this.talentID = talentID;
    }

    public static final String SQL_SELECT_ALL = String.join(" ",
            "select top 10000",
            "  talent_id,",
            "  talent_name,",
            "  birth_year,",
            "  death_year",
            "from imdb.talent");

    public static final String SQL_SELECT_BY_ID = String.join(" ",
            SQL_SELECT_ALL, "where talent_id = :talentID");
    
    public static final String SQL_UPDATE_BY_ID = String.join(" ",
            "update imdb.talent set talent_name = :talentName,",
            "birth_year = :birthYear,",
            "death_year = :deathYear",
            "where talent_id = :talentID");

    @Mandatory(fieldName = "talent ID", message = ValidationHandler.MANDATORY)
    @SerializedName("talent_id")
    @ColumnName("talent_id")
    private String talentID;

    @Mandatory(fieldName = "talent name", message = ValidationHandler.MANDATORY)
    @Size(min = 1, max = 100, message = ValidationHandler.MIN_MAX_LENGTH)
    @SerializedName("talent_name")
    @ColumnName("talent_name")
    private String talentName;

    @Nullable
    @Digits(integer = 4, fraction = 0, message = "{org.northcoder.titlewebdemo.validation.Integer.message}")
    @Range(min = 1800, max = 2100, message = "{org.northcoder.titlewebdemo.validation.Year.message}")
    @SerializedName("birth_year")
    @ColumnName("birth_year")
    private Integer birthYear;

    @Nullable
    @Digits(integer = 4, fraction = 0, message = "{org.northcoder.titlewebdemo.validation.Integer.message}")
    @Range(min = 1800, max = 2100, message = "{org.northcoder.titlewebdemo.validation.Year.message}")
    @SerializedName("death_year")
    @ColumnName("death_year")
    private Integer deathYear;

    @SerializedName("titles")
    private final List<TitleForTalent> titleListForTalent = new ArrayList();
    
    public String getTalentID() {
        return talentID;
    }

    public void setTalentID(String talentID) {
        this.talentID = talentID;
    }

    public String getTalentName() {
        return talentName;
    }

    public void setTalentName(String talentName) {
        this.talentName = talentName;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public List<TitleForTalent> getTitleListForTalent() {
        return titleListForTalent;
    }
    
    public String getSearchData() {
        return Utils.buildExtraSearchTerms(String.join(" ",
                this.getTalentName()));
    }
    
    public String getTalentWithYear() {
        StringBuilder sb = new StringBuilder();
        sb.append(talentName);
        if (birthYear != null) {
            sb.append(" (").append(birthYear).append(" - ");
            if (deathYear != null) {
                sb.append(deathYear);
            }
            sb.append(")");
        }
        return sb.toString();
    }

    public static List<String> getColumnHeadings() {
        return Arrays.asList(
                "Talent ID", // edit links will be shown in this column, so no heading.
                "Talent Name",
                "Year of Birth",
                "Year of Death",
                "hiddenColumn"
        );
    }

    public static List<String> getRowValues() {
        return Arrays.asList( // which bean field to use for each row cell in a table:
                "talentID",
                "talentName",
                "birthYear",
                "deathYear",
                "searchData"
        );
    }

    @Override
    public int compareTo(Talent other) {
        return Utils.compareUsingCollator(this.getTalentName(), other.getTalentName());
    }
}
