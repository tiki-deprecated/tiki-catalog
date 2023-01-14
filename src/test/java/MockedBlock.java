/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import org.springframework.security.crypto.codec.Utf8;

public class MockedBlock {

    public static byte[] get(){
        final String raw = "ý\u0000\u0001\u0004$nè\u000Eº¬{\u001D1\u009Fð\\6È),6ê,\u0010æÕéh\b\n" +
                "\u00848¯<\u00971Û®\u0002D\n" +
                "\u0013\u001D0A¢:2\u0017\u0090\u0015s±#\u00831\u0082ÒH\n" +
                "*\u0001\b\u0011ú.Ý\u000B\f\u00AD\u000EÝù\u0085ø-\u0018¡»*%º\bÏ\u0006#éS\n" +
                "nr\u0081Üð¶w©Fyhq\u008DÚÞbÆI\u0003ç°EwaYZFè=¿hsEãdÂ1\u0083@.\u0010\u0096°£\u0010Ãiù\u001F0\u0098µä\u0012m6&\u0011\u001EdáGaK\u0012ÔÂá\u0093H`¹\u008CzhOF\u00873mx\u008DïB@\u00912~Ø]ü\u0084Gr³\u0014Ob$\u008D'}9èúsðXXãëC9\u0016òZBpád\u0095wtRS\u008B¬\u0002\u008D\u0015ø\fNÆýö\u0093æô\u0001\u0099\u0099\u0080âB\u0081Äqz2ò\u0014ë°Z}Ç\u0019\u0005Ãå\u0088ÿ5\u008Câ~v¦\u000Bý½\u0004\u0001\u0001\u0004c¿vÓ\u0001\u0000 \u008A\f4.,ÁÂ±b\u001Cú\u0087:m\n" +
                "°Ñ©´ß{\u001BJÊ^c\u0099\u0087³/Ög\u0001\u0003ýÊ\u0001\u0001\u0001 $ÜÆ\u0013uï~Ì+nwb\u0099?\u0094öÑö\u0082å\u0095\u00ADYæB\t\u009D<ò\u0084\u00ADJ\u0004c¿vÊ\u0001\u0000ý\u0000\u0001\u0012\u0019s¿Û\u0090§¡HË¤)Á¨Ä\u00AD? (\u0086Ô=Ý2p\u0005\u0082P\u0016]knvË«\n" +
                "÷\u001CN\u0097njA¤i*§«F<\u0084ñ®\u0090yµkSê¦w\u0006Ì½&Ã÷y>©\u0017\u0013zY:¬ë\u0083¹¥\u0098\u0099\u00AD\u0088ÌÉ¢Ý·{ál\u0086äfÿ9°]hû]O°äÜ\u0084ó»7Rû\u001F<.áo¹\u0000ð\u0004[\u000B¦Fî.%l¡Îxå\u0098\u008C×õ\u0010ünX#þ\u0088\u008AáG\u0015O_ÿ\u0089J\u008A\u0082ì¯l\u0091\u0011Ëâ±ÓÁY\u001A]çîQ)\u00052\u0007©êc)rßÉû\u009A\u008FC\u008E\u009A6ÙUd\u0016\u0087-\u000Eur\u0003à¤ZóD¤æÓ¹\u0086ÄÇ4ï\u0013ä\u0083U\u009A\u0085YVUF4}Ê\u0007±l\u0006\u000EXY=ÁR\u0019Mº+W\u008A¯\n" +
                "h\u001B]\u009F\u0083t0ëv\"õ>\u009C\u0001\u0001)user_92e71914-5766-417e-940e-c39492cdf3df\n" +
                "data_point\u001Ecom.mytiki.example_flutter_tos9You can put the Terms of Service the user agreed to here.\u000B[\"user_id\"]ýb\u0001\u0001\u0001 $ÜÆ\u0013uï~Ì+nwb\u0099?\u0094öÑö\u0082å\u0095\u00ADYæB\t\u009D<ò\u0084\u00ADJ\u0004c¿vÒ\u0001\u0000ý\u0000\u0001\u0082:|9yÚÚ\u0097¸\u001Bø\u0004.Ô+ßU¾\u001Eç0¯v·\u0017npì×ë\u0083R5¢»Yµ\u0084\u008AÕ\u009F\u009F%]±ùøWÜOùeù\u008AÁ>qågÅ\u009CWI°B®\u0018\u0087\u0089d\u0019\u000Bà\u0095\u0014ØdR»?1+Û¼ArH=\u0014Hl\u0090\u0087\u0098U\u008FÉ0¹\u0097·TÚ\u0082ÑõåÖHgûÂ\u0095\u0098Írÿ4¶à\u0000Fy\u009Ah¦L{\t\u008F\u0001<\u000B\u0096l\u0088Y:\u0092\u0091\u001E?\u0013Ó=iA\u0088\u0019Ç\u001EMleH©N\fÖÓg7rwpõZ7\u0010A\u0013<uX\u0085XSH^d\u0092=\n" +
                " F,UÄü·âï×\u009AÚX\u0083\u0095h\u0096Ý¿\u0097Ä\u0081\u0093H§\u001DG't¬Ô×¯\u0019\u00ADâj§\u009A\u0093ë÷\u009Esl\u0018 ¦\u0096\u001Dbz\u0015ðQ]6n\u008A\u001BÌ=ò/Ý\\¾~\u009Cu3»\u00054 ôo<U\u0091\u001Bíì\u0089I.KïSKEò\u008DÔ\u0006ôct£é¿µk9e|Ó\f\u0005[\"*\"]\u0005[\"*\"]\u0001\u0000\u0001\u0000\u0001\u0000ý\\\u0001\u0001\u0001 $ÜÆ\u0013uï~Ì+nwb\u0099?\u0094öÑö\u0082å\u0095\u00ADYæB\t\u009D<ò\u0084\u00ADJ\u0004c¿vÓ\u0001\u0000ý\u0000\u0001M\n" +
                "½\u009Eïæ\u00ADtfháfå\u0011¡é\u0018êßød^\u0081\u0089\u000BÉZ:>*\u008C\u001Eëo\u001Dïoà\t8 Ø'«þõ6Ìt\u009E\u0094\u0088\u0088¿ðíJ\fû\u0010¤ÿõÇKZ\u0011\u001CëIÓÄÅ\n" +
                "L\\\u0003ÿMp'\u008125\n" +
                "5\u0092/\u0003P\u0017V\u001E¹ö =Îw,ç\u0087\u001AÂ\u0013Á\u0002ø\u0019]¼ÆF=$5\u0095Áî;+\u001D¾2÷±\u0004\u009A\u0095µ0¬ôÔ+ÍB)Á&¡áq©C\u0013âÓSD$ËO¥\u0097pØÁ\u001Cð\f\u0095wÊe\u008Fw¶«Í»ÏY\\O\u0014l\u0000\u009E\u008DV\u0095Åºø³f¤èPu(4¢\u0097J\\0\u0018Þ\u009Bce<w\u0099Q\u009BJW\u0086x\u0002N\u0016\u0094\u000F!/ùÄ5\u001D¢zmb \u008B®xoE\n" +
                "µxvi¥Ifphñ)\u009C\u0091\u0083Wo\u0094ÃbdK6. ôo<U\u0091\u001Bíì\u0089I.KïSKEò\u008DÔ\u0006ôct£é¿µk9e|Ó\u0006\u0002[]\u0002[]\u0001\u0000\u0001\u0000\u0001\u0000";

        return Utf8.encode(raw);
    }
}
