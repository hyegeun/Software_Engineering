package com.example.sogong_project;

public class GroupInfo {
    private String user;
    private String group;
    private String purpose;

    public GroupInfo() {
    }

    public GroupInfo(String user, String group, String purpose){
        this.user = user;
        this.group = group;
        this.purpose = purpose;
    }

    public String getUser(){
        return this.user;
    }

    public void setUser(String user){
        this.user = user;
    }

    public String getGroup(){
        return this.group;
    }

    public void setGroup(String group){
        this.group = group;
    }

    public String getPurpose(){
        return this.purpose;
    }

    public void setPurpose(String purpose){
        this.purpose = purpose;
    }


}
