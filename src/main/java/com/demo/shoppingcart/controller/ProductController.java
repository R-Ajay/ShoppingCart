package com.demo.shoppingcart.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.shoppingcart.model.Product;
import com.demo.shoppingcart.repository.ProductRepo;
import com.demo.shoppingcart.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

	private List<Product> cart = new ArrayList<>();


	// Products Page

	@GetMapping(value = "/product")
	public String getAllProduct(Model model) {

		List<Product> productList = productService.getAllProduct();
		model.addAttribute("products", productList);
		return "products/index";
	}

	@PostMapping(value = "/add", consumes = { "application/json" })
	public String addProduct(@RequestBody Product product) {

		productService.addProduct(product);
		return "products/success";
	}

	@DeleteMapping(value = "/delete")
	public String deleteProduct(@PathVariable("id") int id) {

		productService.deleteProduct(id);
		return "Deleted";
	}

	// Cart List
	// ------------------------

	@GetMapping(value = "/cart")
	public String viewCart(Model model) {
		// Add the cart and total price to the model for display in the view
		int quantity = 1;
		model.addAttribute("cart", cart);
		model.addAttribute("quantity", quantity);
		double price = totalPrice();
		model.addAttribute("totalPrice", price);
		return "cart/index";
	}

	// Add the new product to the cart

	@PostMapping(value = "/cart/add")
	public String addToCart(@RequestParam("id") int id, Model model) {
		// Add the product to the cart
		int quantity = 1;
		Product product = productService.findById(id);
		if (!cart.isEmpty()) {
			for (Product pro : cart) {
				if (pro.getId() == product.getId()) {
					quantity = pro.getQuantity() + 1;
					int price = quantity * pro.getPrice();
					pro.setQuantity(quantity);
					pro.setPrice(price);
					model.addAttribute("cart", cart);
					double p = totalPrice();
					model.addAttribute("totalPrice", p);
					return "cart/index";
				}
			}
		}
		product.setQuantity(quantity);
		cart.add(product);
		model.addAttribute("cart", cart);
		double price = totalPrice();
		model.addAttribute("totalPrice", price);

		return "cart/index";
	}

	// Update the quantity of the product

	@PostMapping(value = "/cart/updateQuantity")
	public String updateQuantity(@RequestParam(value = "id") int id, @RequestParam("quantity") int quantity,
			Model model) {

		for (Product product : cart) {
			if (product.getId() == id) {
				int price = quantity * product.getPrice();
				product.setPrice(price);
				product.setQuantity(quantity);
				model.addAttribute("cart", cart);
				// model.addAttribute("quantity", quantity);
				double p = totalPrice();
				model.addAttribute("totalPrice", p);
			}
		}

		return "cart/index";
	}
	// Calculate the totalprice

	public double totalPrice() {
		double price = 0.0;
		if (!cart.isEmpty()) {
			for (Product product : cart) {
				price += product.getPrice();
			}
		}
		return price;
	}

	// Remove the product from cart

	@PostMapping(value = "/cart/remove/{id}")
	public String removeFromCart(@PathVariable("id") int id, Model model) {
		// Find the product with the given id in the cart
		Product pr = productService.findById(id);
		Product productToRemove = null;
		for (Product product : cart) {
			if (product.getId() == id) {
				if (product.getQuantity() != 1) {
					int qty = product.getQuantity() - 1;
					int price = qty * pr.getPrice();
					product.setPrice(price);
					product.setQuantity(qty);
					break;
				} else {
					productToRemove = product;
					break;
				}
			}
		}
		// If the product is found, remove it from the cart
		if (productToRemove != null) {
			cart.remove(productToRemove);
		}
		List<Product> carts = cart;
		double price = totalPrice();
		model.addAttribute("cart", carts);
		model.addAttribute("totalPrice", price);
		return "cart/index";
	}
	
	
	//Checkout Page
	
	@PostMapping( value="/cart/checkout")
	public String checkout() {
		
		return "cart/thankyou";
	}

}
