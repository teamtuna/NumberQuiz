package dev.eastar.numberquiz.main

import android.log.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.eastar.numberquiz.data.GameResult
import dev.eastar.numberquiz.data.repo.GameRepository
import javax.inject.Inject

@HiltViewModel
class MultiViewModel @Inject constructor(gameRepository: GameRepository) : ViewModel() {
    private var tryCount: Int = 0
    private val number = gameRepository.generateRandomNumber()


    val gameResult = MutableLiveData<GameResult>()
    val gameEnd = MutableLiveData<String>()
    val tryingNumber = MutableLiveData<String>()

    val members = MutableLiveData<Array<String>>()
    val membersEmpty: LiveData<Unit> = Transformations.switchMap(members) {
        if (it.isEmpty())
            MutableLiveData(Unit)
        else
            null
    }
    val members1Player: LiveData<String> = Transformations.switchMap(members) {
        if (it.size == 1)
            MutableLiveData("멀티 게임에서는 2명 이상의 player가 필요합니다.")
        else
            null
    }


    init {
        Log.e("generateRandomNumber", number)
//        checkMembers()
    }

    fun tryNumber() {
        Log.e(tryingNumber.value, number)
        val tryingNumber = tryingNumber.runCatching {
            value?.toInt()
        }.getOrNull()
        tryingNumber ?: return

        val result = signumTest(tryingNumber)
        val lowHigh = GameResult.values()[result + 1]
        gameResult.value = lowHigh
        tryCount++
        if (lowHigh == GameResult.correct) {
            members.value ?: return
            val members = members.value!!
            val winner = members[tryCount % members.size]
            gameEnd.value = "축하합니다.\n승자는 $winner 입니다."
        }
        Log.w(gameResult.value)
    }

    @VisibleForTesting
    fun signumTest(number: Int): Int {
        return Integer.signum(number - this.number)
    }

    fun checkMembers() {
//        members.value = emptyArray()
    }

    fun setMembers(membersText: String) {
        members.value = membersText.split(",").filter { it.isNotBlank() }.toTypedArray()
    }
}

