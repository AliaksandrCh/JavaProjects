package com.javaee.db;

import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

public class DbConnection {

    private static Connection connect;
    private static Context context;
    private static DataSource  ds;

    public static Connection getConnection(){
        try {
            context = new InitialContext();
            ds = (DataSource) context.lookup("jdbc/library");
            connect = ds.getConnection();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NamingException e) {
            e.printStackTrace();
        }
        return connect;
    }

}
