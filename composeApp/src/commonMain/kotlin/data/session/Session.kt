package data.session

data class Session(
    val isSaved: Boolean,
    val intervals: Int,
    val workTime: Int,
    val restTime: Int
)