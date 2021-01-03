package com.android.addressproject.Bean;

public class InsertData {

    String addressNo, img_path, user_userId, insertAddressName, insertAddressPhone, insertAddressGroup, insertAddressEmail, insertAddressText, insertAddressBirth;


    public InsertData(String addressNo, String img_path, String user_userId, String insertAddressName, String insertAddressPhone, String insertAddressGroup, String insertAddressEmail, String insertAddressText, String insertAddressBirth) {
        this.addressNo = addressNo;
        this.img_path = img_path;
        this.user_userId = user_userId;
        this.insertAddressName = insertAddressName;
        this.insertAddressPhone = insertAddressPhone;
        this.insertAddressGroup = insertAddressGroup;
        this.insertAddressEmail = insertAddressEmail;
        this.insertAddressText = insertAddressText;
        this.insertAddressBirth = insertAddressBirth;
    }

    public String getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(String addressNo) {
        this.addressNo = addressNo;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getUser_userId() {
        return user_userId;
    }

    public void setUser_userId(String user_userId) {
        this.user_userId = user_userId;
    }

    public String getInsertAddressName() {
        return insertAddressName;
    }

    public void setInsertAddressName(String insertAddressName) {
        this.insertAddressName = insertAddressName;
    }

    public String getInsertAddressPhone() {
        return insertAddressPhone;
    }

    public void setInsertAddressPhone(String insertAddressPhone) {
        this.insertAddressPhone = insertAddressPhone;
    }

    public String getInsertAddressGroup() {
        return insertAddressGroup;
    }

    public void setInsertAddressGroup(String insertAddressGroup) {
        this.insertAddressGroup = insertAddressGroup;
    }

    public String getInsertAddressEmail() {
        return insertAddressEmail;
    }

    public void setInsertAddressEmail(String insertAddressEmail) {
        this.insertAddressEmail = insertAddressEmail;
    }

    public String getInsertAddressText() {
        return insertAddressText;
    }

    public void setInsertAddressText(String insertAddressText) {
        this.insertAddressText = insertAddressText;
    }

    public String getInsertAddressBirth() {
        return insertAddressBirth;
    }

    public void setInsertAddressBirth(String insertAddressBirth) {
        this.insertAddressBirth = insertAddressBirth;
    }
}
