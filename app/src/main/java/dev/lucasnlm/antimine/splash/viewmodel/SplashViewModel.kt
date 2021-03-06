package dev.lucasnlm.antimine.splash.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dev.lucasnlm.antimine.common.level.database.models.Stats
import dev.lucasnlm.antimine.common.level.repository.IStatsRepository
import dev.lucasnlm.antimine.preferences.models.ControlStyle
import dev.lucasnlm.antimine.preferences.IPreferencesRepository
import dev.lucasnlm.external.ICloudStorageManager
import dev.lucasnlm.external.model.CloudSave

class SplashViewModel(
    private val preferencesRepository: IPreferencesRepository,
    private val statsRepository: IStatsRepository,
    private val saveCloudStorageManager: ICloudStorageManager,

) : ViewModel() {

    suspend fun migrateCloudSave(playGamesId: String) {
        saveCloudStorageManager.getSave(playGamesId)?.let { cloudSave ->
            loadCloudSave(cloudSave)
        }
    }

    private suspend fun loadCloudSave(cloudSave: CloudSave) = with(cloudSave) {
        preferencesRepository.apply {
            if (cloudSave.completeTutorial == 1) {
                completeTutorial()
            }

            completeFirstUse()
            useTheme(cloudSave.selectedTheme.toLong())
            setSquareRadius(cloudSave.squareRadius)
            setSquareMultiplier(cloudSave.squareSize)
            setCustomLongPressTimeout(cloudSave.touchTiming.toLong())
            setQuestionMark(cloudSave.questionMark != 0)
            setNoGuessingAlgorithm(cloudSave.noGuessing != 0)
            setFlagAssistant(gameAssistance != 0)
            setHapticFeedback(hapticFeedback != 0)
            setHelp(help != 0)
            setSoundEffectsEnabled(soundEffects != 0)
            setPremiumFeatures(cloudSave.premiumFeatures != 0)
            useControlStyle(ControlStyle.values()[cloudSave.controlStyle])
        }

        cloudSave.stats.mapNotNull {
            try {
                Stats(
                    uid = it["uid"]!!.toInt(),
                    duration = it["duration"]!!.toLong(),
                    mines = it["mines"]!!.toInt(),
                    victory = it["victory"]!!.toInt(),
                    width = it["width"]!!.toInt(),
                    height = it["height"]!!.toInt(),
                    openArea = it["openArea"]!!.toInt(),
                )
            } catch (e: Exception) {
                null
            }
        }.distinctBy {
            it.uid
        }.also {
            try {
                statsRepository.addAllStats(it)
            } catch (e: Exception) {
                Log.e(TAG, "Fail to insert stats on DB")
            }
        }
    }

    companion object {
        val TAG = SplashViewModel::class.simpleName
    }
}
