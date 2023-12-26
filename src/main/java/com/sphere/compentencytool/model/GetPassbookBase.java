package com.sphere.compentencytool.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
public class GetPassbookBase {


    private List<String> userId ;
    private String typeName;
}
