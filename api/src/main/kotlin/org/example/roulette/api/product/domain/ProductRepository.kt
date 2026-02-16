package org.example.roulette.api.product.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductRepository : JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.deletedAt IS NULL")
    fun findAllNotDeleted(): List<Product>

    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.deletedAt IS NULL")
    fun findByIdNotDeleted(id: Long): Product?
}
