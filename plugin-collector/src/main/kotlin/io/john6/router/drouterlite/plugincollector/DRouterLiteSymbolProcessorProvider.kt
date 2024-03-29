package io.john6.router.drouterlite.plugincollector

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider


class DRouterLiteSymbolProcessorProvider:SymbolProcessorProvider {
    /**
     * Your main logic should be in the SymbolProcessor.process() method.
     */
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return DRouterLiteSymbolProcessor(environment.codeGenerator, environment.options)
    }
}