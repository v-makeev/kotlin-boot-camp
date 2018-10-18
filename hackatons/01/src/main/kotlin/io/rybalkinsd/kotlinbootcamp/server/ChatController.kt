package io.rybalkinsd.kotlinbootcamp.server

import io.rybalkinsd.kotlinbootcamp.util.logger
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.time.LocalDateTime
import java.util.Queue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

@Controller
@RequestMapping("/chat")
class ChatController {
    val log = logger()
    val messages: Queue<String> = ConcurrentLinkedQueue()
    val usersOnline: MutableMap<String, String> = ConcurrentHashMap()
    var loaded = false

    @RequestMapping(
            path = ["login"],
            method = [RequestMethod.POST],
            consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE]
    )
    fun login(@RequestParam("name") name: String): ResponseEntity<String> = when {
        name.isEmpty() || name.length <= 2 -> ResponseEntity.badRequest().body("Name is too short")
        name.length > 20 -> ResponseEntity.badRequest().body("Name is too long")
        usersOnline.contains(name) -> ResponseEntity.badRequest().body("Already logged in")
        !name.all { it.isLetterOrDigit() } -> ResponseEntity.badRequest().body("Body must contain only letters or digits")
        else -> {
            usersOnline[name] = name
            messages += "[$name] logged in".also { log.info(it) }
            ResponseEntity.ok().build()
        }
    }

    /**
     *
     * Well formatted sorted list of online users
     *
     * curl -i localhost:8080/chat/online
     */
    @RequestMapping(
            path = ["online"],
            method = [RequestMethod.GET],
            produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun online(): ResponseEntity<String> = when {
        usersOnline.isEmpty() -> ResponseEntity.ok().body("Users online:\nNo users")
        else -> {
            ResponseEntity.ok().body("Users online:\n" + usersOnline.values.toList().sortedBy { it.toLowerCase() }.joinToString("\n"))
        }
    }

    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=MY_NAME"
     */
    @RequestMapping(
        path = ["logout"],
        method = [RequestMethod.POST],
        produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun delete(@RequestParam("name")name: String): ResponseEntity<String> = when {
        name.isEmpty() -> ResponseEntity.badRequest().body("Name is empty")
        name !in usersOnline.keys -> ResponseEntity.badRequest().body("Not logged")
        else -> {
            messages += "[$name] logged out".also { log.info(it) }
            usersOnline.remove(name)
            ResponseEntity.ok("You logged out")
        }
    }

    /**
     * curl -X POST -i localhost:8080/chat/say -d "name=MY_NAME&msg=Hello everyone in this chat"
     */
    @RequestMapping(
            path = ["say"],
            method = [RequestMethod.POST],
            produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun say(@RequestParam("name")name: String, @RequestParam("msg")msg: String): ResponseEntity<String> = when {
        name.isEmpty() -> ResponseEntity.badRequest().body("User not online")
        name !in usersOnline.keys -> ResponseEntity.badRequest().body("User not online")
        msg.isEmpty() -> ResponseEntity.ok().build()
        else -> {
            val time = LocalDateTime.now()
            messages += "[${time.hour}:${time.minute}:${time.second}] $name: $msg".also { log.info(it) }
            ResponseEntity.ok().build()
        }
    }

    /**
     * curl -i localhost:8080/chat/history
     */
    @RequestMapping(
            path = ["history"],
            method = [RequestMethod.GET],
            produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    @ResponseBody
    fun history(): String {
        return messages.joinToString(separator = "\n")
    }

    @RequestMapping(
            path = ["check"],
            method = [RequestMethod.GET]
    )
    fun check(@RequestParam("name") name: String) = when (name) {
        in usersOnline -> ResponseEntity.ok()
        else -> ResponseEntity.badRequest()
    }
}