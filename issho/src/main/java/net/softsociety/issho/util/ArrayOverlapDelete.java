package net.softsociety.issho.util;

import java.util.ArrayList;
import java.util.List;

//신승훈 * target 리스트에서 source 리스트에 있는것을 삭제후 리턴
public class ArrayOverlapDelete {
	public static List<String> arrayDelete(List<String> target, List<String> source) {
	    List<String> tmpArr = new ArrayList<>();
	    tmpArr.addAll(target);
	    for (String item : source) {
	        if (target.contains(item) == true) {
	            //일치하는 아이템을 지움
	            tmpArr.remove(item);
	        }
	    }
	    return tmpArr;
	}

}
