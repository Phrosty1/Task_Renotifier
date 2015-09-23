package com.basilarray.taskrenotifier;

import java.util.ArrayList;
import android.view.DragEvent;
//import java.util.Collections;
//import java.util.Comparator;
//import android.widget.LinearLayout;
//import com.basilarray.pizzatoppingpicker.MainActivity.SortableTextView;

public class Util {
//    public static void sortLinearLayout(LinearLayout ll) {
//        ArrayList<SortableTextView> allItems = new ArrayList<SortableTextView>();
//        int mx = ll.getChildCount();
//        for (int i = 0; i < mx; i++)
//            allItems.add(((SortableTextView) ll.getChildAt(i)));
//        Collections.sort(allItems, new Comparator<SortableTextView>() {
//            public int compare(SortableTextView p1, SortableTextView p2) {
//                String sP1 = p1.getText().toString().toUpperCase();
//                String sP2 = p2.getText().toString().toUpperCase();
//                return sP1.compareTo(sP2);
//            }
//        });
//        ll.removeAllViews();
//        for (SortableTextView e : allItems)
//            ll.addView(e);
//    }
//    public static ArrayList<String> llToArray(LinearLayout ll) {
//        ArrayList<String> retval = new ArrayList<String>();
//        for (int i = 0; i < ll.getChildCount(); i++)
//            retval.add(((SortableTextView) ll.getChildAt(i)).getText().toString());
//        ll.getChildCount();
//        return retval;
//    }

    public static String implode(String[] arr, String chr) {
        String retval = null;

        for (String e : arr)
            if (retval == null)
                retval = e;
            else
                retval += chr + e;
        return retval;
    }

    public static String implode(String[] arr) {
        return implode(arr, ",");
    }

    //	public static String implode(ArrayList<Object> list) {
    //		String[] retval = new String[list.size()];
    //		return implode((String[]) list.toArray(retval), ",");
    //	}
    public static String implode(Object[] list) {
        return implode(list, ",");
    }
    public static String implode(Object[] list, String chr) {
        String[] retval = new String[list.length];
        for (int i = 0; i < list.length; i++)
            try {
                retval[i] = String.valueOf(list[i]);
                //retval[i] = (String) list[i];
            } catch (Exception e) {
                retval[i] = e.getMessage();
            }
        return implode(retval, chr);
    }

    public static String implode(ArrayList<String> list, String chr) {
        String[] retval = new String[list.size()];
        return implode((String[]) list.toArray(retval), chr);
    }

    public static String[] explode(String txt, String chr) {
        ArrayList<String> ret = new ArrayList<String>();
        int i = 0;
        int n = 0;
        n = txt.indexOf(chr);
        while (n >= 0) {
            ret.add(txt.substring(i, n));
            i = n + 1;
            n = txt.indexOf(chr, i);
        }
        ret.add(txt.substring(i));
        String[] retval = new String[ret.size()];
        return (String[]) ret.toArray(retval);
    }

    public static String[] listToArray(ArrayList<String> list) {
        String[] retval = new String[list.size()];
        return (String[]) list.toArray(retval);
    }

    public static String[] explode(String txt) {
        return explode(txt, ",");
    }

    public static String getTransDragEvent(int dragEvent) {
        String txt = "";
        switch (dragEvent) {
            case DragEvent.ACTION_DRAG_STARTED:
                txt += "ACTION_DRAG_STARTED";
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                txt += "ACTION_DRAG_ENTERED";
                break;

            case DragEvent.ACTION_DRAG_LOCATION:
                txt += "ACTION_DRAG_LOCATION";
                break;

            case DragEvent.ACTION_DRAG_EXITED:
                txt += "ACTION_DRAG_EXITED";
                break;

            case DragEvent.ACTION_DRAG_ENDED:
                txt += "ACTION_DRAG_ENDED";
                break;

            case DragEvent.ACTION_DROP:
                txt += "ACTION_DROP";
                break;
            default:
                txt += "Unknown action type received by OnDragListener.";
                break;
        }
        return txt;
    }

}
