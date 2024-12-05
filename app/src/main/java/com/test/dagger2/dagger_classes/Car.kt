package com.test.dagger2.dagger_classes

import com.test.dagger2.MainActivity
import dagger.Component
import javax.inject.Inject

class Fuel @Inject constructor(){
    private val fuel = if(true){
        "Benzone"
    }
    else "dizel"
}

class Engine @Inject constructor(private var fuel: Fuel)

class Car @Inject constructor(private var engine: Engine)

@Component
interface DaggerComponent {
    fun getCar(): Car
    fun getEngine(): Engine
    fun getFuel(): Fuel

    fun inject(act: MainActivity)
}