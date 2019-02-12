package windyroad.slashfortifier;

import java.util.List;
import java.util.Map;

import windyroad.slashfortifier.main.Main;

public class ChanceGetter {
	int chance = 100;
	int punish = 0;
	boolean ifTo0 = false;
	boolean ifBreak = false;
	public ChanceGetter(int strength) {
		List<Map> strength_Chance_List = (List<Map>) Main.config.getList("ChanceSetting");	
		for(int i = 0;i<strength_Chance_List.size();i++) {
			Map map =  strength_Chance_List.get(i);
			String sl = map.get("StrengthLevel").toString();
			String pls = map.get("punish").toString();
			if(("max").equals(sl) || strength<=Integer.parseInt(sl)) {
				chance = (Integer)map.get("chance");
				if("x".equals(pls)) {
					ifTo0 = true;
				}else if("X".equals(pls)) {
					ifBreak = true;
				}else {
					punish = Integer.parseInt(pls);
				}
				break;
			}
		}
	}
}
