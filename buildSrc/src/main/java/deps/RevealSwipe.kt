package deps

object RevealSwipe : Dependency() {

    object Versions {
        const val swipeVersion = "1.0.0"
    }

    private const val swipe = "de.charlex.compose:revealswipe:${Versions.swipeVersion}"


    override fun implementations() = listOf(
        swipe
    )
}