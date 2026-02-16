package org.example.roulette.api.order.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,
    @Column(name = "user_id")
    val userId: Long,
    @Column(name = "product_id")
    val productId: Long,
    @Column(name = "price_at_order")
    val priceAtOrder: Long,
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: OrderStatus,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "canceled_at")
    var canceledAt: LocalDateTime? = null,
) {
    fun cancel() {
        if (isCancelled()) {
            throw OrderNotCancelableException()
        }

        this.status = OrderStatus.CANCELLED
        this.canceledAt = LocalDateTime.now()
    }

    fun isCancelled(): Boolean = status == OrderStatus.CANCELLED

    companion object {
        fun create(
            userId: Long,
            productId: Long,
            priceAtOrder: Long,
        ): Order =
            Order(
                userId = userId,
                productId = productId,
                priceAtOrder = priceAtOrder,
                status = OrderStatus.COMPLETED,
            )
    }
}

enum class OrderStatus {
    COMPLETED,
    CANCELLED,
}
