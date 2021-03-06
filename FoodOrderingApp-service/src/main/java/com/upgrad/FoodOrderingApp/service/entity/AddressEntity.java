package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "address")
@NamedQueries({
        @NamedQuery(name = "getAdressId", query = "select ut from AddressEntity ut where ut.uuid =:uuid")
})
public class AddressEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    private  String uuid;

    @Column(name = "flat_buil_number")
    @Size(max = 255)
    private String flat_buil_number;

    @Column(name = "locality")
    @Size(max = 255)
    private String locality;

    @Column(name = "city")
    @Size(max = 30)
    private String city;

    @Column(name = "pincode")
    @Size(max = 30)
    private String pincode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    StateEntity stateEntity;

    @Column(name = "active")
    private Integer active;

    @OneToMany(mappedBy = "addressEntity", fetch = FetchType.LAZY)
    List<OrdersEntity> orders = new ArrayList<>();

    public List<OrdersEntity> getOrders(){
        return orders;
    }

    public AddressEntity(){

    }

    public AddressEntity(String uuid, String flat_buil_number, String locality, String city, String pincode, StateEntity stateEntity){
        this.uuid = uuid;
        this.flat_buil_number = flat_buil_number;
        this.locality = locality;
        this.city = city;
        this.pincode=pincode;
        this.stateEntity = stateEntity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFlat_buil_number() {
        return flat_buil_number;
    }

    public void setFlatBuilNo(String flat_buil_number) {
        this.flat_buil_number = flat_buil_number;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public StateEntity getState(){
        return this.stateEntity;
    }

    public void setState(StateEntity stateEntity){
        this.stateEntity = stateEntity;
    }

}
