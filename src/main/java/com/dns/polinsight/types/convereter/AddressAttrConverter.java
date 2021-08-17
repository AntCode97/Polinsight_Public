package com.dns.polinsight.types.convereter;

import com.dns.polinsight.types.Address;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class AddressAttrConverter implements AttributeConverter<Address, String> {

  public static final String sep = " ";

  @Override
  public String convertToDatabaseColumn(Address address) {
    if (address == null)
      return null;
    return address.getState() + sep + address.getCity();
  }

  @Override
  public Address convertToEntityAttribute(String dbData) {
    if (dbData == null)
      return null;
    if (dbData.isEmpty() || dbData.isBlank())
      return null;
    String[] address = dbData.split(sep);
    return Address.builder()
                  .state(address[0])
                  .city(address[1])
                  .build();
  }

}
