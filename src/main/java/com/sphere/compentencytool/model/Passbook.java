package com.sphere.compentencytool.model;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
@Getter
@Setter
@Entity()
@Table(name="passbook",schema="public")
@DynamicUpdate
@TypeDefs({ @TypeDef(name = "json", typeClass = JsonType.class) })
public class Passbook implements Cloneable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @Type(type = "json")
    @Column(name = "request", columnDefinition = "json")
    private Request request;

    public Passbook clone() throws CloneNotSupportedException {
        return (Passbook) super.clone();
    }




}
