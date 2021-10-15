package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.core.model.UseCase
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.model.SearchOption
import com.rsicarelli.homehunt.domain.repository.PropertyRepository
import com.rsicarelli.homehunt.domain.usecase.PreviewFilterResultUseCase.Outcome
import com.rsicarelli.homehunt.domain.usecase.PreviewFilterResultUseCase.Request
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

class PreviewFilterResultUseCase(
    private val propertyRepository: PropertyRepository,
    private val filterPropertiesUseCase: FilterPropertiesUseCase
) : UseCase<Request, Outcome> {

    @OptIn(FlowPreview::class)
    override fun invoke(request: Request): Flow<Outcome> {
        return propertyRepository.getActiveProperties().filterNotNull().flatMapConcat {
            filterPropertiesUseCase.invoke(
                FilterPropertiesUseCase.Request(
                    request.searchOption,
                    it
                )
            )
        }.map { Outcome(it.properties) }
    }

    data class Request(val searchOption: SearchOption)
    data class Outcome(val properties: List<Property>)

}
