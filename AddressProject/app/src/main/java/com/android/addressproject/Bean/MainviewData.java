package com.android.addressproject.Bean;

public class MainviewData {
    String viewName, viewPhone, viewGroup, viewEmail, viewText, viewBirth, viewImg;

    public MainviewData(String viewName, String viewPhone, String viewGroup, String viewEmail, String viewText, String viewBirth, String viewImg) {
        this.viewName = viewName;
        this.viewPhone = viewPhone;
        this.viewGroup = viewGroup;
        this.viewEmail = viewEmail;
        this.viewText = viewText;
        this.viewBirth = viewBirth;
        this.viewImg = viewImg;
    }

    public String getViewImg() {
        return viewImg;
    }

    public void setViewImg(String viewImg) {
        this.viewImg = viewImg;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getViewPhone() {
        return viewPhone;
    }

    public void setViewPhone(String viewPhone) {
        this.viewPhone = viewPhone;
    }

    public String getViewGroup() {
        return viewGroup;
    }

    public void setViewGroup(String viewGroup) {
        this.viewGroup = viewGroup;
    }

    public String getViewEmail() {
        return viewEmail;
    }

    public void setViewEmail(String viewEmail) {
        this.viewEmail = viewEmail;
    }

    public String getViewText() {
        return viewText;
    }

    public void setViewText(String viewText) {
        this.viewText = viewText;
    }

    public String getViewBirth() {
        return viewBirth;
    }

    public void setViewBirth(String viewBirth) {
        this.viewBirth = viewBirth;
    }
}
