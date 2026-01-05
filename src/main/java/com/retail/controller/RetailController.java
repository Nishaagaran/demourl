package com.retail.controller;

import com.retail.entity.Retail;
import com.retail.service.RetailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/retails")
@CrossOrigin(origins = "*")
public class RetailController {

    private final RetailService retailService;

    @Autowired
    public RetailController(RetailService retailService) {
        this.retailService = retailService;
    }

    /**
     * Create a new retail item
     * POST /api/v1/retails
     */
    @PostMapping
    public ResponseEntity<Retail> createRetail(@Valid @RequestBody Retail retail) {
        Retail createdRetail = retailService.createRetail(retail);
        return new ResponseEntity<>(createdRetail, HttpStatus.CREATED);
    }

    /**
     * Get all retail items
     * GET /api/v1/retails
     */
    @GetMapping
    public ResponseEntity<List<Retail>> getAllRetails() {
        List<Retail> retails = retailService.getAllRetails();
        return new ResponseEntity<>(retails, HttpStatus.OK);
    }

    /**
     * Get retail item by ID
     * GET /api/v1/retails/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Retail> getRetailById(@PathVariable Long id) {
        Retail retail = retailService.getRetailById(id);
        return new ResponseEntity<>(retail, HttpStatus.OK);
    }

    /**
     * Get retail item by product name
     * GET /api/v1/retails/product/{productName}
     */
    @GetMapping("/product/{productName}")
    public ResponseEntity<Retail> getRetailByProductName(@PathVariable String productName) {
        Retail retail = retailService.getRetailByProductName(productName);
        return new ResponseEntity<>(retail, HttpStatus.OK);
    }

    /**
     * Get retail items by category
     * GET /api/v1/retails/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Retail>> getRetailsByCategory(@PathVariable String category) {
        List<Retail> retails = retailService.getRetailsByCategory(category);
        return new ResponseEntity<>(retails, HttpStatus.OK);
    }

    /**
     * Update an existing retail item
     * PUT /api/v1/retails/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Retail> updateRetail(@PathVariable Long id, 
                                                @Valid @RequestBody Retail retailDetails) {
        Retail updatedRetail = retailService.updateRetail(id, retailDetails);
        return new ResponseEntity<>(updatedRetail, HttpStatus.OK);
    }

    /**
     * Partially update an existing retail item
     * PATCH /api/v1/retails/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Retail> partialUpdateRetail(@PathVariable Long id, 
                                                       @RequestBody Retail retailDetails) {
        Retail existingRetail = retailService.getRetailById(id);

        if (retailDetails.getProductName() != null) {
            existingRetail.setProductName(retailDetails.getProductName());
        }
        if (retailDetails.getCategory() != null) {
            existingRetail.setCategory(retailDetails.getCategory());
        }
        if (retailDetails.getPrice() != null) {
            existingRetail.setPrice(retailDetails.getPrice());
        }
        if (retailDetails.getQuantity() != null) {
            existingRetail.setQuantity(retailDetails.getQuantity());
        }
        if (retailDetails.getDescription() != null) {
            existingRetail.setDescription(retailDetails.getDescription());
        }

        Retail updatedRetail = retailService.updateRetail(id, existingRetail);
        return new ResponseEntity<>(updatedRetail, HttpStatus.OK);
    }

    /**
     * Delete a retail item by ID
     * DELETE /api/v1/retails/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRetail(@PathVariable Long id) {
        retailService.deleteRetail(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Delete all retail items
     * DELETE /api/v1/retails
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllRetails() {
        retailService.deleteAllRetails();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Check if retail item exists by ID
     * HEAD /api/v1/retails/{id}
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> existsById(@PathVariable Long id) {
        boolean exists = retailService.existsById(id);
        return exists ? new ResponseEntity<>(HttpStatus.OK) 
                      : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

