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

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal

class Register(private val value: String = "") {
    private val asBigDecimal: BigDecimal
        get() = if (value.isEmpty()) BigDecimal.ZERO else value.toBigDecimal()

    override fun toString() = when {
        value.isEmpty() -> "0"
        value == "-" -> "-0"
        else -> value
    }

    fun isEmpty() = value.isEmpty()

    fun isNotEmpty() = value.isNotEmpty()

    fun isError() = try {
        asBigDecimal
        false
    } catch (e: Exception) {
        true
    }

    fun appendDigit(digit: Int) = Register(value + digit)

    fun appendPeriod() = when {
        value.contains('.') -> this
        value.isEmpty() -> Register("0.")
        else -> Register("$value.")
    }

    fun plusMinus() = Register(if (value.startsWith('-')) value.substring(1) else "-$value")

    operator fun plus(register: Register) =
        Register((asBigDecimal + register.asBigDecimal).toPlainString())

    operator fun minus(register: Register) =
        Register((asBigDecimal - register.asBigDecimal).toPlainString())

    operator fun times(register: Register) =
        Register((asBigDecimal * register.asBigDecimal).toPlainString())

    operator fun div(register: Register) =
        Register(asBigDecimal
            .divide(register.asBigDecimal, DECIMAL_MODE)
            .toPlainString()
        )

    companion object {
        private const val SCALE = 7L
        private val DECIMAL_MODE = DecimalMode(7, RoundingMode.ROUND_HALF_TO_EVEN, SCALE)
    }
}