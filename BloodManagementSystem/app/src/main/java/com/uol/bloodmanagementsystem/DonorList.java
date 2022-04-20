package com.uol.bloodmanagementsystem;

public class DonorList {

    private String donor_name;
    private String donor_phoneNo;
    private String donor_bloodGroup;
    private String donor_addByEmail;
    private String donor_city;

    public DonorList() {
    }

    public DonorList(String donor_name, String donor_phoneNo, String donor_bloodGroup, String donor_addByEmail, String donor_city) {
        this.donor_name = donor_name;
        this.donor_phoneNo = donor_phoneNo;
        this.donor_bloodGroup = donor_bloodGroup;
        this.donor_addByEmail = donor_addByEmail;
        this.donor_city = donor_city;
    }

    public String getDonor_name() {
        return donor_name;
    }

    public void setDonor_name(String donor_name) {
        this.donor_name = donor_name;
    }

    public String getDonor_phoneNo() {
        return donor_phoneNo;
    }

    public void setDonor_phoneNo(String donor_phoneNo) {
        this.donor_phoneNo = donor_phoneNo;
    }

    public String getDonor_bloodGroup() {
        return donor_bloodGroup;
    }

    public void setDonor_bloodGroup(String donor_bloodGroup) {
        this.donor_bloodGroup = donor_bloodGroup;
    }

    public String getDonor_addByEmail() {
        return donor_addByEmail;
    }

    public void setDonor_addByEmail(String donor_addByEmail) {
        this.donor_addByEmail = donor_addByEmail;
    }

    public String getDonor_city() {
        return donor_city;
    }

    public void setDonor_city(String donor_city) {
        this.donor_city = donor_city;
    }
}
