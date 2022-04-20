package com.uol.bloodmanagementsystem;

public class Add_DonorInfo {

    public String donor_name, donor_phoneNo, donor_city, donor_bloodGroup, donor_addByEmail;

    public Add_DonorInfo()
    {
    }

    public Add_DonorInfo(String donor_addByEemail, String donor_name, String donor_bloodGroup, String donor_city, String donor_phoneNo)
    {
        this.donor_addByEmail=donor_addByEemail;
        this.donor_name=donor_name;
        this.donor_bloodGroup=donor_bloodGroup;
        this.donor_city=donor_city;
        this.donor_phoneNo=donor_phoneNo;
    }

}
