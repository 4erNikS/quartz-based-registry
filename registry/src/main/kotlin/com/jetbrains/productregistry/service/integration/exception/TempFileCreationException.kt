package com.jetbrains.productregistry.service.integration.exception

/**
 * Separate exception class to have metrics
 * in case of error file creating temp files
 * for loading distributive
 */
class TempFileCreationException: AbstractIntegrationException()