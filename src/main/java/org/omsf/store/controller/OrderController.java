package org.omsf.store.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.omsf.alarm.controller.AlarmHandler;
import org.omsf.store.model.Menu;
import org.omsf.store.model.Order;
import org.omsf.store.model.OrderMenu;
import org.omsf.store.model.Store;
import org.omsf.store.service.MenuService;
import org.omsf.store.service.OrderService;
import org.omsf.store.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* @packageName    : org.omsf.store.controller
* @fileName       : OrderController.java
* @author         : iamjaeeuncho
* @date           : 2024.07.12
* @description    :
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.07.12        iamjaeeuncho       최초 생성
*/

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {
	
	private final StoreService storeService;
	private final OrderService orderService;
	private final MenuService menuService;
	private final AlarmHandler alarmHandler;
    
	// jaeeun - 주문하기
	@GetMapping("/order/{storeNo}")
	public String showOrderPage(Model model,
								@PathVariable("storeNo") int storeNo) {
		Store store = storeService.getStoreByNo(storeNo);
		List<Menu> menus = menuService.getMenusByStoreNo(storeNo);
		
		model.addAttribute("store", store);
		model.addAttribute("menus", menus);
	    return "order/submitOrder";
	}
	
	// jaeeun - 주문내역저장
	@PostMapping("/order/{storeNo}/submit")
	public String submitOrder(Model model,
							@ModelAttribute Order order, 
							@PathVariable("storeNo") int storeNo,
							@RequestParam("pickupDate") String pickupDate,
							@RequestParam("pickupTime") String pickupTime,
							@RequestParam("totalPrice") int totalPrice,
							@RequestParam("paymethod") String paymethod,
							@RequestParam("memo") String memo,
							@RequestParam("menuNames") String[] menuNames,
	                        @RequestParam("prices") int[] prices,
	                        @RequestParam("quantities") int[] quantities,
							Principal principal) {
        
		String username = principal.getName();
		
		LocalDateTime pickupDateTime = LocalDateTime.parse(pickupDate + "T" + pickupTime);
		Timestamp reservedat = Timestamp.valueOf(pickupDateTime);

		order.setStoreno(storeNo);
		order.setUsername(username);
		order.setTotalprice(totalPrice);
		order.setReservedat(reservedat);
		order.setPaymethod(paymethod);
		order.setMemo(memo);

		orderService.saveOrder(order);
		
		int orderNo = order.getOrderno();

	    for (int i = 0; i < menuNames.length; i++) {
	        OrderMenu orderMenu = new OrderMenu();
	        orderMenu.setOrderno(orderNo);
	        orderMenu.setOrdername(menuNames[i]);
	        orderMenu.setOrderprice(prices[i]);
	        orderMenu.setOrderquantity(quantities[i]);

	        orderService.saveOrderMenu(orderMenu);
	    }	    

	    model.addAttribute("order", order);
		String httpMapping = "http://localhost:8080/order/"+storeNo+"/"+orderNo;
		log.info("httpMapping: {}", httpMapping);
		alarmHandler.sendRequestAlarm(username, storeNo, httpMapping);

		return "redirect:/order/{storeNo}/" + orderNo;
    }
	
	// jaeeun - 주문진행상황
	@GetMapping("/order/{storeNo}/{orderNo}")
	public String showOrder(Model model,
							@PathVariable("storeNo") int storeNo,
							@PathVariable("orderNo") int orderNo) {
        
		Store store = storeService.getStoreByNo(storeNo);
		Order order = orderService.getOrderByNo(orderNo);
		List<OrderMenu> menus = orderService.getOrderMenuByNo(orderNo);
		
		model.addAttribute("store", store);
		model.addAttribute("order", order);
		model.addAttribute("menus", menus);
	    
        return "order/statusOrder";
    }
	
	@PutMapping("/order/{storeNo}/{orderNo}/approve")
    public ResponseEntity<String> approveOrder(@PathVariable("storeNo") int storeNo,
                                               @PathVariable("orderNo") int orderNo,
                                               @RequestBody Map<String, String> request) {
        orderService.updateOrderApproval(orderNo, "O");
        String username = request.get("orderUsername");
		String httpMapping = "http://localhost:8080/order/"+storeNo+"/"+orderNo;
		alarmHandler.sendCompleteAlarm(username, storeNo, httpMapping);
        return ResponseEntity.ok("주문 승인이 완료되었습니다");
    }
	
	@PutMapping("/order/{storeNo}/{orderNo}/reject")
    public ResponseEntity<String> rejectOrder(@PathVariable("storeNo") int storeNo,
                                               @PathVariable("orderNo") int orderNo) {
        orderService.updateOrderApproval(orderNo, "X");
        
        return ResponseEntity.ok("주문 거절이 완료되었습니다");
    }

	@PostMapping("/order/{storeNo}/{orderNo}/pay")
    public String payOrder(@PathVariable String storeNo, 
    										@PathVariable String orderNo, 
    										@RequestBody Map<String, String> paymentDetails) {
        String paystatus = paymentDetails.get("paystatus");
        String paidat = paymentDetails.get("paidat");

        LocalDateTime paidAtDateTime = LocalDateTime.parse(paidat, DateTimeFormatter.ISO_DATE_TIME);

        orderService.updatePayStatus(storeNo, orderNo, paystatus, paidAtDateTime);
        
        return "redirect:/order/{storeNo}/{orderNo}";
    }
	
	@PutMapping("/order/{storeNo}/{orderNo}/pickup")
    public ResponseEntity<String> pickupOrder(@PathVariable("storeNo") int storeNo,
                                               @PathVariable("orderNo") int orderNo,
                                               @RequestBody Map<String, String> pickupDetails) {
		String pickupat = pickupDetails.get("pickupat");
		
		LocalDateTime pickupAtDateTime = LocalDateTime.parse(pickupat, DateTimeFormatter.ISO_DATE_TIME);
		
        orderService.updateOrderPickup(orderNo, pickupAtDateTime);
        
        return ResponseEntity.ok("주문 거절이 완료되었습니다");
    }
}

