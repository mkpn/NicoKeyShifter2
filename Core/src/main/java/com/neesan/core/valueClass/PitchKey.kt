package com.neesan.core.valueClass

@JvmInline
value class PitchKey(val value: Double) : Comparable<PitchKey> {

    // 絶対値に♯と♭をつけたテキストに変換する
    // 原曲キーの場合は±をつける
    fun toKeyText(): String {
        val prefix = if (value == 0.0) "±" else if (value > 0) "♯" else "♭"
        val absoluteKey = kotlin.math.abs(value).toInt()
        return "$prefix$absoluteKey"
    }

    operator fun div(other: PitchKey): PitchKey = PitchKey(this.value / other.value)
    operator fun div(other: Number): PitchKey = PitchKey(this.value / other.toDouble())

    operator fun plus(other: PitchKey): PitchKey = PitchKey(this.value + other.value)
    operator fun plus(other: Number): PitchKey = PitchKey(this.value + other.toDouble())

    operator fun minus(other: PitchKey): PitchKey = PitchKey(this.value - other.value)
    operator fun minus(other: Number): PitchKey = PitchKey(this.value - other.toDouble())

    override operator fun compareTo(other: PitchKey): Int = this.value.compareTo(other.value)
}