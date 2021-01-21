package dev.lucasnlm.external

import dev.lucasnlm.external.model.CloudSave

class CloudStorageManager : ICloudStorageManager {
    override fun uploadSave(cloudSave: CloudSave) {
        // FOSS build doesn't support cloud save.
    }

    override suspend fun getSave(playId: String): CloudSave? {
        // FOSS build doesn't support cloud save.
        return null
    }
}
