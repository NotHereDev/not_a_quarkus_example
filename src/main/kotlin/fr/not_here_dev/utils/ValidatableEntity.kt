@file:Suppress("unused")

package fr.not_here_dev.utils

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validation
import jakarta.validation.Validator

@JsonIgnoreProperties(value = ["valid", "violations", "violationMessages", "violationMap"])
open class ValidatableEntity: PanacheEntity() {
    private var constraintViolations: Set<ConstraintViolation<Any>>? = null
    val violations
        get() = constraintViolations ?: validate()

    private val validator: Validator
        get() = Validation.buildDefaultValidatorFactory().validator

    val valid
        get() = violations.isEmpty()

    fun validate(trowIfInvalid: Boolean = false): Set<ConstraintViolation<Any>> {
        constraintViolations = validator.validate(this)
        if (trowIfInvalid && !valid) {
            throw ConstraintViolationException(constraintViolations)
        }
        return constraintViolations!!
    }

    val violationMessages
        get() = violations.map { it.message }

    val violationMap
        get() = violations.map { it.propertyPath.toString() to it.message }.toMap()
}