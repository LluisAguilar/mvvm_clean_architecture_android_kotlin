package com.luis.aguilar.android.employeelist.core.mapper

import java.util.ArrayList

abstract class Mapper<T1,T2>{
    abstract fun map(value: T1): T2

    abstract fun reverseMap(value: T2): T1

    open fun map(values: List<T1>): List<T2> {
        val returnValues: MutableList<T2> = ArrayList(values.size)
        for (value in values) {
            returnValues.add(map(value))
        }
        return returnValues
    }

    open fun reverseMap(values: List<T2>): List<T1> {
        val returnValues: MutableList<T1> = ArrayList(values.size)
        for (value in values) {
            returnValues.add(reverseMap(value))
        }
        return returnValues
    }
}