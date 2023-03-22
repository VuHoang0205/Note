package deps

object DateTime : Dependency() {

    object Versions {
        const val date = "1.3.1"
    }

    private const val date = "com.jakewharton.threetenabp:threetenabp:${Versions.date}"

    override fun implementations() = listOf(
        date
    )
}