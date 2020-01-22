package org.northcoder.titlewebdemo.controller;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.northcoder.titlewebdemo.Path._MainTemplates.*;
import static org.northcoder.titlewebdemo.Path._Title.*;
import org.northcoder.titlewebdemo.beans.Title;
import org.northcoder.titlewebdemo.beans.ContentType;
import org.northcoder.titlewebdemo.beans.TalentInTitle;
import org.northcoder.titlewebdemo.dao.JdbiDAO;
import org.northcoder.titlewebdemo.util.HttpStatus;

/**
 * See the Title bean.
 */
public class TitleController extends Controller {

    private final JdbiDAO jdbiDAO;
            
    public TitleController(JdbiDAO jdbiDAO) {
        super(jdbiDAO);
        this.jdbiDAO = jdbiDAO;
    }

    public final Handler fetchOne = (ctx) -> {
        Title bindParams = new Title(ctx.pathParam(DB_KEY));
        DaoData<Title> daoData = fetchOneRecord(bindParams, Title.SQL_SELECT_BY_ID);
        ctx.render(SITE_TEMPLATE, buildFormModel(ctx, daoData));
    };

    public final Handler fetchAll = (ctx) -> {
        Title bindParams = new Title();
        DaoData<Title> daoData = fetchAllRecords(bindParams, Title.SQL_SELECT_ALL);
        ctx.render(SITE_TEMPLATE, buildTableModel(ctx, daoData));
    };

    public final Handler updateOne = (ctx) -> {
        DaoData<Title> daoData = updateOneRecord(ctx.body(), Title.SQL_UPDATE_BY_ID,
                new Title());
        if (daoData.getResultBean().getActionCompletedOK()) {
            // doing a post-redirect-get, this is the redirect:
            ctx.redirect(String.format("%s?result=ok", ctx.path()), 
                HttpStatus._303.getStatusCode());
        } else {
            // return original data with errors:
            ctx.render(SITE_TEMPLATE, buildFormModel(ctx, daoData));
        }
    };

    private Map<String, Object> buildTableModel(Context ctx, DaoData<Title> daoData) {
        Map<String, Object> model = new HashMap<>();
        model.put("ctx", ctx);
        model.put("daoData", daoData);
        model.put("tableType", "title");
        model.put("resultBeans", daoData.getResultBeans());
        model.put("columnHeadings", Title.getColumnHeadings());
        model.put("rowValues", Title.getRowValues());
        model.put("tableTitle", "All Titles");
        model.put("contentTemplate", DATA_TABLE_TEMPLATE);
        return model;
    }

    private Map<String, Object> buildFormModel(Context ctx, DaoData<Title> daoData) {
        Map<String, Object> model = new HashMap<>();
        model.put("ctx", ctx);
        model.put("daoData", daoData);
        model.put("title", daoData.getResultBean());
        List<ContentType> contentTypes = new ContentTypeController(jdbiDAO).fetchAll();
        model.put("contentTypes", contentTypes);
        addContentType(daoData, contentTypes);
        model.put("formTitle", daoData.getResultBean().getTitleWithYear());
        model.put("formTemplate", FORM_TEMPLATE);
        model.put("contentTemplate", DATA_ENTRY_TEMPLATE);
        model.put("tabLabels", new String[]{"Title", "Talent", "Synopsis", "JSON"});
        // for the talent listing:
        addTalentForTitle(daoData);
        model.put("tableType", "talent");
        model.put("resultBeans", daoData.getResultBean().getTalentListForTitle());
        model.put("columnHeadings", TalentInTitle.getColumnHeadings());
        model.put("rowValues", TalentInTitle.getRowValues());
        model.put("tableTitle", "Cast and Crew");
        return model;
    }

    private void addTalentForTitle(DaoData<Title> daoData) {
        if (daoData.getResultBean() != null) {
            daoData.getResultBean().getTalentListForTitle()
                    .addAll(fetchTalentForTitle(daoData.getResultBean().getTitleID()));
        }
    }

    protected List<TalentInTitle> fetchTalentForTitle(String titleID) {
        DaoData<TalentInTitle> daoData = fetchAllRecords(new TalentInTitle(titleID),
                TalentInTitle.SQL_SELECT_ALL_BY_TITLE);
        return daoData.getResultBeans();
    }

    private void addContentType(DaoData<Title> daoData, List<ContentType> contentTypes) {
        // when handling validation errors, we still need a content type object
        // to be provided in the result bean - it's used to build the form's title!
        if (daoData.getResultBean() != null && daoData.getResultBean().getContentType() == null) {
            Integer contentTypeID = daoData.getResultBean().getContentTypeID();
            if (contentTypeID != null) {
                for (ContentType contentType : contentTypes) {
                    if (contentType.getContentTypeID().equals(contentTypeID)) {
                        daoData.getResultBean().setContentType(contentType);
                        break;
                    }
                }
            }
        }
    }
}
