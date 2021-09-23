package com.dns.polinsight.projection;

import com.dns.polinsight.types.Address;
import com.dns.polinsight.types.Email;
import com.dns.polinsight.types.Phone;

public interface UserMapping {

  Long getId();

  Long getPoint();

  Email getEmail();

  Phone getPhone();

  Address getAddress();

  String getName();

  String getEducation();

  String getMarry();

  String getGender();

  String getBirth();

  String getBirthType();

  String getJob();

  String getIndustry();

  Boolean getIsEmailReceive();

  Boolean getIsSmsReceive();

  String getRole();

  String getRegisteredAt();

}
