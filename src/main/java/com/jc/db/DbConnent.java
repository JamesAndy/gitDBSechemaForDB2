package com.jc.db;

import lombok.Getter;
import lombok.Setter;

public class DbConnent {
    // JDBC driver name and database URL
    @Getter @Setter
    private String jdbcDriver;
    @Getter @Setter
    private String dbUrl;
    //  Database credentials
    @Getter @Setter
    private String user;
    @Getter @Setter
    private String passWord ;

}
