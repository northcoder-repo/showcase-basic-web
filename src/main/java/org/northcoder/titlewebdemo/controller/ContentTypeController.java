package org.northcoder.titlewebdemo.controller;

import org.northcoder.titlewebdemo.beans.ContentType;
import org.northcoder.titlewebdemo.dao.JdbiDAO;
import java.util.List;

/**
 * See the Content Type bean.
 */
public class ContentTypeController extends Controller {

    public ContentTypeController(JdbiDAO jdbiDAO) {
        super(jdbiDAO);
    }

    public List<ContentType> fetchAll() {
        return fetchAllRecords(new ContentType(), ContentType.SQL_SELECT_ALL)
                .getResultBeans();
    }

}
