package com.example.x.coffeetime

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.widget.EditText
import com.example.x.coffeetime.application.Injection
import com.example.x.coffeetime.application.Validator
import com.example.x.coffeetime.application.ViewModelFactory
import com.example.x.coffeetime.application.api.BindingModel.OrderQuantityModel
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTests {
    @Test
    fun provideOrderQuantity_isCorrect() {
        val orderQuantityModel= OrderQuantityModel("1","1","1")
        assertEquals(orderQuantityModel, Injection.provideOrderQuantity("1","1"))
    }

}
