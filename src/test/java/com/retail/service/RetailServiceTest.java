package com.retail.service;

import com.retail.entity.Retail;
import com.retail.exception.ResourceAlreadyExistsException;
import com.retail.exception.ResourceNotFoundException;
import com.retail.repository.RetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RetailService Unit Tests")
class RetailServiceTest {

    @Mock
    private RetailRepository retailRepository;

    @InjectMocks
    private RetailService retailService;

    private Retail retail;
    private Retail retail2;

    @BeforeEach
    void setUp() {
        retail = new Retail();
        retail.setId(1L);
        retail.setProductName("Laptop");
        retail.setCategory("Electronics");
        retail.setPrice(new BigDecimal("999.99"));
        retail.setQuantity(10);
        retail.setDescription("High-performance laptop");
        retail.setCreatedAt(LocalDateTime.now());
        retail.setUpdatedAt(LocalDateTime.now());

        retail2 = new Retail();
        retail2.setId(2L);
        retail2.setProductName("Smartphone");
        retail2.setCategory("Electronics");
        retail2.setPrice(new BigDecimal("699.99"));
        retail2.setQuantity(20);
        retail2.setDescription("Latest smartphone model");
    }

    @Test
    @DisplayName("Should create retail item successfully")
    void testCreateRetail_Success() {
        // Given
        Retail newRetail = new Retail();
        newRetail.setProductName("Tablet");
        newRetail.setCategory("Electronics");
        newRetail.setPrice(new BigDecimal("499.99"));
        newRetail.setQuantity(15);

        when(retailRepository.existsByProductNameIgnoreCase(anyString())).thenReturn(false);
        when(retailRepository.save(any(Retail.class))).thenReturn(newRetail);

        // When
        Retail result = retailService.createRetail(newRetail);

        // Then
        assertNotNull(result);
        assertEquals("Tablet", result.getProductName());
        assertEquals("Electronics", result.getCategory());
        assertEquals(new BigDecimal("499.99"), result.getPrice());
        assertEquals(15, result.getQuantity());
        verify(retailRepository, times(1)).existsByProductNameIgnoreCase(anyString());
        verify(retailRepository, times(1)).save(any(Retail.class));
    }

    @Test
    @DisplayName("Should throw exception when creating retail item with existing product name")
    void testCreateRetail_ProductNameExists_ThrowsException() {
        // Given
        Retail newRetail = new Retail();
        newRetail.setProductName("Laptop");
        newRetail.setCategory("Electronics");
        newRetail.setPrice(new BigDecimal("999.99"));
        newRetail.setQuantity(10);

        when(retailRepository.existsByProductNameIgnoreCase(anyString())).thenReturn(true);

        // When & Then
        ResourceAlreadyExistsException exception = assertThrows(
                ResourceAlreadyExistsException.class,
                () -> retailService.createRetail(newRetail)
        );

        assertTrue(exception.getMessage().contains("Retail"));
        assertTrue(exception.getMessage().contains("productName"));
        assertTrue(exception.getMessage().contains("Laptop"));
        verify(retailRepository, times(1)).existsByProductNameIgnoreCase(anyString());
        verify(retailRepository, never()).save(any(Retail.class));
    }

    @Test
    @DisplayName("Should get all retail items successfully")
    void testGetAllRetails_Success() {
        // Given
        List<Retail> retailList = Arrays.asList(retail, retail2);
        when(retailRepository.findAll()).thenReturn(retailList);

        // When
        List<Retail> result = retailService.getAllRetails();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getProductName());
        assertEquals("Smartphone", result.get(1).getProductName());
        verify(retailRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get retail item by ID successfully")
    void testGetRetailById_Success() {
        // Given
        when(retailRepository.findById(1L)).thenReturn(Optional.of(retail));

        // When
        Retail result = retailService.getRetailById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getProductName());
        verify(retailRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when retail item not found by ID")
    void testGetRetailById_NotFound_ThrowsException() {
        // Given
        when(retailRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> retailService.getRetailById(999L)
        );

        assertTrue(exception.getMessage().contains("Retail"));
        assertTrue(exception.getMessage().contains("id"));
        assertTrue(exception.getMessage().contains("999"));
        verify(retailRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should get retail item by product name successfully")
    void testGetRetailByProductName_Success() {
        // Given
        when(retailRepository.findByProductNameIgnoreCase("Laptop")).thenReturn(Optional.of(retail));

        // When
        Retail result = retailService.getRetailByProductName("Laptop");

        // Then
        assertNotNull(result);
        assertEquals("Laptop", result.getProductName());
        verify(retailRepository, times(1)).findByProductNameIgnoreCase("Laptop");
    }

    @Test
    @DisplayName("Should throw exception when retail item not found by product name")
    void testGetRetailByProductName_NotFound_ThrowsException() {
        // Given
        when(retailRepository.findByProductNameIgnoreCase("NonExistentProduct")).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> retailService.getRetailByProductName("NonExistentProduct")
        );

        assertTrue(exception.getMessage().contains("Retail"));
        assertTrue(exception.getMessage().contains("productName"));
        verify(retailRepository, times(1)).findByProductNameIgnoreCase("NonExistentProduct");
    }

    @Test
    @DisplayName("Should get retail items by category successfully")
    void testGetRetailsByCategory_Success() {
        // Given
        List<Retail> electronicsList = Arrays.asList(retail, retail2);
        when(retailRepository.findByCategoryIgnoreCase("Electronics")).thenReturn(electronicsList);

        // When
        List<Retail> result = retailService.getRetailsByCategory("Electronics");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(retailRepository, times(1)).findByCategoryIgnoreCase("Electronics");
    }

    @Test
    @DisplayName("Should update retail item successfully")
    void testUpdateRetail_Success() {
        // Given
        Retail updatedRetail = new Retail();
        updatedRetail.setProductName("Gaming Laptop");
        updatedRetail.setCategory("Electronics");
        updatedRetail.setPrice(new BigDecimal("1299.99"));
        updatedRetail.setQuantity(5);
        updatedRetail.setDescription("High-end gaming laptop");

        when(retailRepository.findById(1L)).thenReturn(Optional.of(retail));
        when(retailRepository.existsByProductNameIgnoreCase("Gaming Laptop")).thenReturn(false);
        when(retailRepository.save(any(Retail.class))).thenReturn(retail);

        // When
        Retail result = retailService.updateRetail(1L, updatedRetail);

        // Then
        assertNotNull(result);
        verify(retailRepository, times(1)).findById(1L);
        verify(retailRepository, times(1)).existsByProductNameIgnoreCase("Gaming Laptop");
        verify(retailRepository, times(1)).save(any(Retail.class));
    }

    @Test
    @DisplayName("Should throw exception when updating retail item with existing product name")
    void testUpdateRetail_ProductNameExists_ThrowsException() {
        // Given
        Retail updatedRetail = new Retail();
        updatedRetail.setProductName("Smartphone"); // This already exists for retail2

        when(retailRepository.findById(1L)).thenReturn(Optional.of(retail));
        when(retailRepository.existsByProductNameIgnoreCase("Smartphone")).thenReturn(true);

        // When & Then
        ResourceAlreadyExistsException exception = assertThrows(
                ResourceAlreadyExistsException.class,
                () -> retailService.updateRetail(1L, updatedRetail)
        );

        assertTrue(exception.getMessage().contains("Retail"));
        assertTrue(exception.getMessage().contains("productName"));
        verify(retailRepository, times(1)).findById(1L);
        verify(retailRepository, times(1)).existsByProductNameIgnoreCase("Smartphone");
        verify(retailRepository, never()).save(any(Retail.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent retail item")
    void testUpdateRetail_NotFound_ThrowsException() {
        // Given
        Retail updatedRetail = new Retail();
        updatedRetail.setProductName("Updated Product");

        when(retailRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> retailService.updateRetail(999L, updatedRetail)
        );

        assertTrue(exception.getMessage().contains("Retail"));
        verify(retailRepository, times(1)).findById(999L);
        verify(retailRepository, never()).save(any(Retail.class));
    }

    @Test
    @DisplayName("Should delete retail item successfully")
    void testDeleteRetail_Success() {
        // Given
        when(retailRepository.findById(1L)).thenReturn(Optional.of(retail));
        doNothing().when(retailRepository).delete(retail);

        // When
        retailService.deleteRetail(1L);

        // Then
        verify(retailRepository, times(1)).findById(1L);
        verify(retailRepository, times(1)).delete(retail);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent retail item")
    void testDeleteRetail_NotFound_ThrowsException() {
        // Given
        when(retailRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> retailService.deleteRetail(999L)
        );

        assertTrue(exception.getMessage().contains("Retail"));
        verify(retailRepository, times(1)).findById(999L);
        verify(retailRepository, never()).delete(any(Retail.class));
    }

    @Test
    @DisplayName("Should delete all retail items successfully")
    void testDeleteAllRetails_Success() {
        // Given
        doNothing().when(retailRepository).deleteAll();

        // When
        retailService.deleteAllRetails();

        // Then
        verify(retailRepository, times(1)).deleteAll();
    }

    @Test
    @DisplayName("Should check if retail item exists by ID")
    void testExistsById_Success() {
        // Given
        when(retailRepository.existsById(1L)).thenReturn(true);

        // When
        boolean result = retailService.existsById(1L);

        // Then
        assertTrue(result);
        verify(retailRepository, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("Should return false when retail item does not exist by ID")
    void testExistsById_NotFound() {
        // Given
        when(retailRepository.existsById(999L)).thenReturn(false);

        // When
        boolean result = retailService.existsById(999L);

        // Then
        assertFalse(result);
        verify(retailRepository, times(1)).existsById(999L);
    }
}


