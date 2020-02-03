package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "state")
@NamedQueries({
        @NamedQuery(name = "getStateById", query = "select ut from StateEntity ut where ut.uuid =:uuid")
})
public class StateEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    private    String uuid;

    @Column(name = "state_name")
    @Size(max = 30)
    private String state_name;

    public StateEntity(){

    }

    public StateEntity(String uuid, String state_name){
        this.uuid = uuid;
        this.state_name =state_name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }
}
