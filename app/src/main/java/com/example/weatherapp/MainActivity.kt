package com.example.weatherapp


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.appcompat.widget.SearchView
//381eca7dd427f65a3ba57dadb8d6d4e7
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        fetchWeatherData("Jaipur")
        searchCity()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun searchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (!p0.isNullOrEmpty()) {
                    fetchWeatherData(p0)
                }
                return true

            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(cityName: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(APIInterface::class.java)
        val response = retrofit.getWeatherData(cityName,"381eca7dd427f65a3ba57dadb8d6d4e7","metric")
        response.enqueue(object : Callback<WeatherApp>{
            override fun onResponse(
                p0: Call<WeatherApp?>,
                p1: Response<WeatherApp?>
            ) {

                if (p1.isSuccessful ){
                    val responseBody= p1.body()
                    val temperature = responseBody?.main?.temp.toString()
                    val humidity = responseBody?.main?.humidity
                    val pressure = responseBody?.main?.pressure
                    val windSpeed = responseBody?.wind?.speed
                    val minTemp = responseBody?.main?.temp_min
                    val maxTemp = responseBody?.main?.temp_max
                    val condition = responseBody?.weather?.firstOrNull()?.main ?:"Unknown"

                    binding.currenttemp.text= " $temperature °C "
                    binding.Humidity.text= " $humidity % "
                    binding.pressure.text= " $pressure  "
                    binding.windspeed.text= " $windSpeed m/s "
                    binding.mintemp.text= "  Min: $minTemp °C "
                    binding.maxtemp.text= "  Max: $maxTemp °C "
                    binding.Condition.text= "$condition"
                    binding.cityname.text= "$cityName"
                    changescreenpercondition(condition)

                    //Log.d("TAG", "onResponse: $temperature")
                }
            }




            override fun onFailure(
                p0: Call<WeatherApp?>,
                p1: Throwable
            ) {
                TODO("Not yet implemented")
            }

        })


        }
    private fun changescreenpercondition(condition: String) {
        when(condition){
            "Haze" ->{
                binding.root.setBackgroundResource(R.drawable.sunny_weather_page)
                binding.imageView.setBackgroundResource(R.drawable.sun)
            }

        }
    }


    }
