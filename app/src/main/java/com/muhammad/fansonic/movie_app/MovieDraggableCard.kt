package com.muhammad.fansonic.movie_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.R

@Composable
fun MovieDraggableCard(
    modifier: Modifier = Modifier,
    movieItem: MovieItem,
    enabled : Boolean,
    onSwiped: (SwipeResult, Any) -> Unit,
) {
    DraggableCard(item = movieItem, onSwiped = onSwiped,enabled = enabled, modifier = modifier, content = {
        Column(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(movieItem.image),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth().height(300.dp)
                    .clip(RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp))
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = movieItem.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                )
                {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_star),
                        tint = Color(0xFFFFD700),
                        contentDescription = null, modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = movieItem.rating,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.surface)
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = movieItem.description,
                modifier = Modifier.padding(horizontal = 8.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.surface)
            )
            Spacer(Modifier.height(12.dp))
        }
    })
}