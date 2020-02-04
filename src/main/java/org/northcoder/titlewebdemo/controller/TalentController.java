package org.northcoder.titlewebdemo.controller;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import static org.northcoder.titlewebdemo.Path._MainTemplates.*;
import static org.northcoder.titlewebdemo.Path._Talent.*;
import org.northcoder.titlewebdemo.beans.Talent;
import org.northcoder.titlewebdemo.beans.TitleForTalent;
import org.northcoder.titlewebdemo.dao.JdbiDAO;
import org.northcoder.titlewebdemo.util.HttpStatus;

/**
 * See the Talent bean.
 */
public class TalentController extends Controller {

    public TalentController(JdbiDAO jdbiDAO) {
        super(jdbiDAO);
    }

    public final Handler fetchOne = (ctx) -> {
        Talent bindParams = new Talent(ctx.pathParam(DB_KEY));
        DaoData<Talent> daoData = fetchOneRecord(bindParams, Talent.SQL_SELECT_BY_ID);
        ctx.render(SITE_TEMPLATE, buildFormModel(ctx, daoData));
    };

    public final Handler fetchAll = (ctx) -> {
        Talent bindParams = new Talent();
        DaoData<Talent> daoData = fetchAllRecords(bindParams, Talent.SQL_SELECT_ALL);
        ctx.render(SITE_TEMPLATE, buildTableModel(ctx, daoData));
    };

    public final Handler updateOne = (ctx) -> {
        DaoData<Talent> daoData = updateOneRecord(ctx.body(), Talent.SQL_UPDATE_BY_ID,
                new Talent());
        if (daoData.getResultBean().getActionCompletedOK()) {
            // doing a post-redirect-get, this is the redirect:
            ctx.redirect(String.format("%s?result=ok", ctx.path()), 
                HttpStatus._303.getStatusCode());
        } else {
            // return original data with errors:
            ctx.render(SITE_TEMPLATE, buildFormModel(ctx, daoData));
        }
    };

    private Map<String, Object> buildTableModel(Context ctx, DaoData<Talent> daoData) {
        Map<String, Object> model = new HashMap<>();
        model.put("ctx", ctx);
        model.put("daoData", daoData);
        model.put("tableType", "talent");
        model.put("resultBeans", daoData.getResultBeans());
        model.put("columnHeadings", Talent.getColumnHeadings());
        model.put("rowValues", Talent.getRowValues());
        model.put("tableTitle", "All Talent");
        model.put("contentTemplate", DATA_TABLE_TEMPLATE);
        return model;
    }
    
    private Map<String, Object> buildFormModel(Context ctx, DaoData<Talent> daoData) {
        Map<String, Object> model = new HashMap<>();
        model.put("ctx", ctx);
        model.put("daoData", daoData);
        model.put("talent", daoData.getResultBean());
        model.put("formTitle", daoData.getResultBean().getTalentWithYear());
        model.put("formTemplate", FORM_TEMPLATE);
        model.put("contentTemplate", DATA_ENTRY_TEMPLATE);
        model.put("tabLabels", new String[]{"Talent", "Titles", "Synopsis", "JSON"});
        // for the title listing:
        addTitlesForTalent(daoData);
        model.put("tableType", "title");
        model.put("resultBeans", daoData.getResultBean().getTitleListForTalent());
        model.put("columnHeadings", TitleForTalent.getColumnHeadings());
        model.put("rowValues", TitleForTalent.getRowValues());
        model.put("tableTitle", "Titles");
        return model;
    }
    
    private void addTitlesForTalent(DaoData<Talent> daoData) {
        if (daoData.getResultBean() != null) {
            daoData.getResultBean().getTitleListForTalent()
                    .addAll(fetchTitleForTalent(daoData.getResultBean().getTalentID()));
        }
    }

    protected List<TitleForTalent> fetchTitleForTalent(String talentID) {
        DaoData<TitleForTalent> daoData = fetchAllRecords(new TitleForTalent(talentID), 
                TitleForTalent.SQL_SELECT_ALL_BY_TALENT);
        return daoData.getResultBeans();
    }

}
