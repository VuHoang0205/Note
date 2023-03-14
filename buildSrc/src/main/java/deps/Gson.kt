package deps

object Gson : Dependency() {
    object Versions {
        const val gson = "2.8.8"
    }

    private const val gson = "com.google.code.gson:gson:${Versions.gson}"

    override fun implementations() = listOf<String>(
        gson
    )
}