package fr.not_here_dev.entities

import fr.not_here_dev.utils.ValidatableEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

@Entity
class User : ValidatableEntity() {
    companion object : PanacheCompanion<User>

    @NotEmpty(message = "Login can't be empty")
    @Email(message = "The email address is invalid.")
    @Column(unique = true)
    lateinit var login: String
}