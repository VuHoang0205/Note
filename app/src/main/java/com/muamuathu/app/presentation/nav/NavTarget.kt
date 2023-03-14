package com.muamuathu.app.presentation.nav

sealed class NavTarget(val route: String) {
    // splash
    object Splash : NavTarget("splash")

    // onboard
    object GraphOnboard : NavTarget("onboard/graph")
    object Welcome : NavTarget("onboard/welcome")
    object JournalIntro : NavTarget("onboard/journalIntro")
    object TodoIntro : NavTarget("onboard/todoIntro")

    // authentication
    object GraphAuthentication : NavTarget("authentication/graph")
    object Login : NavTarget("authentication/login")
    object LoginWithEmail : NavTarget("authentication/loginWithEmail")
    object SignUpWithEmail : NavTarget("authentication/signUpWithEmail")
    object VerifyEmailAddress : NavTarget("authentication/verifyEmailAddress/{email}")
    object FacebookSignIn : NavTarget("authentication/facebookSignIn")
    object GoogleSignIn : NavTarget("authentication/googleSignIn")

    object Account : NavTarget("account")

    object GraphNote : NavTarget("note/graph")
    object Note : NavTarget("note")
    object NoteDetail : NavTarget("note/detail/{noteId}")
    object NoteAdd : NavTarget("note/add")
    object NoteAddImage : NavTarget("note/add/image")
    object NoteAddVideo : NavTarget("note/add/video")
    object NoteCaptureImage : NavTarget("note/add/capture_image")
    object NotePickImage : NavTarget("note/add/pick_image")
    object NotePickVideo : NavTarget("note/add/pick_video")
    object NoteDrawSketch : NavTarget("note/add/draw_sketch")
    object NoteAddTags : NavTarget("note/add/tags")

    object GraphFolder : NavTarget("folder/graph")
    object Folder : NavTarget("folder")
    object FolderAdd : NavTarget("folder/add")

    object GraphTodo : NavTarget("todo/graph")
    object Todo : NavTarget("todo")


    object WebView : NavTarget("webView/{$WEB_URL_KEY}")
    companion object {
        const val WEB_URL_KEY = "url_key"
    }
}