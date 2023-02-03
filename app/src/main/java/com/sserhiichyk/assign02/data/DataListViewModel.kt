package com.sserhiichyk.assign02.data

import androidx.lifecycle.ViewModel
import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class DataListViewModel : ViewModel() {

        var userList = getDataUser()

    init {

        Log.i(
            "MainActivity", "DataListViewModel init ".plus(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy: HH.mm.ss.SSS"))
            )
        )
    }


    fun arrayToPreferences(): IntArray {
        val arrayInt = ArrayList<Int>()

        userList.forEach {
            if (it.inContacts) {
                arrayInt.add(it.id)
            }
        }

        return arrayInt.toIntArray()
    }

    fun arrayCheckInit(selectList: ArrayList<UserModel>, state: Boolean) {
        selectList.forEach { it.isSelect = state }
    }

    fun arrayCheckedMove(selectList: ArrayList<UserModel>, state: Boolean): Int {
        var count = 0
        selectList.forEach {
            if (it.isSelect) {
                it.inContacts = state
                count++
            }
        }
        return count
    }

    fun resurrection() {
        userList.forEach {
            if (it.isSelect) {
                it.inContacts = !it.inContacts
            }
        }
    }


    fun setInContact(id: Int, state: Boolean) {
        if (id < getDataListSize()) {
            userList[id].inContacts = state
        }
    }

    fun setIsSelect(id: Int, state: Boolean) {
        userList[id].isSelect = state
    }

    fun addUser(user: UserModel) {
        val newList = userList

        newList.add(user)
        userList = newList
    }

    fun infoUser(id: Int): UserModel {
        return userList[id]
    }

    fun getDataListSize(): Int {
        return userList.size
    }

    fun creatUserListDel(): ArrayList<UserModel> {
        return userListAdd(true)
    }

    fun createUserListAdd(): ArrayList<UserModel> {
        return userListAdd(false)
    }

    private fun userListAdd(inContacts: Boolean): ArrayList<UserModel> {
        val users: ArrayList<UserModel> = ArrayList()

        for (i in userList.indices) {
            if (userList[i].inContacts == inContacts) {
                users.add(userList[i])
            }
        }

        return users
    }

    private fun randomDateBirth(): String {
        val randomDate: Calendar = Calendar.getInstance()
        val rand = (100..5000).random()
        randomDate.set(1995, 0, 1)
        randomDate.add(Calendar.DATE, rand)

        return randomDate.time.toString()
    }

    fun getDataUser(): ArrayList<UserModel> {
        val listOfMale: List<DataMale> = getMaleList()
        val listOfFemale: List<DataFemale> = getFemaleList()
        val listOfCareer: List<String> = getCareerList()
        val listUsers = ArrayList<UserModel>()

        for (i in listOfMale.indices) {
            listUsers.add(
                UserModel(
                    i * 2,
                    "https://xsgames.co/randomusers/assets/avatars/female/$i.jpg",
                    name = listOfFemale[i].name,
                    listOfCareer[i * 2 % listOfCareer.size],
                    listOfFemale[i].gender,
                    (i * 2) % 4,
                    listOfFemale[i].name.lowercase().replace(" ", ".").plus("@gmail.com"),
                    "",
                    "adress: ".plus(listOfFemale[i].name.substringAfter(" ").plus(" home")),
                    randomDateBirth(),
                    false,
                    false
                )
            )

            listUsers.add(
                UserModel(
                    i * 2 + 1,
                    "https://xsgames.co/randomusers/assets/avatars/male/$i.jpg",
                    listOfMale[i].name,
                    listOfCareer[(i * 2 + 1) % listOfCareer.size],
                    listOfMale[i].gender,
                    (i * 2 + 1) % 4,
                    listOfMale[i].name.lowercase().replace(" ", ".").plus("@gmail.com"),
                    "",
                    "adress: ".plus(listOfMale[i].name.substringAfter(" ").plus(" home")),
                    randomDateBirth(),
                    false,
                    false
                )
            )
        }

        return listUsers
    }


    private fun getMaleList(): List<DataMale> {
        val genderInit = -1

        return listOf(
            DataMale("Dwain Kinion", genderInit),
            DataMale("Carmelo Delice", genderInit),
            DataMale("Jess Whitledge", genderInit),
            DataMale("Laurence Cyrulik", genderInit),
            DataMale("Mervin Jun", genderInit),
            DataMale("Shirley Greving", genderInit),
            DataMale("Marvin Skeet", genderInit),
            DataMale("Garry Sockel", genderInit),
            DataMale("Lawerence Tranmer", genderInit),
            DataMale("Jamie Dombrowsky", genderInit),
            DataMale("Wayne Klam", genderInit),
            DataMale("Winford Maringer", genderInit),
            DataMale("Rudolph Goatee", genderInit),
            DataMale("Art Agurcia", genderInit),
            DataMale("Dennis Hayter", genderInit),
            DataMale("Aaron Labeau", genderInit),
            DataMale("Alfred Biondolillo", genderInit),
            DataMale("Derrick Mcfaul", genderInit),
            DataMale("Robt Nishimoto", genderInit),
            DataMale("Dong Tweten", genderInit),
            DataMale("Julian Scherschligt", genderInit),
            DataMale("Isaiah Magalong", genderInit),
            DataMale("Ferdinand Stevie", genderInit),
            DataMale("Brady Lippin", genderInit),
            DataMale("Buford Santellana", genderInit),
            DataMale("Dana Urbon", genderInit),
            DataMale("Chad Domek", genderInit),
            DataMale("Grady Poelker", genderInit),
            DataMale("Barrett Germscheid", genderInit),
            DataMale("Sidney Alejandrino", genderInit),
            DataMale("Eddie Stvil", genderInit),
            DataMale("Gerry Bomberg", genderInit),
            DataMale("Jamel Newth", genderInit),
            DataMale("Ricardo Garbrick", genderInit),
            DataMale("Rosario Seto", genderInit),
            DataMale("Lewis Dejohnette", genderInit),
            DataMale("Gregory Crummey", genderInit),
            DataMale("Gary Lystad", genderInit),
            DataMale("Andrea Cee", genderInit),
            DataMale("Conrad Philbrook", genderInit),
            DataMale("Dominique Ensey", genderInit),
            DataMale("Elisha Hennessee", genderInit),
            DataMale("Karl Baribeault", genderInit),
            DataMale("Marion Sarpy", genderInit),
            DataMale("Bill Apolinario", genderInit),
            DataMale("Ellis Avolio", genderInit),
            DataMale("Johnie Kozdron", genderInit),
            DataMale("Franklyn Gracy", genderInit)
        )
    }

    private fun getFemaleList(): List<DataFemale> {
        val genderInit = 1

        return listOf(
            DataFemale("Penney Seratte", genderInit),
            DataFemale("Leonia Rosemond", genderInit),
            DataFemale("Laurine Figliomeni", genderInit),
            DataFemale("Clemmie Denier", genderInit),
            DataFemale("Candy Brickert", genderInit),
            DataFemale("Lesley Strychalski", genderInit),
            DataFemale("Jannie Jondahl", genderInit),
            DataFemale("Kimberely Millar", genderInit),
            DataFemale("Tameka Hopka", genderInit),
            DataFemale("Hyo Heiken", genderInit),
            DataFemale("Denisha Sicre", genderInit),
            DataFemale("Becky Gabbard", genderInit),
            DataFemale("Marcella Goetsch", genderInit),
            DataFemale("Chanel Landsem", genderInit),
            DataFemale("Tamera Buzzelli", genderInit),
            DataFemale("Guadalupe Tuten", genderInit),
            DataFemale("Vallie Kurdi", genderInit),
            DataFemale("Lucile Alvarado", genderInit),
            DataFemale("Cassie Geile", genderInit),
            DataFemale("Randi Kopeck", genderInit),
            DataFemale("Kerri Hayer", genderInit),
            DataFemale("Tonja Previte", genderInit),
            DataFemale("Ayesha Crowley", genderInit),
            DataFemale("Beatrice Tomassian", genderInit),
            DataFemale("Ghislaine Waibel", genderInit),
            DataFemale("Victorina Bruess", genderInit),
            DataFemale("Myong Brillant", genderInit),
            DataFemale("Martine Even", genderInit),
            DataFemale("Nicolle Serle", genderInit),
            DataFemale("Lavonda Barthelmy", genderInit),
            DataFemale("Jeremy Sirbaugh", genderInit),
            DataFemale("Karri Maggett", genderInit),
            DataFemale("Codi Haylock", genderInit),
            DataFemale("Stormy Vanboxel", genderInit),
            DataFemale("Numbers Moulin", genderInit),
            DataFemale("Jerri Jance", genderInit),
            DataFemale("Ava Smith", genderInit),
            DataFemale("Viki Hemstreet", genderInit),
            DataFemale("Claretha Ayes", genderInit),
            DataFemale("Mafalda Schacter", genderInit),
            DataFemale("Ashely Gromis", genderInit),
            DataFemale("Theressa Caviston", genderInit),
            DataFemale("Annette Jannings", genderInit),
            DataFemale("Lorean Cardwell", genderInit),
            DataFemale("Larue Holtzclaw", genderInit),
            DataFemale("Merlyn Valence", genderInit),
            DataFemale("Silva Gevry", genderInit),
            DataFemale("Ricki Peyer", genderInit)
        )
    }

    fun getCareerList(): List<String> {
        return listOf(
            "Actress",
            "Actor",
            "Administrator",
            "Aerospace Engineer",
            "Anesthesiologist",
            "Art Director",
            "Astronomer",
            "Auditor",
            "Barista",
            "Biochemist",
            "Biologist",
            "Business Analyst",
            "Choreographer",
            "Bus Driver",
            "Cook",
            "Consultant",
            "Credit Analyst",
            "Database Administrator",
            "Photograph",
            "Dentist",
            "Designer",
            "Economist",
            "Editor",
            "Engineer",
            "Fashion Designer",
            "Financial Analyst",
            "Financial Manager",
            "Fire Fighter",
            "Geographer",
            "Geologist",
            "HR Manager",
            "Historian",
            "Librarian",
            "Logistician",
            "Graphic Designer",
            "Machinist",
            "Nurse",
            "Manager",
            "Office Clerk",
            "Pharmacist",
            "Dancer",
            "Pilot",
            "Psychiatrist",
            "Police Detective",
            "Producer",
            "Programmer",
            "Reporter",
            "Risk Manager",
            "Secretary",
            "Sculptor",
            "Travel Agent",
            "Web Administrator",
            "Veterinarian",
            "Web Developer"
        )
    }
}