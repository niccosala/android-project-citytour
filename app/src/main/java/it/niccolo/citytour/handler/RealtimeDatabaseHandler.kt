package it.niccolo.citytour.handler

import android.content.Context
import android.util.Log
import com.google.firebase.database.*
import it.niccolo.citytour.activity.InitActivity
import it.niccolo.citytour.entity.Spot

class RealtimeDatabaseHandler private constructor() {

    private var reference : FirebaseDatabase = FirebaseDatabase.getInstance()
    private var refSpots : DatabaseReference = reference.getReference("Spots")
    private var refVersion : DatabaseReference = reference.getReference("Version")

    private object HOLDER {
        val INSTANCE = RealtimeDatabaseHandler()
    }

    companion object {
        val instance : RealtimeDatabaseHandler by lazy { HOLDER.INSTANCE }
    }

    fun getSpots(context : Context) {
        refVersion.get().addOnSuccessListener { v ->
            val version = v.value.toString().toInt()
            val db = DatabaseHandler(context)
            val ver = db.getVersion()
            when {
                ver <= 0 -> {
                    Log.d("dev-rtdb", "Error retrieving DB version")
                    return@addOnSuccessListener
                }
                version != ver -> {
                    refSpots.get().addOnSuccessListener { s ->
                        db.clearSpots()
                        for(i in s.children) {
                            val spot = Spot(
                                i.child("name").value.toString(),
                                i.child("snippet").value.toString(),
                                i.child("lat").value.toString().toDouble(),
                                i.child("lgt").value.toString().toDouble(),
                                i.child("description").value.toString(),
                                i.child("imagePath").value.toString()
                            )
                            db.addSpot(spot)
                        }
                        db.updateVersion(version)
                        if(context is InitActivity)
                            context.goToMainActivity()
                    }.addOnFailureListener{
                        Log.d("dev-rtdb", "Error retrieving spots: $it")
                    }
                }
                else -> {
                    Log.d("dev-rtdb", "Local DB is already up to date")
                    if(context is InitActivity)
                        context.goToMainActivity()
                }
            }
        }.addOnFailureListener{
            Log.d("dev-rtdb", "Error retrieving version: $it")
        }
    }

}