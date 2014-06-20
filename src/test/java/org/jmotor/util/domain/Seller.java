package org.jmotor.util.domain;

import org.jmotor.util.persistence.annotation.Ignore;

import javax.persistence.Id;
import java.util.Date;

/**
 * Component:
 * Description:
 * Date: 14-6-20
 *
 * @author Andy Ai
 */
public class Seller {
    @Id
    private String sellerId;
    private String sellerName;
    private Integer age;
    private Integer level;
    @Ignore(type = Ignore.IgnoreType.UPDATE)
    private Date createdAt;
    @Ignore(type = Ignore.IgnoreType.CREATE)
    private Date updatedAt;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
