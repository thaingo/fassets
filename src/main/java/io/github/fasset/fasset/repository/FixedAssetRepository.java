package io.github.fasset.fasset.repository;

import io.github.fasset.fasset.model.FixedAsset;
import org.javamoney.moneta.Money;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository("fixedAssetRepository")
public interface FixedAssetRepository extends JpaRepository<FixedAsset,Integer>{

    @Query("SELECT " +
            "DISTINCT e.category " +
            "FROM FixedAsset e")
    List<String> getDistinctCategories();

    @Query("SELECT " +
            "DISTINCT e.solId " +
            "FROM FixedAsset e")
    List<String> getDistinctSolIds();

    /**
     * Return total purchase cost for a given category
     *
     * @param category of fixed asset
     * @return
     */
    @Query("Select "+
            "SUM(e.purchaseCost) " +
            "FROM FixedAsset e " +
            "WHERE e.category = :category ")
    Money getTotalCategoryPurchaseCost(@Param("category") String category);

    @Query("SELECT " +
            "SUM(e.purchaseCost) " +
            "FROM FixedAsset e " +
            "WHERE e.solId = :solId ")
    Money getTotalSolPurchaseCost(@Param("solId") String solId);

    /**
     * Return total net book value for a given category
     *
     * @param category of fixed asset
     * @return
     */
    @Query("Select "+
            "SUM(e.netBookValue) " +
            "FROM FixedAsset e " +
            "WHERE e.category = :category ")
    Money getTotalCategoryNetBookValue(@Param("category") String category);

    @Query("SELECT " +
            "SUM(e.netBookValue) " +
            "FROM FixedAsset e " +
            "WHERE e.solId = :solId ")
    Money getTotalSolNetBookValue(@Param("solId") String solId);

    /**
     * Return total no. of items for a given category
     *
     * @param category of fixed asset
     * @return
     */
    @Query("Select "+
            "COUNT(e.category) " +
            "FROM FixedAsset e " +
            "WHERE e.category = :category ")
    int getTotalCategoryCount(@Param("category") String category);

    @Query("SELECT " +
            "COUNT(e.solId) " +
            "FROM FixedAsset e " +
            "WHERE e.solId = :solId ")
    int getTotalSolCount(@Param("solId") String solId);


}
