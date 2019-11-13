package org.northcoder.titlewebdemo.controller;

import java.util.List;
import static org.northcoder.titlewebdemo.controller.Controller.fetchAllRecords;
import org.northcoder.titlewebdemo.beans.TalentInTitle;

/**
 * See the TalentInTitle bean.
 */
public class TalentInTitleController extends Controller {

    public TalentInTitleController() {
        super();
    }

    public static List<TalentInTitle> fetchAllForOneTitle(String titleID) {
        DaoData<TalentInTitle> daoData = fetchAllRecords(new TalentInTitle(titleID),
                TalentInTitle.SQL_SELECT_ALL_BY_TITLE);
        return daoData.getResultBeans();
    }

}
