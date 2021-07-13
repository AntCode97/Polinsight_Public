package com.dns.polinsight.service;

import com.dns.polinsight.domain.Additional;
import com.dns.polinsight.domain.User;

public interface AdditionalService {

  Additional save(Additional additional);

  Additional find(User user) throws Exception;

}
