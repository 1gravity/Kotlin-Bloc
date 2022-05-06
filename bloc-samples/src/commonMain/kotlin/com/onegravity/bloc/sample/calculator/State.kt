/* Modified from https://github.com/orbit-mvi/orbit-mvi */

/*
 * Copyright 2021 Mikołaj Leszczyński & Appmattus Limited
 * Copyright 2020 Babylon Partners Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * File modified by Mikołaj Leszczyński & Appmattus Limited
 * See: https://github.com/orbit-mvi/orbit-mvi/compare/c5b8b3f2b83b5972ba2ad98f73f75086a89653d3...main
 */

package com.onegravity.bloc.sample.calculator

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth

data class State(
    val register1: Register = Register(),
    val register2: Register = Register(),
    val operator: Operator? = null
) {
    override fun toString() = StringBuilder().apply {
        if (register2.isNotEmpty()) append(register2)
        if (operator != null) append(" ${operator.display}")
        if (register1.isNotEmpty()) append(" $register1")
    }.toString()

    companion object {
        fun error(msg: String?) = State(register1 = Register(msg ?: "Error"), register2 = Register(), operator = null)
        fun Result<State, Throwable>.mapToState() = mapBoth({ it }, { error(it.message) })
    }
}
