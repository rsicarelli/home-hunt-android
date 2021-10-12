package com.rsicarelli.homehunt.presentation.filter.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.ui.theme.Size_Regular
import com.rsicarelli.homehunt.ui.theme.Size_Small

@Composable
fun Counter(
    value: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    contentDescription: String,
) {
    val isDecreaseEnabled = value != 0
    val isIncreaseEnabled = value != 5

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundedButton(
            onDecrease,
            isDecreaseEnabled,
            painter = painterResource(id = R.drawable.ic_round_remove_24),
            contentDescription = contentDescription
        )
        Spacer(modifier = Modifier.width(Size_Regular))
        Text(
            modifier = Modifier.width(Size_Regular),
            text = value.toString(),
            style = MaterialTheme.typography.body2
        )
        Spacer(modifier = Modifier.width(Size_Small))
        RoundedButton(
            onIncrease,
            isIncreaseEnabled,
            Icons.Rounded.Add,
            contentDescription = contentDescription
        )
    }
}