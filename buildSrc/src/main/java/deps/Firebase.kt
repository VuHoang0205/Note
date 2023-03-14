package deps

object Firebase : Dependency() {

    object Versions {
        const val firebaseBom = "30.3.0"
    }

    private const val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebaseBom}"
    private const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics"
    private const val firebaseAnalytics = "com.google.firebase:firebase-analytics"
    private const val firebaseAuth = "com.google.firebase:firebase-auth-ktx"
    private const val firebaseDynamicLink = "com.google.firebase:firebase-dynamic-links-ktx"
    private const val firebaseMessage = "com.google.firebase:firebase-messaging"
    private const val firebasePerformance = "com.google.firebase:firebase-perf"
    private const val firestore = "com.google.firebase:firebase-firestore-ktx"

    override fun implementations() = listOf<String>(
        firebaseCrashlytics,
        firebaseAnalytics
    )

    override fun platformImplementations() = listOf(firebaseBom)
}