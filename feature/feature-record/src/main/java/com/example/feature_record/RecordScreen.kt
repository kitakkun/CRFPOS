package com.example.feature_record

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Record
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RecordScreen(
    modifier: Modifier = Modifier,
    recordList: List<Record>,
    onClickItem: (Record) -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.date_time),
                fontSize = 20.sp,
                modifier = Modifier.width(250.dp)
            )

            Text(
                text = stringResource(id = R.string.total),
                fontSize = 20.sp,
                modifier = Modifier.width(120.dp)
            )

            Text(
                text = stringResource(id = R.string.goods_income),
                fontSize = 20.sp,
                modifier = Modifier.width(120.dp)
            )

            Text(
                text = stringResource(id = R.string.fare_income),
                fontSize = 20.sp,
                modifier = Modifier.width(100.dp)
            )

            Text(
                text = stringResource(id = R.string.adult),
                fontSize = 20.sp,
                modifier = Modifier.width(50.dp)
            )

            Text(
                text = stringResource(id = R.string.child),
                fontSize = 20.sp,
                modifier = Modifier.width(50.dp)
            )
        }

        LazyColumn {
            items(
                count = recordList.size,
                key = { index -> recordList[index].id },
                itemContent = {
                    RecordListItem(
                        record = recordList[it],
                        onClick = { onClickItem(recordList[it]) },
                    )
                }
            )
        }

    }

}

@Composable
private fun RecordListItem(
    record: Record,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = convertUnixTimeToDateTime(record.time / 1000),
                    fontSize = 20.sp,
                    modifier = Modifier.width(230.dp)
                )

                Text(
                    text = stringResource(id = R.string.yen, record.total),
                    fontSize = 20.sp,
                    modifier = Modifier.width(100.dp)
                )

                Text(
                    text = stringResource(id = R.string.yen, record.goodsSales),
                    fontSize = 20.sp,
                    modifier = Modifier.width(100.dp)
                )

                Text(
                    text = stringResource(id = R.string.yen, record.fareSales),
                    fontSize = 20.sp,
                    modifier = Modifier.width(80.dp)
                )

                Text(
                    text = record.adult.toString(),
                    fontSize = 20.sp,
                    modifier = Modifier.width(50.dp)
                )

                Text(
                    text = record.child.toString(),
                    fontSize = 20.sp,
                    modifier = Modifier.width(50.dp)
                )

                LazyRow {
                    record.goodsList?.let { it ->
                        items(
                            count = it.size,
                            key = { index -> record.goodsList!![index].goods.id },
                            itemContent = {
                                Text(
                                    text = record.goodsList!![it].goods.name,
                                    fontSize = 15.sp,
                                    modifier = Modifier.width(110.dp)
                                )
                                Text(
                                    text = stringResource(
                                        id = R.string.ko,
                                        record.goodsList!![it].quantity
                                    ),
                                    fontSize = 15.sp,
                                    modifier = Modifier.width(50.dp)
                                )
                            }
                        )
                    }
                }


            }
        },

        modifier = Modifier.clickable {
            onClick()
        }
    )
    HorizontalDivider()
}

private fun convertUnixTimeToDateTime(unixTime: Long): String {
    val date = Date(unixTime * 1000)
    val formatter = SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.getDefault())
    return formatter.format(date)
}