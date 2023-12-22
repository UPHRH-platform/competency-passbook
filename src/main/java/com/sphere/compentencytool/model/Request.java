package com.sphere.compentencytool.model;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Request {

    @Column(name = "userId")
   private String userId ;
    @Column(name = "typeName")
   private String typeName;

    @Type(type = "json")
    @Column(name = "competencyDetails", columnDefinition = "json")
    private List<Map<String, Object>> competencyDetails;
}
