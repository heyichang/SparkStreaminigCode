package com.ceiec.bigdata.action;

import com.ceiec.bigdata.util.eventutil.KeywordFilter;

import java.util.List;
import java.util.Set;

/**
 * Created by heyichang on 2017/12/25.
 */
public class TestMysql {
    public static void main(String[] args) {
//        EventForeUtils eventForeUtils = new EventForeUtils();
//        EventOutput eventOutput = new EventOutput();
//        eventOutput.setId("AC516487227F69E26E0D97201F0B2C54");
//        eventOutput.setFore_warning_id(4);
//        eventOutput.setInfo_id("AC516487227F69E26E0D97201F0B2C39");
//        eventOutput.setUser_id("F0064AE43AA748B3886F7F7FE59B33F4");
//        eventOutput.setWarning_time(TimeUtils.getTime());
//
//        boolean a =false;
//        try {
//            a = eventForeUtils.saveObject(eventOutput);
//            eventForeUtils.releaseConn();
//        } catch (Exception e) {
//            e.printStackTrace();
//            eventForeUtils.releaseConn();
//        }
//        System.out.println(a);

//        Set<String> set = new HashSet();
//        set.add("haha");
//        set.add("xixi");
//        long a = System.currentTimeMillis();
//        //StringUtils.strip(set.toString(),"[]")
//        System.out.println(set.toString().replace("[","").replace("]",""));
//        long b = System.currentTimeMillis();
//        System.out.println(b-a);
//        String str = TimeUtils.getCreatingTime("Fri Jan 26 04:50:50 +0000 2018");
//        System.out.println(str);
        String[] strings = "drug,curry,kobe,full,Nintendo’ss".split(",");
        List<String> keyWordList = java.util.Arrays.asList(strings);
        KeywordFilter filter = new KeywordFilter(keyWordList);
        Set<String> set = filter.getKeyWord("The fulls names of Nintendo’s latest handheld-only console is the NewNintendo2DS XL, a title that almost requires breaking down into parts to fully understand. It’s a 2DS because it’s like a 3DS without the stereoscopic 3D. It’s XL because its screen is the same size as that of the 3DS XL, roughly 80% bigger than that of the original 2DS. And it’s New because it’s part of the same generation as the New 3DS XL: consoles with slightly more power than their predecessors, a small additional analog stick called the C-Stick, and a handful of exclusive titles (most notably Xenoblade Chronicles and Binding of Isaac: Rebirth).And yet, though the New Nintendo 2DS XL is an improvement on the Nintendo 2DS, it’s not meant to be a replacement. Of course, it’s rarely wise for a company to admit that a product they still have for sale is about to become obsolete. But Nintendo insists the New 2DS XL is just a new member of the family, not meant to push out any other, fitting somewhere between the 2DS (with slightly more power, a C-Stick, and a clamshell design) and the New3DSXL (without 3D). Got that? OK, good.FacebookTwitterPinterestThe New Nintendo 2DS XL, rear. Photograph: Jordan Erica Webber/the GuardianThe New 2DS XL looks like it’s meant for young children. With its pair of initial two-colour styles – either black and JoyCon blue, or white and Fisher Price orange – it looks less techy and more toylike, especially with its lightly ridged lid and protruding hinge. But that’s no bad thing; sometimes you want your game console to feel like something you can play with.It’s lighter than the New 3DS XL, which is perhaps its closest relative, but still sturdy, helped perhaps by the addition of a flap over the slots for games and SD cards. Other changes include a slight rearrangement of the buttons and a shorter stylus – closer to 6cm. Again, it’s all very child-friendly.As a new member to the New 3DS family, the New 2DS XL will be able to play all existing DS and 3DS games, though obviously not in stereoscopic 3D. But at a hands-on with the console in London on Thursday, there were four games available to play that will be released around the same time. Here’s a quick rundown.Dr Kawashima’s Devilish Brain Training: Can you stay focused?In recognition of current fears about the negative effects of smartphones and social media, the latest entry in the Brain Training series is themed around concentration. Devilish Calculations, for example, presents you with a series of simple mental arithmetic questions and asks you to answer the one you saw one, two, or three (and so on?) steps back. Of course, we know that the skills learned in Brain Training gamesprobably don’t actually transfer to real life, and Nintendo refrains from making any such promises, but the notion is still undeniably popular.FacebookTwitterPinterestSuper Mario Bros 2 on the New Nintendo 2DS XL. Photograph: Jordan Erica Webber/the GuardianHey! PikminAlthough Hey! Pikmin is a spin-off developed not by Nintendo but by Arzest (ofYoshi’s New Island), its first few levels suggest a fun and fairly faithful 2D interpretation of some of the ideas core to the main series. Captain Olimar has crashed his spaceship yet again on another planet populated by colourful creatures called Pikmin that will follow him around and perform tasks for him. The goal has been simplified somewhat – to gather 30,000 units of “sparklium” fuel, which apparently can be harvested from both fruit and “treasures” – but there’s still charm enough here, with funny names for the household objects that act as treasures (like the fountain pen called a “peace missile”) and cute little cut scenes for the Pikmin. And it still hurts to see them die.Ever OasisThis RPG from Koichi Ishii, who created the Mana series, seems to tell a typical story of a chosen hero’s quest to overcome some all-consuming evil. But the world and your movements through it are slightly more interesting. Combat is not turn-based, but you do have a party whose members have different attacks and abilities, which are also used to solve puzzles in dungeons. You select your party members from the last surviving oasis, which you manage in between journeys into the desert, creating shops to sell loot and keeping the place clean. The happier your oasis, the more HP you have; apparently, wonderfully, this mechanic is called Rainbow Protection.MiitopiaRemember Tomodachi Life, the game where you put Miis of everyone you knew (plus some celebrities) in an apartment block and waited for them to fall in love and have babies? Miitopia is the same principle within the loose confines of a simple RPG. All the basic elements are here, from combat to levelling to items, but as with Tomodachi Life the real fun is in encouraging relationships to develop and watching the consequences play out. Whether that’ll be worth a full-price game remains to be seen.FacebookTwitterPinterestNintendo 2DS XL portable games console in the hands of two boys. Photograph: NintendoNintendo 2DS XL: our verdictGiven that most games are playable by all members of the 2DS/3DS family, there seems to be relatively little to differentiate all the consoles now available. Purists will probably want to go with the New 3DS XL so they get all the extra hardware features and the stereoscopic effects (which can be genuinely impressive – see Super Mario 3D Land, OutRun, Kid Icarus: Uprising and the Zelda titles). Meanwhile, younger kids may be better off with the cheaper and sturdier wedge-shaped 2DS. For everyone in the middle of those two groups, especially those who don’t care about stereoscopic 3D (or physically can’t see the effect), the New 2DS XL is a great alternative.Just don’t accidentally buy this is for your child if it’s actually theNintendo Switchthey’re after. With so many pieces of hardware available in the company’s current lineup, that would be an understandable and unfortunate error", 2);
        System.out.println(set.size());
    }


}
