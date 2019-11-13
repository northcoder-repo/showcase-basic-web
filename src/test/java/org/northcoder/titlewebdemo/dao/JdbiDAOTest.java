package org.northcoder.titlewebdemo.dao;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;
import org.northcoder.titlewebdemo.beans.Title;
import org.northcoder.titlewebdemo.controller.DaoData;
import org.jdbi.v3.core.Jdbi;
import java.util.List;

/**
 *
 */
public class JdbiDAOTest {

    private final Jdbi jdbi;

    public JdbiDAOTest() {
        this.jdbi = JdbiDSTest.INST.getDS();
    }

    @Test
    public void testSelectBeans() {
        DaoData<Title> daoData = new DaoData(new Title(), Title.SQL_SELECT_ALL);
        daoData = new JdbiDAO(jdbi).selectRecords(daoData);
        List<Title> titles = daoData.getResultBeans();
        assertThat(daoData.getDbErrorMessage()).isNull();
        assertThat(titles.size()).isEqualTo(29);
    }

    @Test
    public void testSelectBeans_DatabaseError() {
        String badSQL = "select foo from imdb.title";
        DaoData<Title> daoData = new DaoData(new Title(), badSQL);
        daoData = new JdbiDAO(jdbi).selectRecords(daoData);
        Title title = daoData.getResultBean();
        assertThat(daoData.getDbErrorMessage()).isEqualTo(JdbiDAO.DATABASE_ERROR);
    }

    @Test
    public void testSelectBean() {
        DaoData<Title> daoData = new DaoData(new Title("tt0644480"), Title.SQL_SELECT_BY_ID);
        daoData = new JdbiDAO(jdbi).selectRecord(daoData);
        Title title = daoData.getResultBean();
        assertThat(daoData.getDbErrorMessage()).isNull();
        assertThat(title.getFormValidationErrors()).isEmpty();
        assertThat(title.getTitleID()).isEqualTo("tt0644480");
        assertThat(title.getOriginalTitle()).isEqualTo("Rope of Cards");
    }

    @Test
    public void testSelectBean_RecordNotFound() {
        DaoData<Title> daoData = new DaoData(new Title("zz999999"), Title.SQL_SELECT_BY_ID);
        daoData = new JdbiDAO(jdbi).selectRecord(daoData);
        Title title = daoData.getResultBean();
        assertThat(daoData.getDbErrorMessage()).isEqualTo(JdbiDAO.RECORD_NOT_FOUND);
    }

    @Test
    public void testSelectBean_DatabaseError() {
        String badSQL = "select foo from imdb.title where title_id = ?";
        DaoData<Title> daoData = new DaoData(new Title("tt0644480"), badSQL);
        daoData = new JdbiDAO(jdbi).selectRecord(daoData);
        Title title = daoData.getResultBean();
        assertThat(daoData.getDbErrorMessage()).isEqualTo(JdbiDAO.DATABASE_ERROR);
    }

    @Test
    public void testUpdateBean() {
        String titleID = "tt3814818";
        String oldPrimaryTitle = "Harvard Homicide";
        String newPrimaryTitle = "Princeton Incident";

        DaoData<Title> daoData = new DaoData(new Title(titleID), Title.SQL_SELECT_BY_ID);
        daoData = new JdbiDAO(jdbi).selectRecord(daoData);
        Title title = daoData.getResultBean();
        assertThat(title.getPrimaryTitle()).isEqualTo(oldPrimaryTitle);

        // update the title in the bean:
        title.setPrimaryTitle(newPrimaryTitle);
        title.setStartYear(title.getStartYear() + 1);

        // set the new bind params, then update the record in the db:
        daoData = new DaoData(title, Title.SQL_UPDATE_BY_ID);
        daoData = new JdbiDAO(jdbi).updateRecord(daoData);
        // was 1 record updated?
        assertThat(daoData.getAffectedRowCount()).isEqualTo(1);

        // retrieve the updated record from the db:
        daoData = new DaoData(new Title(titleID), Title.SQL_SELECT_BY_ID);
        daoData = new JdbiDAO(jdbi).selectRecord(daoData);
        title = daoData.getResultBean();
        assertThat(title.getTitleID()).isEqualTo(titleID);
        assertThat(title.getPrimaryTitle()).isEqualTo(newPrimaryTitle);
    }
    
    @Test
    public void testUpdateBean_NotUpdated() {
        String titleID = "tt0050037";
        String oldPrimaryTitle = "Maverick";
        String newPrimaryTitle = "Conformist";

        DaoData<Title> daoData = new DaoData(new Title(titleID), Title.SQL_SELECT_BY_ID);
        daoData = new JdbiDAO(jdbi).selectRecord(daoData);
        Title title = daoData.getResultBean();
        assertThat(title.getPrimaryTitle()).isEqualTo(oldPrimaryTitle);

        // update the title in the bean:
        title.setTitleID("zz9999"); // non-existent ID
        title.setPrimaryTitle(newPrimaryTitle);
        title.setStartYear(title.getStartYear() + 1);

        // set the new bind params, then update the record in the db:
        daoData = new DaoData(title, Title.SQL_UPDATE_BY_ID);
        daoData = new JdbiDAO(jdbi).updateRecord(daoData);
        assertThat(daoData.getAffectedRowCount()).isEqualTo(0);
        assertThat(daoData.getDbErrorMessage()).isEqualTo(JdbiDAO.RECORD_NOT_UPDATED);
    }

}
