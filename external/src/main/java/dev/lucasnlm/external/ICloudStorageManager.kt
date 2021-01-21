package dev.lucasnlm.external

import dev.lucasnlm.external.model.CloudSave

interface ICloudStorageManager {
    fun uploadSave(cloudSave: CloudSave)

    suspend fun getSave(playId: String): CloudSave?
}
