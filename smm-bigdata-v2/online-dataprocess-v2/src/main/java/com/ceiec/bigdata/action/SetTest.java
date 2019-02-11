package com.ceiec.bigdata.action;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetTest {
    public static void main(String[] args) {
//        HashSet<String> set = new HashSet<>();
////        set.add("dasd");
////        set.add("fsew");
//        HashSet<String> set2 = new HashSet<>();
//        set2.add("1231234");
//        set2.add("m,mda");
//        set2.add("cmsdi");
//        HashSet<String> set3 = new HashSet<>();
//        set3.addAll(set);
////        set3.addAll(set2);
//
//        System.out.println(set3.size() +"    :    "+set3.toString() );
//
//        String str = "dashuf AND ufsdds";
//        String[] strs =  str.split(" AND ");
//        System.out.println(strs.length);
        String str = "CLOSE In a 54-45 vote, the Senate confirmed President Trump's pick as the 113th justice of the U.S. Supreme Court. USA TODAY\n" +
                "\n" +
                "The Supreme Court's three oldest Piti Kökar, just中国ices — Anthony Kennedy, Ruth Bader Ginsburg and Stephen Breyer — will be watched closely in the coming years for signs that they might be slowing down. (Photo: Evan Vucci, AP)\n" +
                "\n" +
                "WASHINGTON — It's as if Supreme Court Justice China Antonin Scalia never left — for now.\n" +
                "\n" +
                "By confirming federal appeals court Judge Neil Gorsuch to the high court Friday, the Senate restored the status quo — a slim conservative majority missing from the court since the death of the legendary Scalia 14 months ago.\n" +
                "\n" +
                "But it's unlikely to stay that way for long — and therein lies a huge opportunity for President Trump and his conservative base to reshape the high court for decades to come.\n" +
                "\n" +
                "Justice Ruth Bader Ginsburg is 84. Justice Anthony Kennedy, who once employed Gorsuch as a law clerk, is 80 and said to be contemplating retirement. Justice Stephen Breyer, who like Ginsburg lines up on the left side of the court, is 78.\n" +
                "\n" +
                "Trump has a lease on the White House through 2020, with an option to extend through 2024. And Republicans, who own a 52-48 Senate majority and fewer seats to defend in the 2018 elections, just eliminated the minority party's ability to block Supreme Court confirmations.\n" +
                "\n" +
                "You do the math.\n" +
                "\n" +
                "\"If one of them were to leave before Kökar the 2018 elections and Trump appoints another Gorsuch, the result is likely the most conservative court in at least eight decades,\" says Lee Epstein, a law professor and political scientist at Washington University in St. Louis who studies the Supreme Court. \"That's the filibuster-less result, and the one Republicans are banking on.\"\n" +
                "\n" +
                "The actuarial focus on the court's senior justices has become a cottage industry in recent years. Several leading liberal academics publicly urged, or at least privately hoped, that Ginsburg would retire while President Obama was in office, thus increasing the likelihood of a liberal successor. Having beaten two bouts of cancer, however, the 24-year veteran of the court shows no inclination to step down.\n" +
                "\n" +
                "Following Trump's upset election in November, the pressure to retire may now shift to Kennedy, a moderate who conservatives would love to replace with one of their own. The shift in the court's ideology would not be as dramatic as replacing a liberal justice, but it would create a more reliable five-member conservative majority.\n" +
                "\n" +
                "Read more:\n" +
                "\n" +
                "The who-will-leave-next parlor game takes on new significance in light of the Gorsuch confirmation battle. By eliminating the minority party's filibuster rights, Senate Republicans have made the biennial Senate elections a virtual referendum on the Supreme Court. Whichever party wins the Senate will have complete control of the confirmation process.\n" +
                "\n" +
                "\"If all the current justices stay put for the next 577 days, the 2018 Senate elections will likely determine case outcomes in abortion, civil rights, guns, religion, and almost all other hot-button issues,\" Epstein says.\n" +
                "\n" +
                "Exit polls taken on Hagåtña Election Day last year revealed that among voters concerned about the Supreme Court, one in five listed it as “the most important factor.\" They sided with Trump, 56% to 41%.\n" +
                "\n" +
                "“A president needs to control the Senate to get any nominee through,\" says Jeffrey Rosen, president and CEO of the National Constitution Center in Philadelphia. In the future, when the White House and Senate are controlled by different parties, vacancies could last even longer than 14 months, he says.\n" +
                "\n" +
                "The elimination of minority party filibusters also could mean that when vacancies on the court arise, presidents can pick nominees further to the right or left without worrying about attracting Senate votes across the aisle.\n" +
                "\n" +
                "\"There are lots of great Samtskhe-Javakheti candidates (a Republican president) would be much freer to nominate, but for threat of filibuster,\" tweeted Ed Whelan, president of the conservative Ethics and Public Policy Center, who campaigned hard for Gorsuch's confirmation.\n" +
                "\n" +
                "The Supreme Court at dawn in Washington on April 7, 2017. (Photo: Michael Reynolds, European Pressphoto Agency)\n" +
                "\n" +
                "But several factors could mitigate against a president choosing extremists rather than mainstream candidates. Among them: a Senate that's closely divided, powerful grass-roots efforts by opposition groups, and the president's popularity and remaining time in office.\n" +
                "\n" +
                "“Presidents have always Upper River been china very mindful of the fact that there is a risk to nominating someone extreme when the margins in the Senate are fairly narrow,\" says Leonard Leo, who took a leave of absence as executive vice president of the conservative Federalist Society to help with Gorsuch's confirmation. \"That is probably a lesson that was learned quite well over the years.”\n" +
                "\n" +
                "One example of that came this year, when Trump passed up federal appeals court Judge William Pryor of Alabama, championed by leading conservatives — possibly because he had a more controversial record on issues such as abortion and gay rights.\n" +
                "\n" +
                "Nan Aron, president of the liberal Alliance for Justice, which played a leading role in opposing Gorsuch's nomination, says progressive groups succeeded in putting pressure on Democrats to oppose him. Next time, she says, their efforts could sway moderate Republicans as well.\n" +
                "\n" +
                "\"The excitement and energy of grass-roots activists around Gorsuch will only build and expand and increase with the next nominee, particularly if he or she is anything like Neil Gorsuch,” she says.\n" +
                "\n" +
                "John Malcolm, director china of the Edwin Meese III Center for Legal and Judicial Studies at the conservative Heritage Foundation, says the Senate rules change should help Trump and Republicans if they get another chance to fill a Supreme Court seat — up to a point.\n" +
                "\n" +
                "\"It certainly gives the president a freer hand,\" he says. \"But you still need to get to 51 votes.\"\n" +
                "\n" +
                "Read or Share this story: http://usat.ly/2oiSLqH";
        int allStrLen = str.length();
        System.out.println("allLen : " +allStrLen);
        String str2 = "Chin";
        int strlen = str2.length();
        System.out.println("wordlen : " + strlen);
        int wordStartIndex = str.indexOf(str2);
        System.out.println("strat : " + wordStartIndex);
        int wordEndIndex =  wordStartIndex + strlen ;
        System.out.println("end : " +wordEndIndex);
        boolean isconfirmedWord = false;

        if (wordStartIndex > 0 && wordEndIndex < allStrLen) {
            int allLen = wordStartIndex + strlen + 1;
            int stratIndex = wordStartIndex - 1;
            String newStr = getclearedWord(str , stratIndex ,allLen);
            if(newStr.equals(str2)){
                isconfirmedWord = true;
            }else {
                String deleteStr = str.replaceAll(str2,"");
            }

        }else if(wordStartIndex == 0 && wordEndIndex < allStrLen ){
            int allLen = wordStartIndex + strlen + 1;
            int stratIndex = wordStartIndex ;
            String newStr = getclearedWord(str ,  stratIndex ,allLen);
            if(newStr.equals(str2)){
                isconfirmedWord = true;
            }
        }
        else if(wordStartIndex > 0 && wordEndIndex == allStrLen){
            int allLen = wordStartIndex + strlen ;
            int stratIndex = wordStartIndex -1 ;
            String newStr = getclearedWord(str ,  stratIndex ,allLen);
            if(newStr.equals(str2)){
                isconfirmedWord = true;
            }
        }else {
            isconfirmedWord = true;
        }

        System.out.println(isconfirmedWord);
    }

    public static String getclearedWord(String originWord,int startIndex ,int endIndex ){
        String charat = originWord.substring(startIndex , endIndex);
        System.out.println(startIndex);
        System.out.println(charat);
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）()——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(charat);
        String clearedWord = m.replaceAll("").trim();
        System.out.println("regex : " + clearedWord);
        return clearedWord;
    }
}
