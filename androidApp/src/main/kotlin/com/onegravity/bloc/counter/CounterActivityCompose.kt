@file:Suppress("WildcardImport")

package com.onegravity.bloc.counter

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onegravity.bloc.R
import com.onegravity.bloc.bloc
import com.onegravity.bloc.compose.observeState
import com.onegravity.bloc.getOrCreate
import com.onegravity.bloc.util.ComposeAppTheme

class CounterActivityCompose : AppCompatActivity() {

    private val bloc by getOrCreate { bloc<Int, Int>(it, 1) { reduce { state + action } } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state by bloc.observeState()
            ComposeAppTheme {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.counter_value, state),
                        fontSize = 18.sp,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp)
                    )
                    Row {
                        Button(
                            modifier = Modifier
                                .padding(8.dp)
                                .defaultMinSize(120.dp)
                                .width(IntrinsicSize.Min),
                            onClick = { bloc.send(1) },
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                            content = {
                                Text(
                                    text = stringResource(R.string.counter_increment),
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )
                            }
                        )
                        Button(
                            modifier = Modifier
                                .padding(8.dp)
                                .defaultMinSize(120.dp)
                                .width(IntrinsicSize.Min),
                            onClick = { bloc.send(-1) },
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                            content = {
                                Text(
                                    text = stringResource(R.string.counter_decrement),
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )
                            }
                        )
                    }
                }
            }
        }
    }

}
