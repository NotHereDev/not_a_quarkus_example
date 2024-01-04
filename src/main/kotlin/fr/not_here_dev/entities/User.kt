package fr.not_here_dev.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

@Entity
class User : PanacheEntity() {
    companion object : PanacheCompanion<User>

    @NotEmpty(message = "Login can't be empty")
    @Email(message = "The email address is invalid.")
    @Column(unique = true)
    lateinit var login: String;
}