package com.retail.repository;

import com.retail.entity.Retail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RetailRepository extends JpaRepository<Retail, Long> {

    /**
     * Find retail items by product name (case-insensitive)
     */
    Optional<Retail> findByProductNameIgnoreCase(String productName);

    /**
     * Find all retail items by category
     */
    List<Retail> findByCategoryIgnoreCase(String category);

    /**
     * Find retail items by category and quantity greater than specified value
     */
    @Query("SELECT r FROM Retail r WHERE r.category = :category AND r.quantity > :quantity")
    List<Retail> findByCategoryAndQuantityGreaterThan(@Param("category") String category, 
                                                       @Param("quantity") Integer quantity);

    /**
     * Check if retail item exists by product name (case-insensitive)
     */
    boolean existsByProductNameIgnoreCase(String productName);
}



