package com.rsicarelli.homehunt.presentation.propertyDetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.ui.theme.SpaceMedium
import com.rsicarelli.homehunt.ui.theme.SpaceSmall

@Composable
fun PropertyFooter(reference: String, propertyUrl: String, pdfUrl: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SpaceMedium)
    ) {

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(
                    MaterialTheme.colors.primary.copy(alpha = 0.3f),
                    RoundedCornerShape(40.dp)
                )
        )

        Spacer(modifier = Modifier.height(SpaceSmall))

        SelectionContainer {
            Text(reference, style = MaterialTheme.typography.overline.copy(fontSize = 14.sp))
        }

        Spacer(modifier = Modifier.height(SpaceSmall))

        CustomClickableText(
            propertyUrl = propertyUrl,
            placeholder = R.string.see_on_web
        )

        pdfUrl?.let {
            CustomClickableText(
                propertyUrl = it,
                placeholder = R.string.see_pdf
            )
        }
    }
}