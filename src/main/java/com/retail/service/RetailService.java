package com.retail.service;

import com.retail.entity.Retail;
import com.retail.exception.ResourceAlreadyExistsException;
import com.retail.exception.ResourceNotFoundException;
import com.retail.repository.RetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RetailService {

    private final RetailRepository retailRepository;

    @Autowired
    public RetailService(RetailRepository retailRepository) {
        this.retailRepository = retailRepository;
    }

    /**
     * Create a new retail item
     * @param retail the retail item to create
     * @return the created retail item
     * @throws ResourceAlreadyExistsException if a retail item with the same product name already exists
     */
    public Retail createRetail(Retail retail) {
        if (retailRepository.existsByProductNameIgnoreCase(retail.getProductName())) {
            throw new ResourceAlreadyExistsException(
                    "Retail",
                    "productName",
                    retail.getProductName()
            );
        }
        return retailRepository.save(retail);
    }

    /**
     * Get all retail items
     * @return list of all retail items
     */
    @Transactional(readOnly = true)
    public List<Retail> getAllRetails() {
        return retailRepository.findAll();
    }

    /**
     * Get retail item by ID
     * @param id the ID of the retail item
     * @return the retail item
     * @throws ResourceNotFoundException if the retail item is not found
     */
    @Transactional(readOnly = true)
    public Retail getRetailById(Long id) {
        return retailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Retail",
                        "id",
                        id
                ));
    }

    /**
     * Get retail item by product name
     * @param productName the product name
     * @return the retail item
     * @throws ResourceNotFoundException if the retail item is not found
     */
    @Transactional(readOnly = true)
    public Retail getRetailByProductName(String productName) {
        return retailRepository.findByProductNameIgnoreCase(productName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Retail",
                        "productName",
                        productName
                ));
    }

    /**
     * Get all retail items by category
     * @param category the category
     * @return list of retail items in the category
     */
    @Transactional(readOnly = true)
    public List<Retail> getRetailsByCategory(String category) {
        return retailRepository.findByCategoryIgnoreCase(category);
    }

    /**
     * Update an existing retail item
     * @param id the ID of the retail item to update
     * @param retailDetails the updated retail item details
     * @return the updated retail item
     * @throws ResourceNotFoundException if the retail item is not found
     * @throws ResourceAlreadyExistsException if updating product name and it already exists for another item
     */
    public Retail updateRetail(Long id, Retail retailDetails) {
        Retail retail = getRetailById(id);

        // Check if product name is being changed and if it already exists
        if (!retail.getProductName().equalsIgnoreCase(retailDetails.getProductName()) &&
                retailRepository.existsByProductNameIgnoreCase(retailDetails.getProductName())) {
            throw new ResourceAlreadyExistsException(
                    "Retail",
                    "productName",
                    retailDetails.getProductName()
            );
        }

        // Update fields
        retail.setProductName(retailDetails.getProductName());
        retail.setCategory(retailDetails.getCategory());
        retail.setPrice(retailDetails.getPrice());
        retail.setQuantity(retailDetails.getQuantity());
        retail.setDescription(retailDetails.getDescription());

        return retailRepository.save(retail);
    }

    /**
     * Delete a retail item by ID
     * @param id the ID of the retail item to delete
     * @throws ResourceNotFoundException if the retail item is not found
     */
    public void deleteRetail(Long id) {
        Retail retail = getRetailById(id);
        retailRepository.delete(retail);
    }

    /**
     * Delete all retail items
     */
    public void deleteAllRetails() {
        retailRepository.deleteAll();
    }

    /**
     * Check if a retail item exists by ID
     * @param id the ID to check
     * @return true if exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return retailRepository.existsById(id);
    }
}

