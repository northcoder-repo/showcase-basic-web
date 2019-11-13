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

/**
 * See the Title bean.
 */
public class TitleController extends Controller {

    public TitleController() {
        super();
    }

    public static Handler fetchOne = (ctx) -> {
        Title bindParams = new Title(ctx.pathParam(DB_KEY));
        DaoData<Title> daoData = fetchOneRecord(bindParams, Title.SQL_SELECT_BY_ID);
        ctx.render(SITE_TEMPLATE, buildFormModel(ctx, daoData));
    };

    public static Handler fetchAll = (ctx) -> {
        Title bindParams = new Title();
        DaoData<Title> daoData = fetchAllRecords(bindParams, Title.SQL_SELECT_ALL);
        ctx.render(SITE_TEMPLATE, buildTableModel(ctx, daoData));
    };

    public static Handler updateOne = (ctx) -> {
        DaoData<Title> daoData = updateOneRecord(ctx.body(), Title.SQL_UPDATE_BY_ID,
                new Title());
        if (daoData.getResultBean().getActionCompletedOK()) {
            // Safeguard: re-select the record, in case of additional DB-triggered updates:
            Title selectBindParams = new Title(daoData.getResultBean().getTitleID());
            daoData = fetchOneRecord(selectBindParams, Title.SQL_SELECT_BY_ID);
            daoData.getResultBean().setActionCompletedOK(true);
        }
        ctx.render(SITE_TEMPLATE, buildFormModel(ctx, daoData));
    };

    private static Map<String, Object> buildTableModel(Context ctx, DaoData<Title> daoData) {
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

    private static Map<String, Object> buildFormModel(Context ctx, DaoData<Title> daoData) {
        Map<String, Object> model = new HashMap<>();
        model.put("ctx", ctx);
        model.put("daoData", daoData);
        model.put("title", daoData.getResultBean());
        List<ContentType> contentTypes = ContentTypeController.fetchAll();
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

    private static void addTalentForTitle(DaoData<Title> daoData) {
        if (daoData.getResultBean() != null) {
            daoData.getResultBean().getTalentListForTitle()
                    .addAll(fetchTalentForTitle(daoData.getResultBean().getTitleID()));
        }
    }

    protected static List<TalentInTitle> fetchTalentForTitle(String titleID) {
        DaoData<TalentInTitle> daoData = fetchAllRecords(new TalentInTitle(titleID),
                TalentInTitle.SQL_SELECT_ALL_BY_TITLE);
        return daoData.getResultBeans();
    }

    private static void addContentType(DaoData<Title> daoData, List<ContentType> contentTypes) {
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
