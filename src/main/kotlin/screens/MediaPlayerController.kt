import javazoom.jl.decoder.Bitstream
import javazoom.jl.decoder.JavaLayerException
import javazoom.jl.player.advanced.AdvancedPlayer
import javazoom.jl.player.advanced.PlaybackEvent
import javazoom.jl.player.advanced.PlaybackListener
import java.io.File
import java.io.FileInputStream

object MediaPlayerController {
    private var mediaFiles: List<File> = emptyList()
    private var currentIndex: Int = -1
    private var player: AdvancedPlayer? = null
    private var _isPlaying: Boolean = false
    private var currentSongName: String = ""
    private var currentArtistName: String = ""
    private var currentPosition: Int = 0
    private var totalFrames: Int = 0

    fun load(files: String) {
        mediaFiles = List<File>(files.split(";").size) { File(files.split(";")[it]) }
        currentIndex = -1
        initializeMediaPlayer()
    }

    private fun initializeMediaPlayer() {
        if (mediaFiles.isNotEmpty()) {
            currentIndex = 0
            playMediaAtIndex(currentIndex)
        }
    }

    private fun playMediaAtIndex(index: Int) {
        if (index >= 0 && index < mediaFiles.size) {
            val file = mediaFiles[index]
            if (file.isFile) {
                if (file.extension.toLowerCase() == "mp3") {
                    playAudioFile(file, currentPosition)
                } else {
                    // Handle other formats
                }
            }
        }
    }

    fun playAudioFile(file: File, startFrame: Int = 0) {
        try {
            val fis = FileInputStream(file)
            val bitstream = Bitstream(fis)
            var frameCount = 0
            while (bitstream.readFrame() != null) {
                frameCount++
                bitstream.closeFrame()
            }
            totalFrames = frameCount
            bitstream.close()

            player = AdvancedPlayer(FileInputStream(file))
            player?.setPlayBackListener(object : PlaybackListener() {
                override fun playbackFinished(evt: PlaybackEvent?) {
                    currentPosition = evt?.frame ?: 0
                    playNext()
                }
            })

            _isPlaying = true
            currentSongName = file.nameWithoutExtension
            currentArtistName = file.parentFile?.name ?: "Unknown Artist"

            Thread {
                try {
                    player?.play(startFrame, totalFrames)
                } catch (e: JavaLayerException) {
                    e.printStackTrace()
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun play() {
        if (!_isPlaying) {
            playMediaAtIndex(currentIndex)
        }
    }

    fun pause() {
        if (_isPlaying) {
            player?.close()
            _isPlaying = false
        }
    }

    fun togglePlayPause() {
        if (_isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun seekTo(position: Float) {
        stop()
        currentPosition = position.toInt()
        playMediaAtIndex(currentIndex)
    }

    fun getCurrentPosition(): Int {
        return currentPosition
    }

    fun playNext() {
        currentPosition = 0
        currentIndex = (currentIndex + 1) % mediaFiles.size
        playMediaAtIndex(currentIndex)
    }

    fun playPrevious() {
        currentPosition = 0
        currentIndex = if (currentIndex > 0) currentIndex - 1 else mediaFiles.size - 1
        playMediaAtIndex(currentIndex)
    }

    fun stop() {
        player?.close()
        _isPlaying = false
    }

    fun adjustVolume(isIncrease: Boolean) {
        // Volume control is not directly supported by JLayer, would require additional implementation
    }

    fun getTotalDuration(): Int {
        // Total duration calculation is not directly supported by JLayer, would require additional implementation
        return 0
    }

    fun isPlaying(): Boolean {
        return _isPlaying
    }

    fun getCurrentSongName(): String {
        return currentSongName
    }

    fun getCurrentArtistName(): String {
        return currentArtistName
    }

    fun toggleShuffle() {
        // Shuffle functionality implementation
    }

    fun playAudioFile(filePath: String) {
        // Play audio file implementation
        val file = File(filePath)
        if (file.exists() && file.isFile) {
            playAudioFile(file)
        }
    }
}
