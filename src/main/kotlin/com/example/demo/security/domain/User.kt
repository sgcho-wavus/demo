package com.example.demo.security.domain

import com.example.demo.common.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false, length = 50)
    val username: String,

    @Column(nullable = false)
    var password: String,

    @Column(unique = true, nullable = false, length = 100)
    val email: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "role")
    val roles: MutableSet<String> = mutableSetOf("ROLE_USER")

) : BaseTimeEntity() {

    fun addRole(role: String) {
        roles.add(role)
    }

    fun removeRole(role: String) {
        roles.remove(role)
    }
}
