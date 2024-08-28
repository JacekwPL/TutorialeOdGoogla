package com.example.tutorialprzyciski

import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tutorialprzyciski.ui.theme.TutorialPrzyciskiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TutorialPrzyciskiTheme {
                TipCounter(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TipCounterAppPreview() {
    TipCounter(
        modifier = Modifier
            .fillMaxSize()
    )
}

@Composable
fun TipCounter(modifier: Modifier = Modifier) {
    var amountInput by remember { mutableStateOf("") }
    var tipPercentage by remember { mutableStateOf("15") }
    var doRound by remember { mutableStateOf(false) }
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipProc = tipPercentage.toDoubleOrNull() ?: 15.0
    val tip = calculateTip(amount, tipProc, doRound)


    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            label = R.string.bill_amount,
            value = amountInput,
            leadingIcon = R.drawable.money,
            onValueChange = { amountInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
        EditNumberField(
            label = R.string.how_was_the_service,
            value = tipPercentage,
            leadingIcon = R.drawable.percent,
            onValueChange = { tipPercentage = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 64.dp)
        ) {
            Text(
                text = stringResource(id = R.string.round_up_tip)
            )
            Switch(
                checked = doRound,
                onCheckedChange = { doRound = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
            )
        }
        Text(
            text = stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall,
        )
        Spacer(modifier = Modifier.padding(bottom = 64.dp))
    }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done
    )
) {
    TextField(
        value = value,
        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIcon),
                contentDescription = null
            )
        },
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text(stringResource(label)) },
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )

}

private fun calculateTip(
    amount: Double,
    tipPercent: Double = 15.0,
    roundUp: Boolean
): String {
    return NumberFormat.getCurrencyInstance()
        .format(if (roundUp) kotlin.math.ceil(tipPercent / 100 * amount) else tipPercent / 100 * amount)
}

