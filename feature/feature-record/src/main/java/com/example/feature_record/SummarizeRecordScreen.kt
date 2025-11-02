package com.example.feature_record

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.database.dao.RecordDao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummarizeRecordScreen(
    recordDateList: List<RecordDao.Summary>,
    onClickItem: (RecordDao.Summary) -> Unit,
) {
    Column {
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
                count = recordDateList.size,
                key = { index -> recordDateList[index].date },
                itemContent = {
                    SummaryRecordItem(
                        recordSummary = recordDateList[it],
                        onClick = { onClickItem(recordDateList[it]) },
                    )
                }
            )
        }
    }
}

@Composable
fun SummaryRecordItem(
    recordSummary: RecordDao.Summary,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = recordSummary.date,
                    fontSize = 25.sp,
                    modifier = Modifier.width(250.dp)
                )

                Text(
                    text = stringResource(id = R.string.ken, recordSummary.numOfSales.toString()),
                    fontSize = 25.sp,
                    modifier = Modifier.width(150.dp)
                )


                Text(
                    text = stringResource(id = R.string.yen, recordSummary.totalSum.toString()),
                    fontSize = 25.sp,
                    modifier = Modifier.width(150.dp)
                )

                Text(
                    text = stringResource(
                        id = R.string.yen,
                        recordSummary.goodsSalesSum.toString()
                    ),
                    fontSize = 25.sp,
                    modifier = Modifier.width(150.dp)
                )

                Text(
                    text = stringResource(id = R.string.yen, recordSummary.fareSalesSum.toString()),
                    fontSize = 25.sp,
                    modifier = Modifier.width(150.dp)
                )

                Text(
                    text = recordSummary.numOfPerson.toString(),
                    fontSize = 25.sp,
                    modifier = Modifier.width(150.dp)
                )

                Text(
                    text = recordSummary.adultSum.toString(),
                    fontSize = 25.sp,
                    modifier = Modifier.width(150.dp)
                )

                Text(
                    text = recordSummary.childSum.toString(),
                    fontSize = 25.sp,
                    modifier = Modifier.width(150.dp)
                )


            }
        },

        modifier = Modifier.clickable {
            onClick()
        }
    )
    HorizontalDivider()
}