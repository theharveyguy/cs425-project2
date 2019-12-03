package edu.jsu.mcis.cs425.project2;

import java.util.HashMap;
import javax.naming.NamingException;

public class BeanApplicant {
    
    private String username;
    private String displayname;
    private String[] skills;
    private int userid;
    
    public void setUserInfo() throws NamingException {
        Database db = new Database();
        HashMap<String, String> userinfo = db.getUserInfo(username);
        userid = Integer.parseInt(userinfo.get("userid"));
        displayname = userinfo.get("displayname");
    }

    public String getDisplayname() {
        return displayname;
    }
    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }
    
    public int getUserid() {
        return userid;
    }
    public void setUserid(int id) {
        this.userid = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String[] getSkills() {
        return skills;
    }
    public void setSkills(String[] skills) {
        this.skills = skills;
    }
    
    public String getJobs() throws NamingException{
        Database db = new Database();
        return(db.getJobsListAsHTML(userid));
    }
    
    public void setJobsList() throws NamingException{
        Database db = new Database();
        // set jobs in database
    }
    
    public void setSkillsList() throws NamingException{
        Database db = new Database();
        db.setSkillsList();
    }
}