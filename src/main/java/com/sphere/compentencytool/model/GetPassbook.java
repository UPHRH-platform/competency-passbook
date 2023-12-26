package com.sphere.compentencytool.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;

@Getter
@Setter
public class GetPassbook implements  Cloneable {

    @Type(type = "json")
    private GetPassbookBase request;

    public GetPassbook clone() throws CloneNotSupportedException {
        return (GetPassbook) super.clone();
    }
}
