package com.example.more.leisu.data

class AiQuestionManager private constructor() {

    companion object {

        private var instance: AiQuestionManager? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): AiQuestionManager {
            if (instance == null) {
                instance = AiQuestionManager()
            }
            return instance!!
        }

        const val TAG = "AiQuestionManager"
    }

    var curSingleAiQuestionType = AiQuestionType.AiQuestionType1

    var curMultiAiQuestionType = AiQuestionType.AiQuestionType1

}

enum class AiQuestionType(val code:Int) {
    AiQuestionType1(0),
    AiQuestionType2(1),
    AiQuestionType3(2),
    AiQuestionType4(3);

    companion object{
        // 根据数字code查找枚举，找不到返回null
        fun fromCode(code: Int): AiQuestionType? {
            return values().firstOrNull { it.code == code }
        }
    }
}
