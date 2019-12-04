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
                    while (resultset.next()) {
                        results.put("id",resultset.getString("id").trim());
                        results.put("displayname", resultset.getString("displayname"));
                    }
                }
            }
        }
        catch (Exception e) { System.err.println( e.toString() ); }
        
        closeConnection();
        
        return results;
    }
    
    public String getSkillsListAsHTML (int userid){
        StringBuilder s = new StringBuilder();
        
        //db pool variables
        Database db = null;
        Connection connection;
        //SQL variables
        String query;
        PreparedStatement pstatement = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        boolean hasResult;
        
        try{
            connection = getConnection();
            
            query = "SELECT skills.*, a.? \n" +
                    "FROM skills LEFT JOIN (SELECT * FROM applicants_to_skills WHERE userid = 1) AS a \n" +
                    "ON skills.id = a.skillsid";
            pstatement = connection.prepareStatement(query);
            pstatement.setString(1, Integer.toString(userid));
            
            hasResult = pstatement.execute();
            
            if (hasResult) {
                resultset = pstatement.getResultSet();
                if (resultset.next()){
                    while (resultset.next()){
                        String description = resultset.getString("description");
                        int id = resultset.getInt("id");
                        
                        s.append("<input type=\"checkbox\" name=\"skills\" value=\"");
                        s.append(id);
                        s.append("\" id=\"skills_").append(id).append("\"");
                        if (resultset.getInt("user") != 0){
                            s.append("checked");
                        }
                        s.append("><br/>");

                        s.append("<label for=\"skills_").append(id).append("\">");
                        s.append(description);
                        s.append("</label> <br/>");
                    }
                }
            }
        }
        catch (Exception e) { System.err.println( e.toString() ); }
        
        closeConnection();
        
        return s.toString();
    }
    
    public String getJobsListAsHTML(int userid){
        StringBuilder s = new StringBuilder();
        //TODO
        
        //db pool variables
        Database db = null;
        Connection connection;
        //SQL variables
        String query;
        PreparedStatement pstatement = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        boolean hasResult;
        
        try{
            connection = getConnection();
        
            query = "SELECT jobs.id, jobs.name, a.userid FROM\n" +
                    "jobs LEFT JOIN (SELECT * FROM applicants_to_jobs WHERE userid = 1) as a\n" +
                    "ON jobs.id IN\n" +
                    "(SELECT jobsid AS id FROM\n" +
                    "(applicants_to_skills JOIN skills_to_jobs\n" +
                    "ON applicants_to_skills.skillsid = skills_to_jobs.skillsid)\n" +
                    "WHERE applicants_to_skills.userid = 1)\n" +
                    "ORDER BY jobs.name";
            pstatement = connection.prepareStatement(query);
            
            hasResult = pstatement.execute();
            
            if (hasResult){
                resultset = pstatement.getResultSet();
                if (resultset.next()){
                    while (resultset.next()){
                        String description = resultset.getString("jobs.name");
                        int id = resultset.getInt("jobs.id");
                        
                        s.append("<input type=\"checkbox\" name=\"jobs\" value=\"");
                        s.append(id);
                        s.append("\" id=\"job_").append(id).append("\"");
                        if (resultset.getInt("user") != 0){
                            s.append("checked");
                        }
                        s.append("><br/>");

                        s.append("<label for=\"job_").append(id).append("\">");
                        s.append(description);
                        s.append("</label> <br/>");
                    }
                }
            }
            
        }
        catch (Exception e) { System.err.println( e.toString() ); }
        
        closeConnection();
        
        return s.toString();
    }
    
    public void setSkillsList(int userid, String[] skills){
        
        //db pool variables
        Database db = null;
        Connection connection;
        //SQL variables
        String query;
        PreparedStatement pstatement = null;
                
        try{
            connection = getConnection();
            
            // delete all where userid = userid
            query = "DELETE * FROM applicants_to_skills WHERE userid = ?";
            pstatement = connection.prepareStatement(query);
            pstatement.setString(1, Integer.toString(userid));
            
            pstatement.execute();
            
            // then create new records for updated user skills
            for (String skill : skills){
                query = "INSERT INTO applicants_to_skills (userid,skillsid) VALUES (?,?)";
                pstatement = connection.prepareStatement(query);
                pstatement.setString(1, Integer.toString(userid));
                pstatement.setString(2, skill);
                
                pstatement.execute();
            }// Iterates through skills array, making new record for each skill
            
        }
        catch (Exception e) { System.err.println( e.toString() ); }
        
        closeConnection();
    }
}
