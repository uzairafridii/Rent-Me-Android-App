package com.uzair.rentme.Models;

public class OwnersData {
    private String ownerName , flatName, flatRent , flatImage , area , category,
             ownerPhone,availableSeats , flatNumber , flatQuantity , type;

    public OwnersData(String ownerName, String flatName, String flatRent,
                      String flatImage, String area, String category,
                      String ownerPhone, String availableSeats, String flatNumber,
                      String flatQuantity, String type) {

        this.ownerName = ownerName;
        this.flatName = flatName;
        this.flatRent = flatRent;
        this.flatImage = flatImage;
        this.area = area;
        this.category = category;
        this.ownerPhone = ownerPhone;
        this.availableSeats = availableSeats;
        this.flatNumber = flatNumber;
        this.flatQuantity = flatQuantity;
        this.type = type;
    }

    public OwnersData() {
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getFlatName() {
        return flatName;
    }

    public void setFlatName(String flatName) {
        this.flatName = flatName;
    }

    public String getFlatRent() {
        return flatRent;
    }

    public void setFlatRent(String flatRent) {
        this.flatRent = flatRent;
    }

    public String getFlatImage() {
        return flatImage;
    }

    public void setFlatImage(String flatImage) {
        this.flatImage = flatImage;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(String availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getFlatQuantity() {
        return flatQuantity;
    }

    public void setFlatQuantity(String flatQuantity) {
        this.flatQuantity = flatQuantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
