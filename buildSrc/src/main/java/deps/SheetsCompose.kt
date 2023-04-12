package deps

object SheetsCompose : Dependency() {
    object Versions {
        const val sheets_compose = "v1.1.1"
    }

    private const val core = "com.airbnb.android:lottie:${Versions.sheets_compose}"
    private const val calendar = "com.airbnb.android:lottie:calendar :${Versions.sheets_compose}"
    private const val clock = "com.airbnb.android:lottie:clock :${Versions.sheets_compose}"
    private const val option = "com.airbnb.android:lottie:option :${Versions.sheets_compose}"
    private const val list = "com.airbnb.android:lottie:list :${Versions.sheets_compose}"
    private const val input = "com.airbnb.android:lottie:input :${Versions.sheets_compose}"


    override fun implementations() = listOf<String>(
        core,
        calendar,
        clock,
        option,
        list,
        input,
    )
}