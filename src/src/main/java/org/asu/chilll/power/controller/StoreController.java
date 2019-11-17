package org.asu.chilll.power.controller;

import java.util.List;

import org.asu.chilll.power.dataview.PurchasedItemsDataView;
import org.asu.chilll.power.dataview.StoreItemDataView;
import org.asu.chilll.power.dataview.StorePurchaseDataView;
import org.asu.chilll.power.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
public class StoreController {
	@Autowired
	private StoreService storeService;
	
	//fetch 8 store items randomly
	@RequestMapping(value = "/api/store/items", method = RequestMethod.GET)
	public @ResponseBody List<StoreItemDataView> fetchStoreItems(@RequestParam("grade") String grade) {
		try {
			List<StoreItemDataView> result = storeService.fetchStoreItems(grade);
			return result;
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	//purchase an item
	@RequestMapping(value = "/api/store/purchase", method = RequestMethod.POST)
	public @ResponseBody String updateGameProgress(@RequestBody String formData) {
		try {
			StorePurchaseDataView dv = new Gson().fromJson(formData, StorePurchaseDataView.class);
			if(storeService.purchaseAnItem(dv)) {
				return "true";
			}
			return "false";
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return "false";
		}
	}
	
	//fetch all purchased items
	@RequestMapping(value = "/api/store/purchaseditems", method = RequestMethod.GET)
	public @ResponseBody List<PurchasedItemsDataView> fetchPurchasedItems(@RequestParam("studentId") String childId, @RequestParam("grade") String grade){
		try {
			return storeService.fetchPurchasedItems(childId, grade);
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return null;
		}
	}
}