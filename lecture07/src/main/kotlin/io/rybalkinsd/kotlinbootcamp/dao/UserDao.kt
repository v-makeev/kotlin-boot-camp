package io.rybalkinsd.kotlinbootcamp.dao

import io.rybalkinsd.kotlinbootcamp.model.User
import io.rybalkinsd.kotlinbootcamp.model.toUser
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class UserDao : Dao<User> {
    override fun findById(id: Int): User? =
            transaction {
                Users.select { Users.id eq id }
                        .firstOrNull()
                        ?.toUser()
            }

    override val all: List<User>
        get() = transaction {
            Messages.selectAll().map { it.toUser() }
        }

    override fun getAllWhere(vararg conditions: String): List<User> {
        val s = conditions.associate {
            it.substringBefore('=') to it.substringAfter('=') }
        Users.select {

        }
    }

    override fun insert(t: User) {
        transaction {
            Users.insert {
                it[id] = t.id
                it[login] = t.login
            }
        }
    }
}
