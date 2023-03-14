package deps

object Coroutine : Dependency() {

    object Versions {
        const val coroutines = "1.3.9"
    }

    private const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    private const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    override fun implementations() = listOf<String>(
        core,
        android
    )
}