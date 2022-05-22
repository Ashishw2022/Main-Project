package com.example.afinal;

public class doctor_model {
    private String Name;
    private String uemail;
    private doctor_model()
    {

    }
    public doctor_model(String name,String uemail)
    {
        this.Name = name;
        this.uemail = uemail;
    }


    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }
}
