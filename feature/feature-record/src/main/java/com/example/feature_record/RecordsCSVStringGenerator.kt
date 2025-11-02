package com.example.feature_record

import android.util.Log
import android.util.Log.e
import com.example.model.Record
import java.io.File
import java.io.IOException
import javax.inject.Inject

class RecordsCSVStringGenerator @Inject constructor() {
    fun generateFromRecords(
        recordsList: List<Record>,
    ) : String {
        return StringBuilder().apply {
            appendLine("ID,Time,Total,Fare Sales,Goods Sales,Adult,Child,")
            recordsList.forEach { record ->
                append(record.id.toString())
                append(",")
                append(record.time.toString())
                append(",")
                append(record.total.toString())
                append(",")
                append(record.fareSales.toString())
                append(",")
                append(record.goodsSales.toString())
                append(",")
                append(record.adult.toString())
                append(",")
                append(record.child.toString())
                append(",")
                for (goods in record.goodsList!!) {
                    append(goods.goods.name)
                    append(",")
                    append(goods.quantity.toString())
                    append(",")
                }
                appendLine()
            }
        }.toString()
    }
}