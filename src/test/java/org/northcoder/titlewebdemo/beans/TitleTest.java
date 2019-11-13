package org.northcoder.titlewebdemo.beans;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;
import org.jdbi.v3.core.Jdbi;
import org.northcoder.titlewebdemo.controller.DaoData;
import org.northcoder.titlewebdemo.validation.ValidationError;
import org.northcoder.titlewebdemo.dao.JdbiDAO;
import org.northcoder.titlewebdemo.dao.JdbiDSTest;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 *
 */
public class TitleTest {

    private static final String TITLE_ID = "tt0644480";

    private Title getTitle() {
        // get a Title bean from the database, for testing:
        Jdbi jdbi = JdbiDSTest.INST.getDS();
        DaoData<Title> daoData = new DaoData(new Title(TITLE_ID), Title.SQL_SELECT_BY_ID);
        daoData = new JdbiDAO(jdbi).selectRecord(daoData);
        return daoData.getResultBean();
    }

    @Test
    public void testGetTitleID() {
        Title title = getTitle();
        assertThat(title.getTitleID()).isEqualTo(TITLE_ID);
    }

    @Test
    public void testValidateFormZeroErrors() {
        Title title = getTitle();
        title.validateFormData();
        assertThat(title.getFormValidationErrors().isEmpty()).isTrue();
    }

    @Test
    public void testValidateFormOneError() {
        Title title = getTitle();
        title.setPrimaryTitle(null);
        title.validateFormData();
        assertThat(title.getFormValidationErrors().size()).isEqualTo(1);
        assertThat(title.getFormValidationErrors().get(0).getMessage())
                .isEqualTo("Please provide a primary title.");
    }

    @Test
    public void testValidateFormTwoErrors() {
        Title title = getTitle();
        title.setPrimaryTitle(null);
        title.setStartYear(-1955);
        title.validateFormData();

        assertThat(title.getFormValidationErrors().size()).isEqualTo(2);

        for (ValidationError validationError : title.getFormValidationErrors()) {
            if (validationError.getField().equals("primaryTitle")) {
                assertThat(validationError.getMessage())
                        .isEqualTo("Please provide a primary title.");
            } else if (validationError.getField().equals("startYear")) {
                assertThat(validationError.getMessage())
                        .isEqualTo("Value must be a year between 1890 and 2030.");
            }
        }
    }

    @Test
    public void testValidateFormTwoErrorsAsJson() {
        Title title = getTitle();
        title.setPrimaryTitle(null);
        title.setStartYear(-1955);
        title.validateFormData();
        String json = title.getFormValidationErrorsAsJson();
        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(json);
        assertThat(jsonTree.isJsonArray()).isTrue();
        if (jsonTree.isJsonArray()) {
            JsonArray jsonArray = jsonTree.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                if (obj.get("field").getAsString().equals("primaryTitle")) {
                    assertThat(obj.get("message").getAsString())
                        .isEqualTo("Please provide a primary title.");
                } else {
                    assertThat(obj.get("message").getAsString())
                        .isEqualTo("Value must be a year between 1890 and 2030.");
                }
            }
        }
    }

}
