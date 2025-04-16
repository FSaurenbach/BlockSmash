import kotlin.properties.*

var allBlockTypes: List<Array<Array<Int>>> by Delegates.notNull()

// === SINGLE BLOCKS ===
val ONE_BY_ONE = arrayOf(
    arrayOf(1, 0, 0), arrayOf(0, 0, 0), arrayOf(0, 0, 0)
)

val ONE_BY_TWO = arrayOf(
    arrayOf(1, 0, 0), arrayOf(1, 0, 0), arrayOf(0, 0, 0)
)

val TWO_BY_ONE = arrayOf(
    arrayOf(1, 1, 0), arrayOf(0, 0, 0), arrayOf(0, 0, 0)
)

val ONE_BY_THREE = arrayOf(
    arrayOf(1, 0, 0), arrayOf(1, 0, 0), arrayOf(1, 0, 0)
)

val THREE_BY_ONE = arrayOf(
    arrayOf(1, 1, 1), arrayOf(0, 0, 0), arrayOf(0, 0, 0)
)

val ONE_BY_FOUR = arrayOf(
    arrayOf(1, 0, 0), arrayOf(1, 0, 0), arrayOf(1, 0, 0) // cut off after 3 (fits 3x3)
)

val FOUR_BY_ONE = arrayOf(
    arrayOf(1, 1, 1), arrayOf(0, 0, 0), arrayOf(0, 0, 0) // cut off after 3
)

val ONE_BY_FIVE = ONE_BY_THREE // same logic
val FIVE_BY_ONE = THREE_BY_ONE

val TWO_BY_TWO = arrayOf(
    arrayOf(1, 1, 0), arrayOf(1, 1, 0), arrayOf(0, 0, 0)
)

val THREE_BY_THREE = arrayOf(
    arrayOf(1, 1, 1), arrayOf(1, 1, 1), arrayOf(1, 1, 1)
)

val L_2X2_0 = arrayOf(
    arrayOf(1, 0, 0), arrayOf(1, 1, 0), arrayOf(0, 0, 0)
)

val L_2X2_90 = arrayOf(
    arrayOf(1, 1, 0), arrayOf(0, 1, 0), arrayOf(0, 0, 0)
)

val L_2X2_180 = L_2X2_90
val L_2X2_270 = L_2X2_0

val L_2X3_0 = arrayOf(
    arrayOf(1, 0, 0), arrayOf(1, 0, 0), arrayOf(1, 1, 0)
)

val L_2X3_90 = arrayOf(
    arrayOf(0, 0, 1), arrayOf(1, 1, 1), arrayOf(0, 0, 0)
)

val L_2X3_180 = arrayOf(
    arrayOf(1, 1, 0), arrayOf(0, 1, 0), arrayOf(0, 1, 0)
)

val L_2X3_270 = arrayOf(
    arrayOf(1, 1, 1), arrayOf(1, 0, 0), arrayOf(0, 0, 0)
)

val L_3X3_0 = arrayOf(
    arrayOf(1, 0, 0), arrayOf(1, 0, 0), arrayOf(1, 1, 1)
)

val L_3X3_90 = arrayOf(
    arrayOf(1, 1, 1), arrayOf(0, 0, 1), arrayOf(0, 0, 1)
)

val L_3X3_180 = arrayOf(
    arrayOf(1, 1, 1), arrayOf(0, 0, 1), arrayOf(0, 0, 1)
)

val L_3X3_270 = arrayOf(
    arrayOf(1, 1, 1), arrayOf(1, 0, 0), arrayOf(1, 0, 0)
)

val T_2X3_0 = arrayOf(
    arrayOf(1, 1, 1), arrayOf(0, 1, 0), arrayOf(0, 0, 0)
)

val T_2X3_90 = arrayOf(
    arrayOf(0, 1, 0), arrayOf(1, 1, 0), arrayOf(0, 1, 0)
)

val T_2X3_180 = arrayOf(
    arrayOf(0, 1, 0), arrayOf(1, 1, 1), arrayOf(0, 0, 0)
)

val T_2X3_270 = arrayOf(
    arrayOf(1, 0, 0), arrayOf(1, 1, 0), arrayOf(1, 0, 0)
)

val TWO_BY_THREE_0 = arrayOf(
    arrayOf(1, 1, 1), arrayOf(1, 1, 1), arrayOf(0, 0, 0)
)

val TWO_BY_THREE_90 = arrayOf(
    arrayOf(1, 1, 0), arrayOf(1, 1, 0), arrayOf(1, 1, 0)
)

val THREE_BY_TWO_0 = TWO_BY_THREE_90
val THREE_BY_TWO_90 = TWO_BY_THREE_0

val S_2X3_0 = arrayOf(
    arrayOf(0, 1, 1), arrayOf(1, 1, 0), arrayOf(0, 0, 0)
)

val S_2X3_90 = arrayOf(
    arrayOf(1, 0, 0), arrayOf(1, 1, 0), arrayOf(0, 1, 0)
)

val S_2X3_180 = S_2X3_0
val S_2X3_270 = S_2X3_90


// === INIT FUNCTION ===
fun initBlockTypes() {
    allBlockTypes = listOf(
        ONE_BY_ONE,
        ONE_BY_TWO,
        TWO_BY_ONE,
        ONE_BY_THREE,
        THREE_BY_ONE,
        ONE_BY_FOUR,
        FOUR_BY_ONE,
        ONE_BY_FIVE,
        FIVE_BY_ONE,
        TWO_BY_TWO,
        THREE_BY_THREE,
        L_2X2_0,
        L_2X2_90,
        L_2X2_180,
        L_2X2_270,
        L_2X3_0,
        L_2X3_90,
        L_2X3_180,
        L_2X3_270,
        L_3X3_0,
        L_3X3_90,
        L_3X3_180,
        L_3X3_270,
        T_2X3_0,
        T_2X3_90,
        T_2X3_180,
        T_2X3_270,
        TWO_BY_THREE_0,
        TWO_BY_THREE_90,
        THREE_BY_TWO_0,
        THREE_BY_TWO_90,
        S_2X3_0,
        S_2X3_90,
        S_2X3_180,
        S_2X3_270
    )
}
