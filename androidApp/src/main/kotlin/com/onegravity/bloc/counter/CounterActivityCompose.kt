package com.onegravity.bloc.counter

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                        modifier = Modifier.fillMaxWidth().padding(32.dp)
                    )
                    Row {
                        Button(
                            modifier = Modifier.padding(8.dp).defaultMinSize(120.dp).width(IntrinsicSize.Min),
                            onClick = { bloc.send(1) },
                            content = { Text(text = stringResource(R.string.counter_increment), textAlign = TextAlign.Center) }
                        )
                        Button(
                            modifier = Modifier.padding(8.dp).defaultMinSize(120.dp).width(IntrinsicSize.Min),
                            onClick = { bloc.send(-1) },
                            content = { Text(text = stringResource(R.string.counter_decrement), textAlign = TextAlign.Center) }
                        )
                    }
                }
            }
        }
    }

}
