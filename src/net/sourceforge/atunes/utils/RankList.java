/**
 * aTunes 1.6.0
 * Copyright (C) 2006-2007 Alex Aranda (fleax) alex.aranda@gmail.com
 *
 * http://www.atunes.org
 * http://sourceforge.net/projects/atunes
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package net.sourceforge.atunes.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class RankList implements Serializable {
	
	private static final long serialVersionUID = -4404155415144124761L;

	private ArrayList<Object> order;
	private HashMap<Object, Integer> count;
	
	public RankList() {
		order = new ArrayList<Object>();
		count = new HashMap<Object, Integer>();
	}
	
	public void addItem(Object obj) {
		if (order.contains(obj)) {
			Integer previousCount = count.get(obj);
			count.put(obj, previousCount + 1);
			moveUpOnList(obj);
		} else {
			order.add(obj);
			count.put(obj, 1);
		}
	}
	
	public int size() {
		return order.size();
	}
	
	public Integer getCount(Object obj) {
		return count.get(obj);
	}
	
	private void moveUpOnList(Object obj) {
		int index = order.indexOf(obj);
		if (index > 0) {
			int previousItemCount = count.get(order.get(index-1));
			int currentItemCount = count.get(order.get(index));
			if (previousItemCount < currentItemCount) {
				Object previous = order.get(index-1);
				Object current = order.get(index);

				order.remove(previous);
				order.remove(current);
				
				order.add(index-1, current);
				order.add(index, previous);
			
				moveUpOnList(obj);
			}
		}
	}
	
	public ArrayList getNFirstElements(int n) {
		if (n == -1)
			return order;
		else if (n <= order.size())
			return new ArrayList<Object>(order.subList(0, n));
		else
			return getNFirstElements(order.size());
	}
	
	public ArrayList<Integer> getNFirstElementCounts(int n) {
		if (n == -1)
			n = order.size();
		if (n <= order.size()) {
			ArrayList<Integer> result = new ArrayList<Integer>();
			for (int i = 0; i < n; i++) {
				result.add(count.get(order.get(i)));
			}
			return result;
		}
		return getNFirstElementCounts(order.size());
	}
}
