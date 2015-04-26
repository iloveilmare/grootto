import model.Api
import model.Lotto
@Grab(group = 'commons-lang', module = 'commons-lang', version = '2.6')
import org.apache.commons.lang.math.RandomUtils
import util.LottoDateUtil

//variables
times = 1;
xNums = [];
iNums = [];
frq = false;

//start
initCliBuilder()
executeCommand(args) { it ->
    display(it)
}

//functions
def initCliBuilder() {
    cli = new CliBuilder(usage: 'groovy grootto.groovy -[dhiwxj]')
    cli.with {
        help(longOpt: 'help', 'print this message')
        h(longOpt: 'h', 'print this message')
        i(longOpt: 'i', 'include number(1~45, separated by comma)', required: false, args: 6, valueSeparator: ',')
        w(longOpt: 'w', 'won, your money', required: false, args: 1)
        x(longOpt: 'x', 'exclude number(1~45, separated by comma)', required: false, args: 39, valueSeparator: ',')
        f(longOpt: 'f', 'show frequency', required: false)
        d(longOpt: 'd', 'show data', required: false, args: 1)
        //t(longOpt: 't', 'test', required: false, args: 1)
    }
}

def executeCommand(def args, Closure c) {
    def opts
    int drwNo;
    if ('-d' in args && args.size() == 1) {
        drwNo = LottoDateUtil.getDrawNumber()
        Api.getNumber(drwNo) {
            Lotto lotto = (Lotto) it
            c.call(lotto)
        }
    } else {
        opts = cli.parse(args)

        if (opts?.d) {
            drwNo = opts.d as int
            Api.getNumber(drwNo) {
                Lotto lotto = (Lotto) it
                c.call(lotto)
            }
        }

        if (opts?.h || opts?.help) {
            cli.usage()
            System.exit(0)
        }

        if (!opts?.d) {
            try {
                //if result is 2.37 then remove 0.37 and default value is 1.
                int w = opts.w as int
                times = w.intdiv(1000) > 0 ? w.intdiv(1000) : 1
                iNums = opts.getProperty("is")?.grep { it }.collect { it as int }.findAll { it in 1..45 }
                xNums = opts.getProperty("xs")?.grep { it }.collect { it as int }.findAll { it in 1..45 }
                frq = opts.f
            } catch (Exception e) {
                times = 1
            } finally {
                c.call(generateNumber())
            }
        }
    }
}

def generateNumber() {
    def numList
    def timesMap = [:]
    times.times { //first times is argument, that getting from cli. second one is times(Closure c){ }
        numList = iNums ? iNums.collect() : [];
        if (xNums) {
            assert numList.disjoint(xNums) //assert that 'no intersection of xNums and iNums'
            while (numList.size() < 6) {
                def num = getRandomNumber()
                if (notInExcludeListAndTempList(num, numList)) {
                    numList += num
                }
            }
        } else {
            while (numList.size() < 6) {
                def num = getRandomNumber()
                if (notInTempList(num, numList)) {
                    numList += num
                }
            }
        }
        timesMap[it] = numList
        numList = []
    }
    return timesMap
}

def notInExcludeListAndTempList(def num, def tempList) {
    if (!(num in xNums || num in tempList)) {
        return true
    } else {
        return false
    }
}

def notInTempList(def num, def tempList) {
    if (!(num in tempList)) {
        return true
    } else {
        return false
    }
}

def getRandomNumber() {
    return RandomUtils.nextInt(45) + 1
}

def display(Lotto lotto) {
    println '\n' + "${lotto.drwNo + '회 / ' + lotto.drwNoDate}"
    println "${'1등 당첨금 : ' + lotto.firstWinamnt + ' / ' + lotto.firstPrzwnerCo + '명'}"
    println "${lotto.numbers}" + ' + ' + "${lotto.bnusNo}" + '\n'
    System.exit(0)
}

def display(def resultMap) {
    if (!resultMap) {
        return
    }

    frqMap = [:]

    println '\n' + '*' * 10 + ' { lotto 6/45 } ' + '*' * 10
    println '-' * 30
    resultMap.each { key, value ->
        times = key + 1
        println "${times} > ${value.sort()} | ${value.sum()}"

        if (frq) {
            value.each {
                frqMap[it] = frqMap.get(it, 0) + 1
            }
        }
    }

    if (frq) {
        frqList = frqMap.keySet().toList()
        frqList.sort { frqMap[it] }
        println '\n' + '-' * 10 + 'frequency' + '-' * 10
        frqList.each { println "${it} : ${frqMap[it]}" }
    }

    println '-' * 30 + '\n'
    System.exit(0)
}
