package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.exceptions.APIException;
import com.ecommerce.Ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.Ecom.model.Cart;
import com.ecommerce.Ecom.model.CartItem;
import com.ecommerce.Ecom.model.Product;
import com.ecommerce.Ecom.payload.CartDto;
import com.ecommerce.Ecom.payload.CartItemDto;
import com.ecommerce.Ecom.payload.ProductDto;
import com.ecommerce.Ecom.repositories.CartItemRepository;
import com.ecommerce.Ecom.repositories.CartRepository;
import com.ecommerce.Ecom.repositories.ProductRepository;
import com.ecommerce.Ecom.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CartDto addProductToCart(Long productId, Integer quantity) {
        Cart cart  = createCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the cart");
        }

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItem newCartItem = new CartItem();

        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity());

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        cartRepository.save(cart);

        CartDto cartDTO = modelMapper.map(cart, CartDto.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDto> productStream = cartItems.stream().map(item -> {
            ProductDto map = modelMapper.map(item.getProduct(), ProductDto.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productStream.toList());

        return cartDTO;
    }

    @Override
    public List<CartDto> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if (carts.size() == 0) {
            throw new APIException("No cart exists");
        }

        List<CartDto> cartDTOs = carts.stream().map(cart -> {
            CartDto cartDTO = modelMapper.map(cart, CartDto.class);

            List<ProductDto> products = cart.getCartItems().stream().map(cartItem -> {
                ProductDto productDTO = modelMapper.map(cartItem.getProduct(), ProductDto.class);


                 productDTO.setQuantity(cartItem.getQuantity()); // Set the quantity from CartItem
                return productDTO;
            }).collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;

        }).collect(Collectors.toList());

        return cartDTOs;
    }

    @Override
    public CartDto getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null){
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }
        CartDto cartDTO = modelMapper.map(cart, CartDto.class);
        cart.getCartItems().forEach(c ->
                c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDto> products = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDto.class))
                .toList();
        cartDTO.setProducts(products);
        return cartDTO;
    }

    @Transactional
    @Override
    public CartDto UpdateProductQuantityInCart(Long productId, Integer quantity) {

        String emailId= authUtil.loggedInEmail();

        Cart userCart=cartRepository.findCartByEmail(emailId
        );

        Long cartId= userCart.getCartId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));




        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }



    //passing both because we want product having the cartId
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);

        if(cartItem==null){
            throw new APIException("Product"+product.getProductName()+"not available in the cart!!"
            );



        }

        int newQantity=cartItem.getQuantity();

        if(newQantity<0){
            throw new APIException("The resulting Quantity cant be negative !");
        }

        if(newQantity==0){
            deleteProductFromCart(cartId,productId);
        }else{
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity()+quantity);
            cartItem.setDiscount(cartItem.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice()+cartItem.getProductPrice()*quantity);

            cartRepository.save(cart);
        }

        //cartItem is the product that exist in the cart
         CartItem updatedItem=cartItemRepository.save(cartItem);

         if(updatedItem.getQuantity()==0){
             cartItemRepository.deleteById(updatedItem.getCartItemId());
         }

         //we update the cartDto for cart values
         CartDto cartDto=modelMapper.map(cart,CartDto.class);

         List<CartItem> cartItems=cart.getCartItems();

//now we will updated the product in the cartItem and convert it into the dto that we have in the List<CartItem>

        Stream<ProductDto>productStream=cartItems.stream().map(item-> {
            //converting every product in to the productDto class
            ProductDto prd = modelMapper.map(item.getProduct(),ProductDto.class
            );
            prd.setQuantity(item.getQuantity());
           return prd;
        });


            cartDto.setProducts(productStream.toList());
        return cartDto;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {

        Cart cart=cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("Cart","cartId",cartId));
        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);

        if(cartItem==null){
            throw new ResourceNotFoundException("product","prodcutId",productId);
        }

        cart.setTotalPrice(cart.getTotalPrice()-cartItem.getProductPrice()*cartItem.getQuantity());

        //deleting cartItem by product and cartid
        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId,productId);


        return "Product"+cartItem.getProduct().getProductName()+"removed from the cart!!!";
    }




    private Cart createCart() {
        Cart userCart  = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart != null){
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart =  cartRepository.save(cart);

        return newCart;
    }
    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));


        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);

        if(cartItem==null)throw  new APIException(("Product"+product.getProductName()+"not available in the cart!!"));

        //calculating and reducing by total prive
        double cartPrice=cart.getTotalPrice()-(cartItem.getProductPrice()*cartItem.getQuantity());

        cartItem.setProductPrice(product.getSpecialPrice());
        //updating the product price
        cart.setTotalPrice(cartPrice);

        cart.setTotalPrice(cartPrice+(cartItem.getProductPrice()*cartItem.getQuantity()));
        cartItem=cartItemRepository.save(cartItem);
    }

    @Transactional
    @Override
    public String createOrUpdateCartWithItems(List<CartItemDto> cartItems) {
        //get loged in user email
        String emailId= authUtil.loggedInEmail();

        //check if existing cart is available or create a new one
    Cart existingCart=cartRepository.findCartByEmail(emailId);


        if(existingCart==null){
            existingCart=new Cart();
            existingCart.setTotalPrice(0.00);
            existingCart.setUser(authUtil.loggedInUser());
            existingCart=cartRepository.save(existingCart);
        }else{
            //if cart already exist than we will get this
            //clear all the current item in the existing cart
            cartItemRepository.deleteAllByCartId(existingCart.getCartId());
        }

        double toatPrice=0.00;
        //process each item in the request to add to the cart
        for(CartItemDto cartItemDto:cartItems){
            Long productId=cartItemDto.getProductId();
            Integer quantity = cartItemDto.getQuantity();

            Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));
            //directly update product stock and total price
            //----->we would like to update cart when the user checkout
          //  product.setQuantity((product.getQuantity()-quantity));

            toatPrice+=product.getSpecialPrice()*quantity;

            //create save cart item
            CartItem cartItem=new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(existingCart);
            cartItem.setQuantity(quantity);
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setDiscount(product.getDiscount());
            cartItemRepository.save(cartItem);
        }

        existingCart.setTotalPrice(toatPrice);
        cartRepository.save(existingCart);
        return "Cart created/Updated with the new Items successfully";
    }
}
