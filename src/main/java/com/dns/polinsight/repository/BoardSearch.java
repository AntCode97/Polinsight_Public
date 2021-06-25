package com.dns.polinsight.repository;

import com.dns.polinsight.domain.SearchKind;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardSearch {

    private SearchKind searchKind;
    private String searchValue;

}
