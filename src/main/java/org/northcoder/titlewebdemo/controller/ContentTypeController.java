package org.northcoder.titlewebdemo.controller;

import org.northcoder.titlewebdemo.beans.ContentType;
import java.util.List;

/**
 * See the Content Type bean.
 */
public class ContentTypeController extends Controller {

    public ContentTypeController() {
        super();
    }

    public static List<ContentType> fetchAll() {
        DaoData<ContentType> daoData = fetchAllRecords(new ContentType(), 
                ContentType.SQL_SELECT_ALL);
        return daoData.getResultBeans();
    }

}
