package test

import model.Api
import model.Lotto


Api.getNumber(1) {
    Lotto lotto = it;
    println lotto.numbers
    assert lotto.numbers == ['10', '23', '29', '33', '37', '40']
}

