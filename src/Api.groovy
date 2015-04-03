@Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7')

import groovy.json.JsonSlurper
import groovyx.net.http.AsyncHTTPBuilder

/**
 * Created by kangsangmook on 15. 4. 3..
 */
class Api {
    static def http = new AsyncHTTPBuilder(
            poolSize: 4,
            uri: 'http://www.nlotto.co.kr',
            contentType: 'text/html')

    static def getNumber(def drwNo, Closure c) {

        http.get(path: '/common.do', query: [method: 'getLottoNumber', drwNo: "${drwNo}"]) { resp, msg ->
            def jsonSlurper = new JsonSlurper()
            def json = jsonSlurper.parseText(msg.toString())

            if ("fail".equals(json.returnValue)) {
                println 'no data.'
                System.exit(0)
            } else {
                Lotto lotto = new Lotto()
                lotto.drwNo = json.drwNo
                lotto.returnValue = json.returnValue
                lotto.numbers += json.drwtNo1
                lotto.numbers += json.drwtNo2
                lotto.numbers += json.drwtNo3
                lotto.numbers += json.drwtNo4
                lotto.numbers += json.drwtNo5
                lotto.numbers += json.drwtNo6
                lotto.bnusNo = json.bnusNo
                lotto.drwNoDate = json.drwNoDate
                lotto.firstWinamnt = json.firstWinamnt
                lotto.firstPrzwnerCo = json.firstPrzwnerCo
                lotto.totSellamnt = json.totSellamnt
                c.call(lotto)
            }
        }
    }
}
