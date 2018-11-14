package io.rybalkinsd.kotlinbootcamp.dao

import io.rybalkinsd.kotlinbootcamp.model.Message
import io.rybalkinsd.kotlinbootcamp.model.toMessage
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class MessageDao : Dao<Message> {
    override fun findById(id: Int): Message? {
        Messages.select { Messages.id eq id }.map { it.toMessage() }
    }

    override val all: List<Message>
        get() = transaction {
            Messages.selectAll().map { it.toMessage() }
        }.toList()

    override fun getAllWhere(vararg conditions: String): List<Message> {

    }

    override fun insert(t: Message) {
        transaction {
            Messages.insert {
                it[id] = t.id
                it[user] = t.user.id
                it[time] = t.timestamp
                it[content] = t.content
            }
        }
    }

}