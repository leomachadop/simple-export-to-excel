package com.simple.export.model

class AuxModel {

    var index = 0

    var header: Array<String> = emptyArray()

    fun plus(): Int {
        return index++
    }
}