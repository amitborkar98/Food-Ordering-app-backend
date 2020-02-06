package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "coupon")
@NamedQueries({
        @NamedQuery(name = "getCouponByName", query = "select ut from CouponEntity ut where ut.coupon_name =:coupon_name"),
        @NamedQuery(name = "getCouponById", query = "select ut from CouponEntity ut where ut.uuid =:uuid")
})
public class CouponEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    private  String uuid;

    @Column(name = "coupon_name")
    @Size(max = 30)
    private String coupon_name;

    @Column(name = "percent")
    private Integer percent;

    public CouponEntity(){

    }

    public CouponEntity(String uuid, String coupon_name, Integer percent){
        this.uuid = uuid;
        this.coupon_name = coupon_name;
        this.percent = percent;
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

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }
}
