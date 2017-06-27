package com.ssm.service;

import java.util.List;

import com.ssm.domain.Items;
import com.ssm.domain.ItemsCustom;

public interface ItemsService {
	public List<ItemsCustom> findItemsList(Items item);
	
	/*
	 * 通过id获得ItemsCustom对象
	 */
	public ItemsCustom findItemsById(Integer id);
	
	public void updateItemsCustom(ItemsCustom itemsCustom);
}
