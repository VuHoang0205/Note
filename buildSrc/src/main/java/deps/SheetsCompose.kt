package deps

object SheetsCompose : Dependency() {
    object Versions {
        const val sheets_compose = "1.1.1"
    }

    private const val core = "com.maxkeppeler.sheets-compose-dialogs:core:${Versions.sheets_compose}"
    private const val calendar = "com.maxkeppeler.sheets-compose-dialogs:calendar:${Versions.sheets_compose}"
    private const val clock = "com.maxkeppeler.sheets-compose-dialogs:clock:${Versions.sheets_compose}"
    private const val option = "com.maxkeppeler.sheets-compose-dialogs:option:${Versions.sheets_compose}"
    private const val list = "com.maxkeppeler.sheets-compose-dialogs:list:${Versions.sheets_compose}"
    private const val input = "com.maxkeppeler.sheets-compose-dialogs:input:${Versions.sheets_compose}"


    override fun implementations() = listOf(
        core,
        calendar,
        clock,
        option,
        list,
        input,
    )
}