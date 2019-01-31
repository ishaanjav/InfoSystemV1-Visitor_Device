package com.example.anany.informationsystemv1;

public class WorkerLogOut {
    private String time;
    private String name;
    private String phone;
    private String email;
    private String username;
    private String relation;

    public WorkerLogOut(String time, String name, String phone, String email, String username, String relation) {
        this.time = time;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.relation = relation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
