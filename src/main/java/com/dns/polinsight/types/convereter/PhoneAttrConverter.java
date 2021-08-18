package com.dns.polinsight.types.convereter;

import com.dns.polinsight.types.Phone;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PhoneAttrConverter implements AttributeConverter<Phone, String> {

  private final String sep = "-";

  @Override
  public String convertToDatabaseColumn(Phone phone) {
    if (phone == null)
      return null;
    return phone.getFirst() + sep + phone.getSecond() + sep + phone.getThird();
  }

  @Override
  public Phone convertToEntityAttribute(String dbData) {
    if (dbData == null)
      return null;
    if (dbData.isEmpty() || dbData.isBlank())
      return null;
    String[] phone = dbData.split(sep);
    return Phone.builder()
                .first(phone[0])
                .second(phone[1])
                .third(phone[2])
                .build();
  }

}
