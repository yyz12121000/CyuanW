package com.yyz.cyuanw.view.sortrecyclerview;

public class SortModel {
private int type;
    private int id;
    private String name;
    private String logo;
    private String letters;//显示拼音的首字母

   private String power, emission_standard,gearbox,car_style;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }


    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getEmission_standard() {
        return emission_standard;
    }

    public void setEmission_standard(String emission_standard) {
        this.emission_standard = emission_standard;
    }

    public String getGearbox() {
        return gearbox;
    }

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }

    public String getCar_style() {
        return car_style;
    }

    public void setCar_style(String car_style) {
        this.car_style = car_style;
    }
}
