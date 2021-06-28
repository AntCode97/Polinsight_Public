package com.dns.polinsight.service;

import com.dns.polinsight.domain.Attach;

import java.util.List;

public interface AttachService {
    List<Attach> findAll();

    Attach find(Attach attach);

    Attach saveOrUpdate(Attach attach);

}
