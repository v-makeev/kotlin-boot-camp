package io.rybalkinsd.kotlinbootcamp

import java.util.Random
import kotlin.math.abs

class RawProfile(val rawData: String)

enum class DataSource {
    FACEBOOK,
    VK,
    LINKEDIN
}

typealias Src = DataSource

sealed class Profile(
    var id: Long,
    var firstName: String? = null,
    var lastName: String? = null,
    var age: Int? = null,
    var Src: Src
)

val rawProfiles = listOf(
        RawProfile("""
            firstName=alice,
            age=16,
            source=facebook
            """.trimIndent()
        ),
        RawProfile("""
            firstName=Dent,
            lastName=kent,
            age=88,
            source=linkedin
            """.trimIndent()
        ),
        RawProfile("""
            firstName=alla,
            lastName=OloOlooLoasla,
            source=vk
            """.trimIndent()
        ),
        RawProfile("""
            firstName=bella,
            age=36,
            source=vk
            """.trimIndent()
        ),
        RawProfile("""
            firstName=angel,
            age=I will not tell you =),
            source=facebook
            """.trimIndent()
        ),

        RawProfile(
                """
            lastName=carol,
            source=vk
            age=49,
            """.trimIndent()
        ),
        RawProfile("""
            fisrtName=bob,
            lastName=John,
            age=47,
            source=linkedin
            """.trimIndent()
        ),
        RawProfile("""
            lastName=kent,
            fisrtName=dent,
            age=88,
            source=facebook
            """.trimIndent()
        )
)

val profileList = getProfileList(rawProfiles)

/**
 * Task #1
 * Declare classes for all data sources
 */
class FacebookProfile(id: Long) : Profile(Src = Src.FACEBOOK, id = id)
class VKProfile(id: Long) : Profile(Src = Src.VK, id = id)
class LinkedInProfile(id: Long) : Profile(Src = Src.LINKEDIN, id = id)

class ID {
    companion object {
        private val ID_List = mutableListOf<Long>()
        fun generate(): Long {
            var id = (Random().nextLong() % 123456789)
            while (id in ID_List)
                id = (Random().nextLong() % 123456789)
            id = abs(id)
            ID_List.add(id)
            return id
        }
    }
}

fun getProfileFromRawData(input: RawProfile): Profile {
    val p = when (input.rawData.toLowerCase()[input.rawData.indexOf("source=") + 7]) {
        'v' -> VKProfile(ID.generate())
        'f' -> FacebookProfile(ID.generate())
        else -> LinkedInProfile(ID.generate())
    }
    for (x in input.rawData.toLowerCase().replace("\n", "").split(',')) {
        when (x.substringBefore('=')) {
            "age" -> p.age = try {
                x.substringAfter('=').toInt()
            } catch (e: IllegalArgumentException) {
                null
            }
            "firstname" -> p.firstName = x.substringAfter('=')
            "lastname" -> p.lastName = x.substringAfter('=')
        }
    }
    return p
}

fun getProfileList(input: List<RawProfile>): List<Profile> {
    val profiles = mutableListOf<Profile>()
    input.forEach { profiles += getProfileFromRawData(it) }
    return profiles
}

/**
 * Task #2
 * Find the average age for each Src (for profiles that has age)
 *
 * TODO
 */

fun DataSource.makeAvg(): Pair<Src, Double> {
    var count = profileList.count { it.Src == this }
    val avg = profileList.fold(0) { sum, pers ->
        sum + if (pers.Src == this) {
            if (pers.age == null)
                count--
            pers.age ?: 0
        } else 0
    }.toDouble() / count
    return Pair(this, avg)
}

val avgAge: Map<Src, Double> = mapOf(Src.FACEBOOK.makeAvg(),
        Src.LINKEDIN.makeAvg(),
        Src.VK.makeAvg())
/**
 * Task #3
 * Group all user ids together with all profiles of this user.
 * We can assume users equality by : firstName & lastName & age
 *
 *
 */
val groupedProfiles: Map<Long, List<Profile>> =
        profileList.associate { it.id to profileList.fold(arrayListOf<Profile>()) { res, x -> if (x.id == it.id) res.add(x); res } }

/**
 * Here are Raw profiles to analyse
 */
