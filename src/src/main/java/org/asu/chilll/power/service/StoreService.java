package org.asu.chilll.power.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.asu.chilll.power.dataview.PurchasedItemsDataView;
import org.asu.chilll.power.dataview.StoreItemDataView;
import org.asu.chilll.power.dataview.StorePurchaseDataView;
import org.asu.chilll.power.entity.StorePurchaseHistory;
import org.asu.chilll.power.entity.StudentProfile;
import org.asu.chilll.power.entity.lookup.LookupStoreItem;
import org.asu.chilll.power.repository.StoreRepository;
import org.asu.chilll.power.repository.StudentProfileRepository;
import org.asu.chilll.power.repository.feature.LookupImportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreService {
	@Autowired
	private StoreRepository storeRepo;
	
	@Autowired
	private LookupImportRepository scriptRepo;
	
	@Autowired
	private StudentProfileRepository profileRepo;
	
	public List<StoreItemDataView> fetchStoreItems(String grade){
		List<StoreItemDataView> result = new ArrayList<StoreItemDataView>();
		
		List<LookupStoreItem> allItems = storeRepo.fetchAllItems();
		List<LookupStoreItem> selectedItems = new ArrayList<LookupStoreItem>();
		List<Integer> selectedItemsIds = new ArrayList<Integer>();
		
		if(allItems != null && allItems.size() > 0) {
			int totalCount = allItems.size();
			List<Integer> priceLevelList = generateLevelList();
			int level1EndingIndex = 0, level2EndingIndex = 0, level3EndingIndex = 0, level4EndingIndex = 0, level5EndingIndex = 0, level6EndingIndex = 0, level7EndingIndex = 0;
			for(int i = 0; i < totalCount; i++) {
				if(level1EndingIndex == 0 && allItems.get(i).getPriceLevel() == 2) {
					level1EndingIndex = i - 1;
				}else if(level2EndingIndex == 0 && allItems.get(i).getPriceLevel() == 3) {
					level2EndingIndex = i - 1;
				}else if(level3EndingIndex == 0 && allItems.get(i).getPriceLevel() == 4) {
					level3EndingIndex = i - 1;
				}else if(level4EndingIndex == 0 && allItems.get(i).getPriceLevel() == 5) {
					level4EndingIndex = i - 1;
				}else if(level5EndingIndex == 0 && allItems.get(i).getPriceLevel() == 6) {
					level5EndingIndex = i - 1;
				}else if(level6EndingIndex == 0 && allItems.get(i).getPriceLevel() == 7) {
					level6EndingIndex = i - 1;
				}else if(level7EndingIndex == 0 && allItems.get(i).getPriceLevel() == 8) {
					level7EndingIndex = i - 1;
				}
			}
			Random rand = new Random();
			for(Integer level: priceLevelList) {
				int size = selectedItemsIds.size();
				while(size == selectedItemsIds.size()) {
					if(level == 1) {
						int num = rand.nextInt(level1EndingIndex);
						if(!selectedItemsIds.contains(num)) {
							selectedItemsIds.add(num);
							selectedItems.add(allItems.get(num));
						}
					}else if(level == 2) {
						int num = rand.nextInt(level2EndingIndex - (level1EndingIndex + 1)) + level1EndingIndex + 1;
						if(!selectedItemsIds.contains(num)) {
							selectedItemsIds.add(num);
							selectedItems.add(allItems.get(num));
						}
					}else if(level == 3) {
						int num = rand.nextInt(level3EndingIndex - (level2EndingIndex + 1)) + level2EndingIndex + 1;
						if(!selectedItemsIds.contains(num)) {
							selectedItemsIds.add(num);
							selectedItems.add(allItems.get(num));
						}
					}else if(level == 4) {
						int num = rand.nextInt(level4EndingIndex - (level3EndingIndex + 1)) + level3EndingIndex + 1;
						if(!selectedItemsIds.contains(num)) {
							selectedItemsIds.add(num);
							selectedItems.add(allItems.get(num));
						}
					}else if(level == 5) {
						int num = rand.nextInt(level5EndingIndex - (level4EndingIndex + 1)) + level4EndingIndex + 1;
						if(!selectedItemsIds.contains(num)) {
							selectedItemsIds.add(num);
							selectedItems.add(allItems.get(num));
						}
					}else if(level == 6) {
						int num = rand.nextInt(level6EndingIndex - (level5EndingIndex + 1)) + level5EndingIndex + 1;
						if(!selectedItemsIds.contains(num)) {
							selectedItemsIds.add(num);
							selectedItems.add(allItems.get(num));
						}
					}else if(level == 7) {
						int num = rand.nextInt(level7EndingIndex - (level6EndingIndex + 1)) + level6EndingIndex + 1;
						if(!selectedItemsIds.contains(num)) {
							selectedItemsIds.add(num);
							selectedItems.add(allItems.get(num));
						}
					}else if(level == 8) {
						int num = rand.nextInt(totalCount - 1 - (level7EndingIndex + 1)) + level7EndingIndex + 1;
						if(!selectedItemsIds.contains(num)) {
							selectedItemsIds.add(num);
							selectedItems.add(allItems.get(num));
						}
					}
				}
			}
		}
		
		for(LookupStoreItem item: selectedItems) {
			StoreItemDataView dv = new StoreItemDataView();
			dv.setItemId(item.getItemId());
			dv.setName(item.getItemName());
			dv.setItemSrc(item.getItemSrc());
			dv.setPriceLevel(item.getPriceLevel());
			if(grade.equals("K")) {
				dv.setPrice(item.getPriceGradeK());
			}else if(grade.equals("1")) {
				dv.setPrice(item.getPriceGrade1());
			}else if(grade.equals("2")) {
				dv.setPrice(item.getPriceGrade2());
			}else if(grade.equals("3")) {
				dv.setPrice(item.getPriceGrade3());
			}else if(grade.equals("4")) {
				dv.setPrice(item.getPriceGrade4());
			}else if(grade.equals("5")) {
				dv.setPrice(item.getPriceGrade5());
			}else if(grade.equals("6")) {
				dv.setPrice(item.getPriceGrade6());
			}
			
			result.add(dv);
		}
		
		return result;
	}
	
	public Boolean purchaseAnItem(StorePurchaseDataView dv) {
		//fetch item
		LookupStoreItem lookup = storeRepo.fetchItem(dv.getPurchasedItemId());
		if(lookup == null) {
			return false;
		}
		//check child coins
		StudentProfile profile = profileRepo.fetchStudentProfile(dv.getChildId(), dv.getGrade());
		
		double itemPrice = lookup.getPriceGradeK();
		if(dv.getGrade() != null) {
			if(dv.getGrade().equals("1")) {
				itemPrice = lookup.getPriceGrade1();
			}else if(dv.getGrade().equals("2")) {
				itemPrice = lookup.getPriceGrade2();
			}else if(dv.getGrade().equals("3")) {
				itemPrice = lookup.getPriceGrade3();
			}else if(dv.getGrade().equals("4")) {
				itemPrice = lookup.getPriceGrade4();
			}else if(dv.getGrade().equals("5")) {
				itemPrice = lookup.getPriceGrade5();
			}else if(dv.getGrade().equals("6")) {
				itemPrice = lookup.getPriceGrade6();
			}
		}
		
		if(profile.getTotalCoins() == null || profile.getTotalCoins() < itemPrice) {
			//can not buy!
			return false;
		}
		//
		StorePurchaseHistory history = new StorePurchaseHistory();
		history.setChildId(dv.getChildId());
		history.setGrade(dv.getGrade());
		history.setPurchasedItemId(lookup.getItemId());
		history.setPurchasedPrice(itemPrice);
		history.setTotalCoinsBeforePurchase(profile.getTotalCoins());
		storeRepo.createItemPurchaseHistory(history);
		profile.setTotalCoins(profile.getTotalCoins() == null ? (int)itemPrice : profile.getTotalCoins() - (int)itemPrice);
		profileRepo.updateTotalCoins(profile);
		
		return true;
	}
	
	public List<PurchasedItemsDataView> fetchPurchasedItems(String childId, String grade){
		List<LookupStoreItem> lookups = storeRepo.fetchPurchasedItems(childId, grade);
		List<PurchasedItemsDataView> result = new ArrayList<PurchasedItemsDataView>();
		for(LookupStoreItem item: lookups) {
			PurchasedItemsDataView dv = new PurchasedItemsDataView();
			dv.setItemId(item.getItemId());
			dv.setItemName(item.getItemName());
			dv.setItemSrc(item.getItemSrc());
			result.add(dv);
		}
		return result;
	}
	
	public Boolean initStoreItems() {
		//check store items
		Long totalItemsCount = storeRepo.fetchTotalCount();
		if(totalItemsCount > 0) {
			return true;
		}
		
		//insert all items
		return scriptRepo.insertStoreItems();
	}
	
	private List<Integer> generateRandomLevelList(){
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 1; i <= 8; i++) {
			list.add(i);
		}
		
		List<Integer> duplicateList = new ArrayList<Integer>();
		Random rand = new Random();
		while(duplicateList.size() < 4) {
			int randomNum = rand.nextInt(8 - 1) + 1;
			if(!duplicateList.contains(randomNum)) {
				duplicateList.add(randomNum);
			}
		}
		for(int i = 0; i < duplicateList.size(); i++) {
			list.add(duplicateList.get(i));
		}
		
		Collections.sort(list);
		return list;
	}
	
	private List<Integer> generateLevelList(){
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(5);
		list.add(6);
		list.add(7);
		list.add(7);
		list.add(8);
		return list;
	}
}