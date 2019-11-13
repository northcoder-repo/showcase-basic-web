package org.northcoder.titlewebdemo;

/**
 *
 */
public class Path {

    public static final String TITLES = "/titles";
    public static final String TALENTS = "/talents";

    public static final String ROUTES_OVERVIEW = "/routes";
    public static final String STATIC_FILES = "/public";

    public static class _MainTemplates {

        public static final String SITE_TEMPLATE = "site_template.html";
        public static final String DATA_TABLE_TEMPLATE = "data_table_template.html";
        public static final String DATA_ENTRY_TEMPLATE = "data_entry_template.html";
    }

    public static class _Title {

        public static final String MODEL_KEY = "title";
        public static final String DB_KEY = String.join("", MODEL_KEY, "_id");
        public static final String ROUTE = String.join("", "/", MODEL_KEY, "/:", DB_KEY);
        public static final String FORM_TEMPLATE = String.join("", "record/", MODEL_KEY, ".html");
    }

    public static class _Talent {

        public static final String MODEL_KEY = "talent";
        public static final String DB_KEY = String.join("", MODEL_KEY, "_id");
        public static final String ROUTE = String.join("", "/", MODEL_KEY, "/:", DB_KEY);
        public static final String FORM_TEMPLATE = String.join("", "record/", MODEL_KEY, ".html");
    }

    public static class _Country {

        public static final String MODEL_KEY = "country";
        public static final String DB_KEY = String.join("", MODEL_KEY, "_id");
        public static final String ROUTE = String.join("", "/", MODEL_KEY, "/:", DB_KEY);
        public static final String FORM_TEMPLATE = String.join("", "record/", MODEL_KEY, ".html");
    }

}
