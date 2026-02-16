package org.example.roulette.api.roulette.app

data class AdminDashboardQueryResponse(
    val totalBudget: Long,
    val remainingBudget: Long,
    val participantCount: Long,
)
