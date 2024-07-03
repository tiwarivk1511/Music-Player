package screens

import java.io.File
import javax.sound.sampled.*

class MediaPlayerController {
    private var audioFiles: List<File> = emptyList()
    private var currentIndex: Int = -1
    private val VOLUME_STEP = 0.1f
    private var clip: Clip? = null
    private var _isPlaying: Boolean = false
    private var totalDuration: Float = 0.0f
    private var isShuffleMode: Boolean = false
    var currentSongName: String = ""

    var currentArtistName: String = ""

    private var currentPosition: Float = 0.0f


    fun load(files: List<File>) {
        audioFiles = files
        currentIndex = -1
        isShuffleMode = false
        initializeMediaPlayer()
    }

    private fun initializeMediaPlayer() {
        if (audioFiles.isNotEmpty()) {
            currentIndex = 0
            playSongAtIndex(currentIndex)
        }
    }

    private fun playSongAtIndex(index: Int) {
        if (index >= 0 && index < audioFiles.size) {
            try {
                val audioInputStream: AudioInputStream = AudioSystem.getAudioInputStream(audioFiles[index])
                val clip: Clip = AudioSystem.getClip()
                clip.open(audioInputStream)
                this.clip = clip
                this.totalDuration = (clip.microsecondLength.toFloat() / 1_000_000)
                currentPosition = 0.0f
                currentSongName = audioFiles[index].nameWithoutExtension
                currentArtistName = audioFiles[index].parentFile?.name ?: "Unknown Artist"
                _isPlaying = true
                clip.start()
                clip.addLineListener { event ->
                    if (event.type == LineEvent.Type.STOP) {
                        playNext()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun togglePlayPause() {
        if (_isPlaying) pause() else play()
    }

    fun play() {
        clip?.start()
        _isPlaying = true
    }

    fun pause() {
        clip?.stop()
        _isPlaying = false
    }

    fun stop() {
        clip?.stop()
        clip?.flush()
        clip?.framePosition = 0
        _isPlaying = false
    }

    fun toggleShuffle() {
        isShuffleMode = !isShuffleMode
    }

    fun playNext() {
        if (isShuffleMode) {
            val nextIndex = (0 until audioFiles.size).random()
            playSongAtIndex(nextIndex)
        } else {
            currentIndex = (currentIndex + 1) % audioFiles.size
            playSongAtIndex(currentIndex)
        }
    }

    fun playPrevious() {
        if (isShuffleMode) {
            val previousIndex = (0 until audioFiles.size).random()
            playSongAtIndex(previousIndex)
        } else {
            currentIndex = if (currentIndex > 0) currentIndex - 1 else audioFiles.size - 1
            playSongAtIndex(currentIndex)
        }
    }

    fun adjustVolume(isIncrease: Boolean) {
        val gainControl = clip?.let { line ->
            if (line is Clip) {
                line.getControl(FloatControl.Type.MASTER_GAIN) as? FloatControl
            } else {
                null
            }
        }

        gainControl?.let { control ->
            val currentVolume = control.value
            val newValue = if (isIncrease) {
                Math.min(currentVolume + VOLUME_STEP, control.maximum)
            } else {
                Math.max(currentVolume - VOLUME_STEP, control.minimum)
            }
            control.value = newValue
        }
    }

    fun seekTo(position: Float) {
        val newPosition = (position / 100) * totalDuration
        clip?.framePosition = (newPosition * clip?.format?.frameRate!!).toInt()
    }

    fun getCurrentPosition(): Float {
        return clip?.microsecondPosition?.toFloat()?.div(1_000_000) ?: 0.0f
    }

    fun getTotalDuration(): Float {
        return totalDuration
    }

    fun isPlaying(): Boolean {
        return _isPlaying
    }

    fun getCurrentFilePath(): String? {
        return audioFiles.getOrNull(currentIndex)?.absolutePath
    }
}
