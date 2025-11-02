package com.example.feature_record

data class DailyGoodsSalesSummary(
    val date: String,
    val salesSummary: List<GoodsSalesSummary>
)