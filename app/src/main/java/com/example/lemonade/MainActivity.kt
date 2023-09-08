package com.example.lemonade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lemonade.ui.theme.LemonadeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LemonadeTheme {
                LemonadeApp()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    @Preview
    fun LemonadeApp(modifier: Modifier = Modifier) {
        TopAppBar(
            title = { Text(text = "Lemonade", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center) },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer)
        )
        LemonadeInteractable(modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
        )
    }

    @Composable
    fun LemonadeInteractable(modifier: Modifier) {
        var state by remember { mutableStateOf(1)}
        var numSqueezes by remember { mutableStateOf(0)}
        var goalSqueezes by remember { mutableStateOf(2)}
        var imageResource = when(state) {
            1 -> R.drawable.lemon_tree
            2 -> R.drawable.lemon_squeeze
            3 -> R.drawable.lemon_drink
            else -> R.drawable.lemon_restart
        }
        var contentDescription = when(state) {
            1 -> R.string.image_description_lemon_tree
            2 -> R.string.image_description_lemon
            3 -> R.string.image_description_lemonade
            else -> R.string.image_description_empty_glass
        }
        var stringResource = when(state) {
            1 -> R.string.app_instruction_1
            2 -> R.string.app_instruction_2
            3 -> R.string.app_instruction_3
            else -> R.string.app_instruction_4

        }

        val scale = remember { Animatable(1f) }
        val coroutineScope = rememberCoroutineScope()

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Button(onClick = {
                coroutineScope.launch {
                    scale.animateTo(0.75F, tween(100))
                    scale.animateTo(1F, tween(100))
                }
                if(numSqueezes == 0) { //first squeeze of screen
                    goalSqueezes = (1..3).random()
                    numSqueezes += 1 //continue squeezing
                }
                else {
                    if(numSqueezes < goalSqueezes)
                        numSqueezes += 1 //continue squeezing
                    else {
                        //reset state
                        state = (state + 1) % 4
                        numSqueezes = 0
                    }
                }
            },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.scale(scale = scale.value)
            ) {
                Image(
                    painter = painterResource(id = imageResource),
                    contentDescription = stringResource(id = stringResource)
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = stringResource(id = stringResource),
                fontSize = 18.sp
            )
        }
    }
}

