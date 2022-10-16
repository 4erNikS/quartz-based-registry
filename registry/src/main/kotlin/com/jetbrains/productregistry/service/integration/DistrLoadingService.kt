package com.jetbrains.productregistry.service.integration

import java.net.URL

/**
 * Service that handles all logic related with
 * loading, saving and extracting information about distributive
 */
interface DistrLoadingService {
    /**
     * Method that loads linux distributive
     * save it in temp file, extract info json file
     * and offers its information as string to be saved
     *
     * In future can add json-parsing to reduce storage used in db (to keep actual information)
     * But as a side effect we'll have to aggregate all data from db once we're preparing responses
     * But not sure if it's needed by task. So for now that part is skipped
     */
    fun getLinuxDistributiveInfoJson(downloadUrl: URL): String
}