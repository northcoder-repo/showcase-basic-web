package org.northcoder.titlewebdemo.controller;

import java.util.List;
import org.northcoder.titlewebdemo.beans.TalentInTitle;
import org.northcoder.titlewebdemo.dao.JdbiDAO;

/**
 * See the TalentInTitle bean.
 */
public class TalentInTitleController extends Controller {

    public TalentInTitleController(JdbiDAO jdbiDAO) {
        super(jdbiDAO);
    }

    public List<TalentInTitle> fetchAllForOneTitle(String titleID) {
        DaoData<TalentInTitle> daoData = fetchAllRecords(new TalentInTitle(titleID),
                TalentInTitle.SQL_SELECT_ALL_BY_TITLE);
        return daoData.getResultBeans();
    }

}
