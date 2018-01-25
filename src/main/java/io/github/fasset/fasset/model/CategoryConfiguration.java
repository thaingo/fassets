package io.github.fasset.fasset.model;

import io.github.fasset.fasset.DomainModel;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * This is a representative model of the fixed asset's category for purposes of depreciation, that is its
 * name, its depreciation rate and its depreciation logic
 */
@Entity(name="CategoryConfiguration")
@Audited
public class CategoryConfiguration extends DomainModel<String> {

    @NotNull(message ="Please provide a valid designation for category")
    @Column(name="category")
    private String designation;

    /**
     * The name of the depreciation logic
     */
    @NotNull(message ="Please provide a valid designation for depreciation logic")
    @Column(name="depreciation_logic")
    private String depreciationLogic;

    /**
     * This is the item on which the depreciation rate is applied, as in either the cost
     * or the net book value
     */
    @NotNull(message ="Please provide a valid designation for depreciation deprecant")
    @Column(name="deprecant")
    private String deprecant;

    @NotNull(message = "Please provide depreciation per annum")
    @Column(name="depreciation_rate")
    private double depreciationRate;

    @NotNull(message = "Kindly supply the ledgerId for thiis category")
    @Column(name="category_ledger_id")
    private String categoryLedgerId;

    public CategoryConfiguration() {
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation.toUpperCase();
    }

    public String getDepreciationLogic() {
        return depreciationLogic;
    }

    public void setDepreciationLogic(String depreciationLogic) {
        this.depreciationLogic = depreciationLogic.toUpperCase();
    }

    public String getDeprecant() {
        return deprecant;
    }

    public void setDeprecant(String deprecant) {
        this.deprecant = deprecant.toUpperCase();
    }

    public double getDepreciationRate() {
        return depreciationRate;
    }

    public void setDepreciationRate(double depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public String getCategoryLedgerId() {
        return categoryLedgerId;
    }

    public void setCategoryLedgerId(String categoryLedgerId) {
        this.categoryLedgerId = categoryLedgerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CategoryConfiguration that = (CategoryConfiguration) o;
        return Double.compare(that.depreciationRate, depreciationRate) == 0 &&
                Objects.equals(designation, that.designation) &&
                Objects.equals(depreciationLogic, that.depreciationLogic) &&
                Objects.equals(deprecant, that.deprecant) &&
                Objects.equals(categoryLedgerId, that.categoryLedgerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), designation, depreciationLogic, deprecant, depreciationRate, categoryLedgerId);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CategoryConfiguration{");
        sb.append("designation='").append(designation).append('\'');
        sb.append(", depreciationLogic='").append(depreciationLogic).append('\'');
        sb.append(", deprecant='").append(deprecant).append('\'');
        sb.append(", depreciationRate=").append(depreciationRate);
        sb.append(", categoryLedgerId='").append(categoryLedgerId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}