package com.example.myapplication_22apriltask2


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    // Declare the UI elements
    private lateinit var etName: AppCompatEditText
    private lateinit var etEmail: AppCompatEditText
    private lateinit var etGoalsStatus: AppCompatEditText
    private lateinit var switchGoals: SwitchCompat
    private lateinit var etDob: AppCompatEditText
    private lateinit var dobImage: AppCompatImageButton
    private lateinit var etTimeDate: AppCompatEditText
    private lateinit var timeDateImage: AppCompatImageButton
    private lateinit var ratingBar: AppCompatRatingBar
    private lateinit var btnSubmit: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize the UI elements
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etGoalsStatus = findViewById(R.id.et_goalsStatus)
        switchGoals = findViewById(R.id.switch_goals)
        etDob = findViewById(R.id.et_dob)
        dobImage = findViewById(R.id.dob_image)
        etTimeDate = findViewById(R.id.et_TimeDate)
        timeDateImage = findViewById(R.id.datetime_image)
        ratingBar = findViewById(R.id.et_rating)
        btnSubmit = findViewById(R.id.submit_button)

        // Set a listener for the switch button to display "Yes" or "No"
        switchGoals.setOnCheckedChangeListener { _, isChecked ->
            etGoalsStatus.setText(if (isChecked) "Yes" else "No")
        }
        // Get an instance of the UserDao
        val db = AppDatabase.getDatabase(application)
        val userDao = db.userDao()
        // Set a listener for the DOB image button to display a DatePickerDialog
        dobImage.setOnClickListener {
            val currentDate = Calendar.getInstance()
            val year = currentDate.get(Calendar.YEAR)
            val month = currentDate.get(Calendar.MONTH)
            val day = currentDate.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, _, monthOfYear, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, monthOfYear, dayOfMonth)
                    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                    etDob.setText(sdf.format(selectedDate.time))
                },
                year,
                month,
                day
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        // Set a listener for the time and date TextView to display a TimePickerDialog
        timeDateImage.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val year = currentTime.get(Calendar.YEAR)
            val month = currentTime.get(Calendar.MONTH)
            val dayOfMonth = currentTime.get(Calendar.DAY_OF_MONTH)
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(Calendar.MINUTE)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    val timePickerDialog = TimePickerDialog(
                        this,
                        { _, selectedHour, selectedMinute ->
                            val selectedTime = Calendar.getInstance()
                            selectedTime.set(
                                selectedYear,
                                selectedMonth,
                                selectedDayOfMonth,
                                selectedHour,
                                selectedMinute
                            )
                            val sdf = SimpleDateFormat("dd-MM-yyyy | HH:mm", Locale.US)
                            etTimeDate.setText(sdf.format(selectedTime.time))
                        },
                        hour,
                        minute,
                        false
                    )
                    timePickerDialog.show()
                },
                year,
                month,
                dayOfMonth
            )
            datePickerDialog.show()
        }

        // Set a listener for the submit button to insert user data into the Room database
        btnSubmit.setOnClickListener {
            if (etName.text!!.isNotBlank() && etEmail.text!!.isNotBlank() && etGoalsStatus.text!!.isNotBlank()
                && etDob.text!!.isNotBlank() && etTimeDate.text!!.isNotBlank() && ratingBar.rating > 0
            ) {
                // Create a new User object from the input data
                val user = User(
                    name = etName.text.toString(),
                    email = etEmail.text.toString(),
                    goalsStatus = etGoalsStatus.text.toString(),
                    dob = etDob.text.toString(),
                    timeDate = etTimeDate.text.toString(),
                    rating = ratingBar.rating
                )

                // Insert the user into the Room database
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        userDao.insert(user)
                        // Navigate to the UserListActivity
                        val intent = Intent(this@MainActivity, UserListActivity::class.java)
                        intent.putExtra("USER-DATA", user)
                        startActivity(intent)
                        finish()
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
