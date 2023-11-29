package com.example.myfirebaseexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.myfirebaseexample.api.FirebaseApiAdapter
import com.example.myfirebaseexample.api.response.BookResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    // Referenciar campos de las interfaz
    private lateinit var idSpinner: Spinner
    private lateinit var nameField: EditText
    private lateinit var autorField: EditText
    private lateinit var costField: EditText
    private lateinit var editorialfield: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonLoad: Button

    // Referenciar la API
    private var firebaseApi = FirebaseApiAdapter()

    // Mantener los nombres e IDs de las armas
    private var weaponList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        idSpinner = findViewById(R.id.idSpinner)
        nameField = findViewById(R.id.nameField)
        autorField = findViewById(R.id.autorField)
        editorialfield = findViewById(R.id.editorialfield)
        costField = findViewById(R.id.costField)

        buttonLoad = findViewById(R.id.buttonLoad)
        buttonLoad.setOnClickListener {
            Toast.makeText(this, "Cargando información", Toast.LENGTH_SHORT).show()
            runBlocking {
                getWeaponFromApi()
            }
        }

        buttonSave = findViewById(R.id.buttonSave)
        buttonSave.setOnClickListener {
            Toast.makeText(this, "Guardando información", Toast.LENGTH_SHORT).show()
            runBlocking {
                sendBookToApi()
            }
        }

        runBlocking {
            populateIdSpinner()
        }
    }

    private suspend fun populateIdSpinner() {
        val response = GlobalScope.async(Dispatchers.IO) {
            firebaseApi.getWeapons()
        }
        val weapons = response.await()
        weapons?.forEach { entry ->
            weaponList.add("${entry.key}: ${entry.value.name}")
        }
        val weaponAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, weaponList)
        with(idSpinner) {
            adapter = weaponAdapter
            setSelection(0, false)
            gravity = Gravity.CENTER
        }
    }

    private suspend fun getWeaponFromApi() {
        val selectedItem = idSpinner.selectedItem.toString()
        val weaponId = selectedItem.subSequence(0, selectedItem.indexOf(":")).toString()
        println("Loading ${weaponId}... ")
        val weaponResponse = GlobalScope.async(Dispatchers.IO) {
            firebaseApi.getWeapon(weaponId)
        }
        val weapon = weaponResponse.await()
        nameField.setText(weapon?.name)
        autorField.setText(weapon?.autor)
        costField.setText("${weapon?.cost}")
    }

    private suspend fun sendBookToApi() {
        val edit = editorialfield.text.toString()
        val name = nameField.text.toString()
        val autor = autorField.text.toString()
        val cost = costField.text.toString().toLong()
        val book = BookResponse(edit, name, autor, cost)
        val weaponResponse = GlobalScope.async(Dispatchers.IO) {
            firebaseApi.setWeapon(book)
        }
        val response = weaponResponse.await()
        nameField.setText(book?.name)
        autorField.setText(book?.autor)
        costField.setText("${book?.cost}")
    }
}
