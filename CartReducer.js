const initialState = {
  cart: [],
  totalPrice: 0,
  cartId: null,
};

export const cartReducer = (state = initialState, action) => {
  switch (action.type) {
    case "ADD_CART": {
      const productToAdd = action.payload;
      const existingProduct = state.cart.find(
        (item) => item.productId === productToAdd.productId,
      );

      if (existingProduct) {
        const updatedCart = state.cart.map((item) => {
          if (item.productId === productToAdd.productId) {
            return productToAdd;
          } else {
            return item;
          }
        });

        return {
          ...state,
          cart: updatedCart,
        };
      } else {
        const newCart = [...state.cart, productToAdd];
        return {
          ...state,
          cart: newCart,
        };
      }
    }
    case "REMOVE_CART":
      return {
        ...state,
        //filtering the products
        cart: state.cart.filter(
          (item) => item.productId !== action.payload.productId,
        ),
      };

    //updating in the local reducx store
    case "GET_USER_CART_PRODUCTS":
      return {
        ...state,
        cart: action.payload,
        totalPrice: action.totalPrice,
        cartId: action.cartId,
      };

    case "CLEAR_CART":
      return {
        ...state,
        cart: [],
        totalPrice: 0,
        cartId: 0,
      };
    default:
      return state;
  }
  // eslint-disable-next-line no-unreachable
  return state;
};
