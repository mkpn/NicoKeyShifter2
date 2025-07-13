package com.neesan.core


/**
 * Assertの出力をカスタムして、どのテストが引っ掛かってるかを視認しやすくするために作った関数
 * テストに成功すれば緑文字でSUCCESS、失敗すれば赤文字でFAILを出力する
 * 1つのテストメソッドで1回しかAssertを実行しない場合はtestDescription省略しても良い
 * その場合、テストメソッドの名前がdescriptionとして出力される
 *
 * stackTraceから関数名を取得するのはコストがかかるらしく、実行時間が膨らむ場合は改修を検討する
 *
 * @param testCaseDescription テストケースの説明 省略した場合はスタックトレースから呼び出し元の関数名を取得する
 * @param testFunc テストケース assertを実行する関数を想定 例: { assertEquals(1, 1) }
 */
fun runWithDescription(
    testCaseDescription: String = Thread.currentThread().stackTrace[2].methodName,
    testFunc: () -> Unit,
) {
    try {
        testFunc()
        // assertionErrorが起きなかったら緑文字で出力
        println("\u001B[32m$testCaseDescription SUCCESS!\u001B[0m")
    } catch (e: AssertionError) {
        // assertionErrorをキャッチしたら赤文字で出力
        println("\u001B[31m$testCaseDescription FAIL...\u001B[0m")
        println(e.message)
        // ちゃんとthrowしないと、こけたテストがあってもテストがパスしたことになってしまうので注意
        throw e
    }
}
