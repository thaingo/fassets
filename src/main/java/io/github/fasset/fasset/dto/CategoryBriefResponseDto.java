package io.github.fasset.fasset.dto;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import io.github.fasset.fasset.model.brief.CategoryBrief;
import org.javamoney.moneta.Money;

public class CategoryBriefResponseDto {

    private int id;

    /* name of the category*/
    private String designation;

    /* total costs of all items in this category*/
    private double purchaseCost;

    /* total NBVs of all items in this category*/
    private double netBookValue;

    /* Total accumulated depreciation for items in this category*/
    private double accruedDepreciation;

    /* total no. of items in this category*/
    private int poll;


    public CategoryBriefResponseDto(CategoryBrief categoryBrief) {
        this.id=categoryBrief.getId();
        this.designation=categoryBrief.getDesignation();
        this.purchaseCost=categoryBrief.getPurchaseCost().getNumber().doubleValue();
        this.netBookValue=categoryBrief.getNetBookValue().getNumber().doubleValue();
        this.accruedDepreciation=categoryBrief.getAccruedDepreciation().getNumber().doubleValue();
        this.poll=categoryBrief.getPoll();
    }

    public int getId() {
        return id;
    }

    public CategoryBriefResponseDto setId(int id) {
        this.id = id;
        return this;
    }

    public String getDesignation() {
        return designation;
    }

    public CategoryBriefResponseDto setDesignation(String designation) {
        this.designation = designation;
        return this;
    }

    public double getPurchaseCost() {
        return purchaseCost;
    }

    public CategoryBriefResponseDto setPurchaseCost(double purchaseCost) {
        this.purchaseCost = purchaseCost;
        return this;
    }

    public double getNetBookValue() {
        return netBookValue;
    }

    public CategoryBriefResponseDto setNetBookValue(double netBookValue) {
        this.netBookValue = netBookValue;
        return this;
    }

    public double getAccruedDepreciation() {
        return accruedDepreciation;
    }

    public CategoryBriefResponseDto setAccruedDepreciation(double accruedDepreciation) {
        this.accruedDepreciation = accruedDepreciation;
        return this;
    }

    public int getPoll() {
        return poll;
    }

    public CategoryBriefResponseDto setPoll(int poll) {
        this.poll = poll;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryBriefResponseDto that = (CategoryBriefResponseDto) o;
        return id == that.id &&
                Double.compare(that.purchaseCost, purchaseCost) == 0 &&
                Double.compare(that.netBookValue, netBookValue) == 0 &&
                Double.compare(that.accruedDepreciation, accruedDepreciation) == 0 &&
                poll == that.poll &&
                Objects.equal(designation, that.designation);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, designation, purchaseCost, netBookValue, accruedDepreciation, poll);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("designation", designation)
                .add("purchaseCost", purchaseCost)
                .add("netBookValue", netBookValue)
                .add("accruedDepreciation", accruedDepreciation)
                .add("poll", poll)
                .toString();
    }
}