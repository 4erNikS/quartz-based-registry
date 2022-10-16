package com.jetbrains.productregistry.model.persitence.enums

/**
 *  Data layer enum storing possible
 *  process statuses for each version
 */
enum class VersionProcessingStatus {
    /**
     * Version was ignored
     * for loading product- info.json
     */
    IGNORED,

    /**
     * Version information might have changed
     * due changes in checksum of release date
     * So, versions marked with that status should be
     * chosen for update
     * Also can be used to warn client
     * that information that is offered a bit outdated
     */
    NEED_UPDATE,

    /**
     * Update process in progress
     */
    UPDATING,

    /**
     * Information about version
     * is in actual state
     */
    UPLOADED
}