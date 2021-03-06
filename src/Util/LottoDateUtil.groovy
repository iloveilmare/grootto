package util
@Grab(group='joda-time', module='joda-time', version='2.7')
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants
import org.joda.time.Weeks
/**
 * Created by kangsangmook on 15. 4. 4..
 */

//1회 (2002년 12월 7일 20시 40분) 기준
class LottoDateUtil {

    def static int getDrawNumber(int year, int month, int day) {
        DateTime dateTime = new DateTime(year, month, day, 20, 40, 0)
        DateTime firstLotto = new DateTime(2002, 12, 7, 20, 40, 0)
        int weeks = Weeks.weeksBetween(firstLotto, dateTime).getWeeks()
        return weeks + 1
    }

    def static int getDrawNumber(){
        DateTime now = DateTime.now()
        return getDrawNumber(now.year, now.monthOfYear, now.dayOfMonth)
    }

    def static int getNextDrawNumber() {
        DateTime lottoTime = getNextLottoTime()
        return getDrawNumber(lottoTime.year, lottoTime.monthOfYear, lottoTime.dayOfMonth)
    }

    def static DateTime getNextLottoTime(){
        DateTime lottoTime
        switch (DateTime.now().getDayOfWeek()){
            case DateTimeConstants.MONDAY..DateTimeConstants.FRIDAY:
                lottoTime = DateTime.now().withDayOfWeek(DateTimeConstants.SATURDAY)
                break
            case DateTimeConstants.SATURDAY..DateTimeConstants.SUNDAY:
                lottoTime = DateTime.now().plusWeeks(1).withDayOfWeek(DateTimeConstants.SATURDAY)
                break
        }
        return lottoTime
    }
}
