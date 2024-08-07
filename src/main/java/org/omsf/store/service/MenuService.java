package org.omsf.store.service;

import java.util.List;

import org.omsf.store.model.Menu;

/**
 * packageName    : org.omsf.store.service
 * fileName       : MenuService
 * author         : KIMCHANGHWAN
 * date           : 2024-07-05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-07-05      KIMCHANGHWAN       
 */

public interface MenuService {
	
	Menu getMenuByMenuNo(int menuNo);
	int createMenu(Menu menu);
	void updateMenu(Menu menu);
	void deleteMenu(int MenuNo);
	
	void updateMenus(int storeNo, List<Menu> menus);
	
	// jaeeun
	void addMenu(String[] menuNames, long[] menuPrices, int storeNo);
	List<Menu> getMenusByStoreNo(int storeNo);
}
