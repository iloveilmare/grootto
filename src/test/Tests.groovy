package test

import model.Api
import model.Lotto
import util.LottoDateUtil

//junit으로 테스트 하면 grab이 제대로 동작하지 않는다. 따라서 그냥 스크립트로 테스트한다.

//LottoDateUtil test
println "now draw is " + LottoDateUtil.getDrawNumber()
println "next draw is " + LottoDateUtil.getNextDrawNumber()

assert LottoDateUtil.getDrawNumber(2002, 12, 7) == 1
assert LottoDateUtil.getDrawNumber(2002, 12, 14) == 2
assert LottoDateUtil.getDrawNumber(2007, 4, 21) == 229
assert LottoDateUtil.getDrawNumber(2011, 5, 28) == 443
assert LottoDateUtil.getDrawNumber(2013, 7, 20) == 555
assert LottoDateUtil.getDrawNumber(2015, 2, 28) == 639
assert LottoDateUtil.getDrawNumber(2015, 4, 11) == 645

//Api test
Api.getNumber(LottoDateUtil.getDrawNumber(2002, 12, 7)) {
    Lotto lotto = it;
    println lotto.numbers
    //1회차 당첨번호
    assert lotto.numbers == ['10', '23', '29', '33', '37', '40']
}
