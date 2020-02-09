package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "customer")
@NamedQueries({
        @NamedQuery(name = "customerByContact", query = "select u from CustomerEntity u where u.contact_number = :contact_number"),
        @NamedQuery(name = "customerById", query = "select u from CustomerEntity u where u.uuid = :uuid"),
})
public class CustomerEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    private  String uuid;

    @Column(name = "firstname")
    @Size(max = 30)
    private String firstname;

    @Column(name = "lastname")
    @Size(max = 30)
    private  String lastname;

    @Column(name = "email")
    @Size(max = 50)
    private  String email;

    @Column(name = "contact_number")
    @Size(max = 30)
    private  String contact_number;

    @Column(name = "password")
    @Size(max = 255)
    private  String password;

    @Column(name = "salt")
    @Size(max = 255)
    private  String salt;

    @OneToMany(mappedBy = "customerEntity", fetch = FetchType.LAZY)
     private List<OrdersEntity> customerOrders;

    public void setCustomerOrders(List<OrdersEntity> customerOrders){
        this.customerOrders=customerOrders;
    }

    public List<OrdersEntity> getCustomerOrders() {
        return  customerOrders;
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public String getLastame() {
        return lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}

