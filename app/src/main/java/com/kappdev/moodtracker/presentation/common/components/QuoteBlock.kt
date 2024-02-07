package com.kappdev.moodtracker.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.moodtracker.domain.model.Quote

@Composable
fun QuoteBlock(
    quote: Quote,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = QuoteShape
            )
            .convexEffect(QuoteShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        QuoteMark(
            Modifier
                .rotate(180f)
                .align(Alignment.Top)
        )
        QuoteContent(
            quote = quote,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp)
        )
        QuoteMark(
            Modifier.align(Alignment.Bottom)
        )
    }
}

@Composable
private fun QuoteContent(
    quote: Quote,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        QuoteText(text = quote.text)
        QuoteText(
            text = quote.author,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
private fun QuoteText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 14.sp,
        modifier = modifier,
        lineHeight = 18.sp,
        fontStyle = FontStyle.Italic,
        fontFamily = FontFamily.Serif,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun QuoteMark(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Rounded.FormatQuote,
        tint = MaterialTheme.colorScheme.primary,
        modifier = modifier.size(QuoteMarkSize),
        contentDescription = "Quote mark"
    )
}

private val QuoteMarkSize = 24.dp
private val QuoteShape = RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp)