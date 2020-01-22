package org.northcoder.titlewebdemo.controller;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;
import org.northcoder.titlewebdemo.beans.ContentType;
import org.northcoder.titlewebdemo.dao.JdbiDAO;
import org.northcoder.titlewebdemo.dao.JdbiDSTest;
import java.util.List;

public class ContentTypeControllerTest {
    
    private final JdbiDAO jdbiDAO;
    
    public ContentTypeControllerTest() {
        this.jdbiDAO = new JdbiDAO(JdbiDSTest.INST.getDS());
    }

    @Test
    public void testFetchAll() {
        List<ContentType> contentTypes = new ContentTypeController(jdbiDAO).fetchAll();
        assertThat(contentTypes.size()).isEqualTo(10);
        ContentType expectedType = new ContentType(2); // 2 = movie
        assertThat(contentTypes.contains(expectedType)).isTrue();        
        contentTypes.stream().filter((ct) -> (ct.equals(expectedType))).forEachOrdered((ct) -> {
            assertThat(ct.getContentTypeName().toLowerCase()).isEqualTo("movie");
        });
    }
    
}
