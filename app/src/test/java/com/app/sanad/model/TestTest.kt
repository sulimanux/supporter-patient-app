package com.app.sanad.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TestTest{

    @Test
    fun emptyUserNameReturnsFalse(){
        val result = Test.isValidName("Ahmed")
        assertThat(result).isTrue()
    }


}


