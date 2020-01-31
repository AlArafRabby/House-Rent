package com.house.rent;

public class Blog {
    private String address, details, image, phone, area, thana, district;

    public Blog(){

    }

    public Blog(String address, String details, String image, String phone, String area, String thana, String district) {
        this.address = address;
        this.details = details;
        this.image = image;
        this.phone = phone;
        this.area= area;
        this.thana= thana;
        this.district= district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getThana() {
        return thana;
    }

    public void setThana(String thana) {
        this.thana = thana;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
