package deps

object DateTimePicker : Dependency() {

    object Versions {
        const val date = "0.7.0"
    }

    private const val date = "com.marosseleng.android:compose-material3-datetime-pickers:${Versions.date}"

    override fun implementations() = listOf(
        date,
    )
}