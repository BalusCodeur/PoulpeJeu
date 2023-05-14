package com.example.poulpejeu.games

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton

class GlassButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {
    private var isBroken = false
    private var weak = false

    private fun breakGlass() {
        isBroken = true
        isActivated = false
        this.setBackgroundColor(Color.RED);
    }

    fun init(weak: Boolean) {

        this.weak = weak
        isActivated = false
        this.setBackgroundColor(Color.GRAY);

    }

    fun reInit() {
        if (!isBroken) {
            isActivated = false
            this.setBackgroundColor(Color.GRAY);
        }
    }

    fun active() {
        if (!isBroken) {
            isActivated = true
            this.setBackgroundColor(Color.BLUE);
        }
    }

    fun discover(): Boolean {
        return if (weak) {
            breakGlass()
            false
        } else {
            active()
            true
        }
    }

}