package com.example.peoplecounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private var count : Int = 0
    private lateinit var tvCountVal : TextView
    private var limitVal : Int = 0
    private lateinit var tvLimitVal : TextView
    private lateinit var tvWarning : TextView
    private lateinit var btnUp : ImageButton
    private lateinit var btnDown : ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCountVal = findViewById(R.id.tvCountVal)
        tvLimitVal = findViewById(R.id.tvLimitVal)
        tvWarning = findViewById(R.id.tvWarning)
        btnUp = findViewById(R.id.btnUp)
        btnDown = findViewById(R.id.btnDown)


        tvLimitVal.text = ""
        tvCountVal.text = ""
        limitVal = 5
        getUserData()
        tvLimitVal.text = limitVal.toString()

        btnUp.setOnClickListener {
            limitVal++
            tvLimitVal.text = limitVal.toString()
        }

        btnDown.setOnClickListener {
            limitVal--
            tvLimitVal.text = limitVal.toString()
        }



    }

    private fun getUserData() {

        dbref = FirebaseDatabase.getInstance().getReference("Count")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {

                    count = snapshot.getValue(Int::class.javaObjectType)!!
                    tvCountVal.text = count.toString()

                    tvWarning.text = if (count < limitVal - 2) {
                        "Normal levels of occupancy"
                    } else if (count < limitVal) {
                        "Occupancy level almost at full capacity"
                    } else if (count >= limitVal) {
                        "WARNING!!! Occupancy level has exceeded limit!"
                    } else{
                        "Limit set is too low"
                    }

                }
            }


            override fun onCancelled(error: DatabaseError) {
                Log.w("error:",error.toException())
            }


        })

    }

}