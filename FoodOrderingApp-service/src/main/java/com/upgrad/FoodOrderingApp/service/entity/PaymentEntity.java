package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "payment")
@NamedQueries({
        @NamedQuery(name = "getPaymentById", query = "select ut from PaymentEntity ut where ut.uuid =:uuid")
})
public class PaymentEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    private  String uuid;

    @Column(name = "payment_name")
    @Size(max = 255)
    private String payment_name;

    public PaymentEntity(){}

    public PaymentEntity(String uuid, String payment_name){
        this.uuid = uuid;
        this.payment_name = payment_name;
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

    public String getPayment_name() {
        return payment_name;
    }

    public void setPaymentName(String payment_name) {
        this.payment_name = payment_name;
    }
}
