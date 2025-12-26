package com.muhammad.fansonic.heightPicker

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.R
import com.muhammad.fansonic.ui.theme.FansonicTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HeightPickerScreen() {
    val layoutDirection = LocalLayoutDirection.current
    var age by remember { mutableIntStateOf(0) }
    var weight by remember { mutableIntStateOf(0) }
    var height by remember { mutableIntStateOf(0) }
    var isDarkTheme by remember { mutableStateOf(false) }
    FansonicTheme(darkTheme = isDarkTheme){
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.measurement_pickers))
                }, actions = {
                    IconButton(onClick = {
                        isDarkTheme =!isDarkTheme
                    }, shapes = IconButtonDefaults.shapes()) {
                        val icon = if(isDarkTheme) R.drawable.moon else R.drawable.sun
                        Image(imageVector = ImageVector.vectorResource(icon),contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }) { paddingValues ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = paddingValues.calculateLeftPadding(layoutDirection) + 16.dp,
                    end = paddingValues.calculateEndPadding(layoutDirection) + 16.dp,
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item("agePickerSection") {
                    MeasurementPickerSection(
                        title = R.string.age,
                        description = R.string.age_description,
                        unit = R.string.years,
                        icon = R.drawable.ic_age,
                        minMeasurement = 10,
                        maxMeasurement = 100,
                        initialMeasurement = 20,
                        currentMeasurement = age,
                        onMeasureChange = { newAge ->
                            age = newAge
                        })
                }
                item("weightPickerSection") {
                    MeasurementPickerSection(
                        title = R.string.weight,
                        description = R.string.weight_description,
                        unit = R.string.kg,
                        icon = R.drawable.ic_weight,
                        minMeasurement = 20,
                        maxMeasurement = 120,
                        initialMeasurement = 50,
                        currentMeasurement = weight,
                        onMeasureChange = { newWeight->
                            weight = newWeight
                        })
                }
                item("heightPickerSection") {
                    MeasurementPickerSection(
                        title = R.string.height,
                        description = R.string.height_description,
                        unit = R.string.cm,
                        icon = R.drawable.ic_height,
                        minMeasurement = 100,
                        maxMeasurement = 300,
                        initialMeasurement = 150,
                        currentMeasurement = height,
                        onMeasureChange = { newHeight->
                            height = newHeight
                        })
                }
            }
        }
    }
}

@Composable
fun MeasurementPickerSection(
    @StringRes title: Int,
    @StringRes description: Int,
    @StringRes unit: Int,
    icon: Int,
    currentMeasurement: Int,
    minMeasurement: Int,
    maxMeasurement: Int,
    initialMeasurement: Int,
    modifier: Modifier = Modifier, onMeasureChange: (Int) -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(description),
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.surface)
        )
        Spacer(Modifier.height(16.dp))
        MeasurementPicker(
            modifier = Modifier.fillMaxWidth(),
            minMeasurement = minMeasurement,
            maxMeasurement = maxMeasurement,
            initialMeasurement = initialMeasurement,
            onMeasurementChange = { measurement ->
                onMeasureChange(measurement)
            })
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = "$currentMeasurement ${stringResource(unit)}",
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
            )
        }
    }
}
