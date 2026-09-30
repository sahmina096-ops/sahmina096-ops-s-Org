package com.example.data.model

data class MistakeItem(
    val id: Long = 0,
    val question: String,
    val userAnswer: String,
    val correctAnswer: String,
    val explanation: String,
    val topic: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isResolved: Boolean = false
)
