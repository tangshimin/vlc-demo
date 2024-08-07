
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.runBlocking
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import java.awt.Component
import java.util.*
import kotlin.concurrent.schedule
import kotlin.system.exitProcess

const val VIDEO_URL = "https://user-images.githubusercontent.com/16540656/163662386-e82dc534-7a5a-4566-8449-fc71db51f960.mp4"
fun main() = application {
    System.setProperty("compose.interop.blending", "true")
    var isOpen by remember { mutableStateOf(true) }
    if(isOpen){
        NativeDiscovery().discover()
        Window(
            onCloseRequest = ::exitApplication,
            title = "视频播放器"
        ){
            val videoPlayerComponent by remember { mutableStateOf(createMediaPlayerComponent()) }
            var isPlaying by remember{ mutableStateOf(false)}
            var init by remember { mutableStateOf(false) }
            Box(Modifier.fillMaxSize().background(Color.Black)){
                SwingPanel(
                    background = Color.Transparent,
                    modifier = Modifier.fillMaxSize(),
                    factory = { videoPlayerComponent },
                    update = {}
                )

            }

            LaunchedEffect(VIDEO_URL){
                videoPlayerComponent.mediaPlayer().media().play(VIDEO_URL)
            }

            LaunchedEffect(Unit){
                videoPlayerComponent.mediaPlayer().events().addMediaPlayerEventListener(object : MediaPlayerEventAdapter() {
                    override fun mediaPlayerReady(mediaPlayer: MediaPlayer?) {
                        println("Ready to play")
                    }
                    override fun finished(mediaPlayer: MediaPlayer) {
                        println("playback finished")
                        isPlaying = false
                    }

                    override fun positionChanged(mediaPlayer: MediaPlayer?, newPosition: Float) {
                        println("position changed: $newPosition")
                    }
                })
            }

            DisposableEffect(Unit){
                onDispose {
                    videoPlayerComponent.mediaPlayer().release()
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        runBlocking {
            Timer("close app",false).schedule(2 * 60 * 1000){
                isOpen = false
                exitProcess(0)
            }

        }
    }
}
fun Component.mediaPlayer(): MediaPlayer {
    return when (this) {
        is CallbackMediaPlayerComponent -> mediaPlayer()
        is EmbeddedMediaPlayerComponent -> mediaPlayer()
        else -> throw IllegalArgumentException("You can only call mediaPlayer() on vlcj player component")
    }
}

fun createMediaPlayerComponent(): Component {
    NativeDiscovery().discover()
    println("开始创建视频播放器...")
    return  CallbackMediaPlayerComponent()
}
