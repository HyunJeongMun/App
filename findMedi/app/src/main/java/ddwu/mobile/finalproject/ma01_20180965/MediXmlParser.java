package ddwu.mobile.finalproject.ma01_20180965;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class MediXmlParser {
    public enum TagType { NONE, MEDI, ADDR, DIVNAME, NAME, TEL, END, START, LAT, LNG};

    final static String TAG_ITEM = "item";
    final static String TAG_ADDR = "dutyAddr";
    final static String TAG_DIVNAME = "dutyDivName";
    final static String TAG_NAME = "dutyName";
    final static String TAG_TEL = "dutyTel1";
    final static String TAG_ENDTIME = "endTime";
    final static String TAG_STARTTIME = "startTime";
    final static String TAG_LATITUDE = "latitude";
    final static String TAG_LONGITUDE = "longitude";

    public MediXmlParser() {
    }

    public ArrayList<MediDTO> parse(String xml) {

        Log.d("MAINCC", xml);

        ArrayList<MediDTO> resultList = new ArrayList();
        MediDTO dto = null;
        String[] time = null;

        TagType tagType = TagType.NONE;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals(TAG_ITEM)){
                            dto = new MediDTO();
                            time = new String[2];
                        } else if (parser.getName().equals(TAG_ADDR)) {
                            if (dto != null) tagType = TagType.ADDR;
                        } else if (parser.getName().equals(TAG_DIVNAME)) {
                            if (dto != null) tagType = TagType.DIVNAME;
                        } else if (parser.getName().equals(TAG_NAME)) {
                            if (dto != null) tagType = TagType.NAME;
                        } else if (parser.getName().equals(TAG_TEL)) {
                            if (dto != null) tagType = TagType.TEL;
                        } else if (parser.getName().equals(TAG_ENDTIME)) {
                            if (dto != null) tagType = TagType.END;
                        } else if (parser.getName().equals(TAG_STARTTIME)) {
                            if (dto != null) tagType = TagType.START;
                        } else if (parser.getName().equals(TAG_LATITUDE)) {
                            if (dto != null) tagType = TagType.LAT;
                        } else if (parser.getName().equals(TAG_LONGITUDE)) {
                            if (dto != null) tagType = TagType.LNG;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            dto.setTime(time);
                            resultList.add(dto);

                            time = null;
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case ADDR:
                                dto.setAddress(parser.getText());
                                break;
                            case DIVNAME:
                                dto.setDivName(parser.getText());
                                break;
                            case NAME:
                                dto.setName(parser.getText());
                                break;
                            case TEL:
                                dto.setTel(parser.getText());
                                break;
                            case END:
                                time[1] = parser.getText();
                                break;
                            case START:
                                time[0] = parser.getText();
                                break;
                            case LAT:
                                dto.setLat(Double.valueOf(parser.getText()));
                                break;
                            case LNG:
                                dto.setLng(Double.valueOf(parser.getText()));
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

}
