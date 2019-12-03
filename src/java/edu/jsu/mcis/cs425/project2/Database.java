package edu.jsu.mcis.cs425.project2;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Database {
    Context envContext = null, initContext = null;
    DataSource ds = null;
    Connection conn = null;

    public Database() throws NamingException {
        try {
            envContext = new InitialContext();
            initContext  = (Context)envContext.lookup("java:/comp/env");
            ds = (DataSource)initContext.lookup("jdbc/db_pool");
            conn = ds.getConnection();   
        }
        catch (SQLException e) {}
    } // Constructor

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            }
            catch (SQLException e) {}   
        }
    } // End closeConnection()

    public Connection getConnection() { return conn; }
    
    public HashMap getUserInfo(String username){
        
        //db pool variables
        Database db = null;
        Connection connection;
        //SQL variables
        String query;
        PreparedStatement pstatement = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        boolean hasResult;
        
        HashMap <String,String> results = null;
        
        try{
            connection = getConnection();
            
            query = "SELECT * FROM 'user' WHERE username = ?";
            pstatement = connection.prepareStatement(query);
            pstatement.setString(1,username);
            
            hasResult = pstatement.execute();
            
            if (hasResult) {
                resultset = pstatement.getResultSet();
                
                if (resultset.next()) {
                    results.put("id",resultset.getString("id").trim());
                    results.put("displayname", resultset.getString("displayname"));
                }
            }
        }
        catch (Exception e) { System.err.println( e.toString() ); }
        
        return results;
    }
    
    public String getSkillsListAsHTML (int userid){
        StringBuilder s = new StringBuilder();
        
        // TODO
        // query database and get info back as resultset
        
        while (resultset.next()){
            String description = resultset.getString("description");
            int id = resultset.getInt("id");
            
            
            s.append("<input type=\"checkbox\" name=\"skills\" value=\"");
            s.append(id);
            s.append("\" id=\"skills_").append(id).append("\"");
            if (user != 0){
                s.append("checked");
            }
            s.append("><br/>");
            
            s.append("<label for=\"skills_").append(id).append("\">");
            s.append(description);
            s.append("</label> <br/>");
            
        }
        return s.toString();
    }
    
    public String getJobsListAsHTML(int userid){
        StringBuilder s = new StringBuilder();
        //TODO
        
        return s.toString();
    }
    
    public void setSkillsList(){
        // TODO
        // delete all where userid = userid
        // create new record(s) for updated user skills
    }
}
