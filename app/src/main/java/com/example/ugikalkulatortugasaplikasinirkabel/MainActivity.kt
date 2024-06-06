package com.example.ugikalkulatortugasaplikasinirkabel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ugikalkulatortugasaplikasinirkabel.ui.theme.UgiKalkulatorTugasAplikasiNirkabelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UgiKalkulatorTugasAplikasiNirkabelTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorApp()
                }
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var displayText by remember { mutableStateOf("") }
    var firstNumber by remember { mutableStateOf("") }
    var secondNumber by remember { mutableStateOf("") }
    var operator by remember { mutableStateOf<Char?>(null) }
    var isSecondNumber by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = displayText,
            fontSize = 36.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            maxLines = 1
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CalculatorButton("7") { appendValue("7", isSecondNumber, firstNumber, secondNumber, operator) { updatedFirst, updatedSecond, display -> firstNumber = updatedFirst; secondNumber = updatedSecond; displayText = display } }
                CalculatorButton("8") { appendValue("8", isSecondNumber, firstNumber, secondNumber, operator) { updatedFirst, updatedSecond, display -> firstNumber = updatedFirst; secondNumber = updatedSecond; displayText = display } }
                CalculatorButton("9") { appendValue("9", isSecondNumber, firstNumber, secondNumber, operator) { updatedFirst, updatedSecond, display -> firstNumber = updatedFirst; secondNumber = updatedSecond; displayText = display } }
                CalculatorButton("/") { setOperator('/', firstNumber) { newOperator, display -> operator = newOperator; displayText = display; isSecondNumber = true } }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CalculatorButton("4") { appendValue("4", isSecondNumber, firstNumber, secondNumber, operator) { updatedFirst, updatedSecond, display -> firstNumber = updatedFirst; secondNumber = updatedSecond; displayText = display } }
                CalculatorButton("5") { appendValue("5", isSecondNumber, firstNumber, secondNumber, operator) { updatedFirst, updatedSecond, display -> firstNumber = updatedFirst; secondNumber = updatedSecond; displayText = display } }
                CalculatorButton("6") { appendValue("6", isSecondNumber, firstNumber, secondNumber, operator) { updatedFirst, updatedSecond, display -> firstNumber = updatedFirst; secondNumber = updatedSecond; displayText = display } }
                CalculatorButton("*") { setOperator('*', firstNumber) { newOperator, display -> operator = newOperator; displayText = display; isSecondNumber = true } }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CalculatorButton("1") { appendValue("1", isSecondNumber, firstNumber, secondNumber, operator) { updatedFirst, updatedSecond, display -> firstNumber = updatedFirst; secondNumber = updatedSecond; displayText = display } }
                CalculatorButton("2") { appendValue("2", isSecondNumber, firstNumber, secondNumber, operator) { updatedFirst, updatedSecond, display -> firstNumber = updatedFirst; secondNumber = updatedSecond; displayText = display } }
                CalculatorButton("3") { appendValue("3", isSecondNumber, firstNumber, secondNumber, operator) { updatedFirst, updatedSecond, display -> firstNumber = updatedFirst; secondNumber = updatedSecond; displayText = display } }
                CalculatorButton("-") { setOperator('-', firstNumber) { newOperator, display -> operator = newOperator; displayText = display; isSecondNumber = true } }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CalculatorButton("C") { clearCalculator { display, resetFirst, resetSecond, resetOperator, resetIsSecondNumber -> displayText = display; firstNumber = resetFirst; secondNumber = resetSecond; operator = resetOperator; isSecondNumber = resetIsSecondNumber } }
                CalculatorButton("0") { appendValue("0", isSecondNumber, firstNumber, secondNumber, operator) { updatedFirst, updatedSecond, display -> firstNumber = updatedFirst; secondNumber = updatedSecond; displayText = display } }
                CalculatorButton("=") { calculateResult(firstNumber, secondNumber, operator) { result, display -> firstNumber = result; secondNumber = ""; operator = null; displayText = display; isSecondNumber = false } }
                CalculatorButton("+") { setOperator('+', firstNumber) { newOperator, display -> operator = newOperator; displayText = display; isSecondNumber = true } }
            }
        }
    }
}

@Composable
fun CalculatorButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(4.dp)
            .size(64.dp)
    ) {
        Text(text = text, fontSize = 24.sp)
    }
}

fun appendValue(value: String, isSecondNumber: Boolean, firstNumber: String, secondNumber: String, operator: Char?, onUpdate: (String, String, String) -> Unit) {
    if (isSecondNumber) {
        val newSecondNumber = secondNumber + value
        onUpdate(firstNumber, newSecondNumber, firstNumber + operator.toString() + newSecondNumber)
    } else {
        val newFirstNumber = firstNumber + value
        onUpdate(newFirstNumber, secondNumber, newFirstNumber)
    }
}

fun setOperator(newOperator: Char, firstNumber: String, onUpdate: (Char, String) -> Unit) {
    if (firstNumber.isNotEmpty()) {
        onUpdate(newOperator, firstNumber + newOperator)
    }
}

fun clearCalculator(onUpdate: (String, String, String, Char?, Boolean) -> Unit) {
    onUpdate("", "", "", null, false)
}

fun calculateResult(firstNumber: String, secondNumber: String, operator: Char?, onUpdate: (String, String) -> Unit) {
    val firstValue = firstNumber.toDoubleOrNull()
    val secondValue = secondNumber.toDoubleOrNull()
    if (firstValue != null && secondValue != null && operator != null) {
        val result = when (operator) {
            '+' -> firstValue + secondValue
            '-' -> firstValue - secondValue
            '*' -> firstValue * secondValue
            '/' -> if (secondValue != 0.0) firstValue / secondValue else Double.NaN
            else -> Double.NaN
        }
        onUpdate(result.toString(), result.toString())
    }
}

/* === BY: Ugi Ispoyo Widodo === */