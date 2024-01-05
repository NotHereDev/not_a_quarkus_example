@file:Suppress("unused")

package fr.not_here_dev.utils

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.eclipse.microprofile.openapi.annotations.media.Schema

@JsonIgnoreProperties(value = ["valid", "violations", "violationMessages", "violationMap"])
open class ValidatableEntity: PanacheEntity() {
    @Schema(hidden = true)
    private var constraintViolations: Set<ConstraintViolation<Any>>? = null

    @get:Schema(hidden = true)
    val violations
        get() = constraintViolations ?: validate()

    @get:Schema(hidden = true)
    private val validator: Validator
        get() = Validation.buildDefaultValidatorFactory().validator

    @get:Schema(hidden = true)
    val valid
        get() = violations.isEmpty()

    fun validate(trowIfInvalid: Boolean = false): Set<ConstraintViolation<Any>> {
        constraintViolations = validator.validate(this)
        if (trowIfInvalid && !valid) {
            throw ConstraintViolationException(constraintViolations)
        }
        return constraintViolations!!
    }

    @get:Schema(hidden = true)
    val violationMessages
        get() = violations.map { it.message }

    @get:Schema(hidden = true)
    val violationMap
        get() = violations.map { it.propertyPath.toString() to it.message }.toMap()
}