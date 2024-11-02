package com.example.feature_record

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SummarizeGoodsScreen(
    back: () -> Unit,
    viewModel: SummarizeGoodsViewModel,
) {
    val items = viewModel.dailyGoodsSalesSummary.collectAsState(initial = emptyList())
    SummarizeGoodsScreen(
        back = back,
        dailyGoodsSalesSummary = items.value,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SummarizeGoodsScreen(
    back: () -> Unit,
    dailyGoodsSalesSummary: List<SummarizeGoodsViewModel.DailyGoodsSalesSummary>,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.diary_goods_summary)) },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },

            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.date),
                    fontSize = 25.sp,
                    modifier = Modifier.width(250.dp)
                )

                Text(
                    text = stringResource(id = R.string.count),
                    fontSize = 25.sp,
                    modifier = Modifier.width(150.dp)
                )

                Text(
                    text = stringResource(id = R.string.soukei),
                    fontSize = 25.sp,
                    modifier = Modifier.width(150.dp)
                )

                Text(
                    text = stringResource(id = R.string.goods_income),
                    fontSize = 25.sp,
                    modifier = Modifier.width(150.dp)
                )

                Text(
                    text = stringResource(id = R.string.fare_income),
                    fontSize = 25.sp,
                    modifier = Modifier.width(150.dp)
                )

                Text(
                    text = stringResource(id = R.string.person),
                    fontSize = 25.sp,
                    modifier = Modifier.width(150.dp)
                )

                Text(
                    text = stringResource(id = R.string.adult),
                    fontSize = 25.sp,
                    modifier = Modifier.width(150.dp)
                )

                Text(
                    text = stringResource(id = R.string.child),
                    fontSize = 25.sp,
                    modifier = Modifier.width(150.dp)
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
}

@Composable
fun SalesSummaryItem(
    dailyGoodsSalesSummary: SummarizeGoodsViewModel.DailyGoodsSalesSummary,
) {
    ListItem(
        headlineContent = {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = dailyGoodsSalesSummary.date,
                    fontSize = 25.sp,
                    modifier = Modifier.width(150.dp)
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
    goodsSales: SummarizeGoodsViewModel.GoodsSalesSummary,
) {
    ListItem(
        headlineContent = {
            Column (
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = goodsSales.goodsName,
                    fontSize = 10.sp,
                    modifier = Modifier.width(50.dp)
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