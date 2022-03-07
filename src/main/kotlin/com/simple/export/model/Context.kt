package com.simple.export.model

data class Context(
    val input: String,
    val output: String,
    val separator: String,
    val withoutData: String,
    val sheetName: String,
    val encoding: String,
    val locale: String,
    val debug: Boolean
)
