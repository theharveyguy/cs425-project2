package edu.jsu.mcis.cs425.project2;

import java.util.HashMap;
import javax.naming.NamingException;

public class BeanApplicant {
    
    private String username;
    private String displayname;
    private String[] skills;
    private int id;
    
    public void setUserInfo() throws NamingException {
        Database db = new Database();
        HashMap<String, String> userinfo = db.getUserInfo(username);
        id = Integer.parseInt(userinfo.get("userid"));
        displayname = userinfo.get("displayname");
    }

    public String getDisplayname() {
        return displayname;
    }
    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    public void setSkill(String[] skills) {
        this.skills = skills;
    }
    
    public String getJobsList(){
        Database db = new Database();
        return(db.getJobsListAsHTML(id));
    }
    
    public void setSkillsList(){
        Database db = new Database();
        db.setSkillsList();
    }
}