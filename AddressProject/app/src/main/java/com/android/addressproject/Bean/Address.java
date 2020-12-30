package com.android.addressproject.Bean;

// 20.12.29 지은 추가
public class Address {
    //Field (필드) = Bean 에선 이렇게 한줄 씩 쓰는 것이 좋다.
    int addressNo;
    String user_userId;
    String addressName;
    String addressPhone;
    String addressGroup;
    String addressEmail;
    String addressText;
    String addressBirth;
    String addressImage;
    String addressStar;

    // Constructor (생성자)

    public Address(int addressNo, String user_userId, String addressName, String addressPhone, String addressGroup, String addressEmail, String addressText, String addressBirth, String addressImage, String addressStar) {
        this.addressNo = addressNo;
        this.user_userId = user_userId;
        this.addressName = addressName;
        this.addressPhone = addressPhone;
        this.addressGroup = addressGroup;
        this.addressEmail = addressEmail;
        this.addressText = addressText;
        this.addressBirth = addressBirth;
        this.addressImage = addressImage;
        this.addressStar = addressStar;
    }

    public Address(String addressName, String addressPhone, String addressGroup, String addressEmail, String addressText, String addressBirth, String addressImage, String addressStar) {
        this.addressName = addressName;
        this.addressPhone = addressPhone;
        this.addressGroup = addressGroup;
        this.addressEmail = addressEmail;
        this.addressText = addressText;
        this.addressBirth = addressBirth;
        this.addressImage = addressImage;
        this.addressStar = addressStar;
    }

    public Address(String addressGroup) {
        this.addressGroup = addressGroup;
    }



    public Address(String addressName, String addressPhone, String addressGroup, String addressEmail) {
        this.addressName = addressName;
        this.addressPhone = addressPhone;
        this.addressGroup = addressGroup;
        this.addressEmail = addressEmail;
    }

    public int getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(int addressNo) {
        this.addressNo = addressNo;
    }

    public String getUser_userId() {
        return user_userId;
    }

    public void setUser_userId(String user_userId) {
        this.user_userId = user_userId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressPhone() {
        return addressPhone;
    }

    public void setAddressPhone(String addressPhone) {
        this.addressPhone = addressPhone;
    }

    public String getAddressGroup() {
        return addressGroup;
    }

    public void setAddressGroup(String addressGroup) {
        this.addressGroup = addressGroup;
    }

    public String getAddressEmail() {
        return addressEmail;
    }

    public void setAddressEmail(String addressEmail) {
        this.addressEmail = addressEmail;
    }

    public String getAddressText() {
        return addressText;
    }

    public void setAddressText(String addressText) {
        this.addressText = addressText;
    }

    public String getAddressBirth() {
        return addressBirth;
    }

    public void setAddressBirth(String addressBirth) {
        this.addressBirth = addressBirth;
    }

    public String getAddressImage() {
        return addressImage;
    }

    public void setAddressImage(String addressImage) {
        this.addressImage = addressImage;
    }

    public String getAddressStar() {
        return addressStar;
    }

    public void setAddressStar(String addressStar) {
        this.addressStar = addressStar;
    }
}

