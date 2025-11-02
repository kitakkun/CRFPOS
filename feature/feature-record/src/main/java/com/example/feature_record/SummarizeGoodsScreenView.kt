package com.example.feature_record

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummarizeGoodsScreenView(
    dailyGoodsSalesSummary: List<DailyGoodsSalesSummary>,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.date),
                fontSize = 20.sp,
                modifier = Modifier.width(120.dp)
            )

        }
        LazyColumn {
            items(
                count = dailyGoodsSalesSummary.size,
                key = { index -> dailyGoodsSalesSummary[index].date },
                itemContent = {
                    SalesSummaryItem(
                        dailyGoodsSalesSummary = dailyGoodsSalesSummary[it],
                    )
                }
            )
        }
    }
}

@Composable
fun SalesSummaryItem(
    dailyGoodsSalesSummary: DailyGoodsSalesSummary,
) {
    ListItem(
        headlineContent = {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = dailyGoodsSalesSummary.date,
                    fontSize = 20.sp,
                    modifier = Modifier.width(120.dp)
                )

                LazyRow {
                    items(
                        count = dailyGoodsSalesSummary.salesSummary.size,
                        key = { index -> dailyGoodsSalesSummary.salesSummary[index].goodsId },
                        itemContent = {
                            SalesSummaryGoodsItem(
                                goodsSales = dailyGoodsSalesSummary.salesSummary[it],

                                )
                        }
                    )
                }
            }

        },

        )
    HorizontalDivider()
}

@Composable
fun SalesSummaryGoodsItem(
    goodsSales: GoodsSalesSummary,
) {
    ListItem(
        headlineContent = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = goodsSales.goodsName,
                    fontSize = 10.sp,
                    modifier = Modifier.width(55.dp)
                )

                Text(
                    text = goodsSales.totalQuantity.toString(),
                    fontSize = 15.sp,
                    modifier = Modifier.width(20.dp)
                )
            }
        },
    )
}