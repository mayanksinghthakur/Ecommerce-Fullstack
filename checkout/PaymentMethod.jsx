import {
  FormControl,
  FormControlLabel,
  FormLabel,
  Radio,
  RadioGroup,
} from "@mui/material";
import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { addPaymentMethod, createUserCart } from "../../store/actions";

const PaymentMethod = () => {
  const dispatch = useDispatch();

  //using pyment defined in the store
  const { paymentMethod } = useSelector((state) => state.payment);
  const { cart, cartId } = useSelector((state) => state.carts);
  const { isLoading, errorMessage } = useSelector((state) => state.errors);
  //calling addPaymentMethod defined in the store as event
  const PaymentMethodHandler = (method) => {
    //here the event will be sdispatched that is returned with the value defined in the index.js in addPaymentMethod

    dispatch(addPaymentMethod(method));
  };

  useEffect(
    () => {
      if (cart.length > 0 && !cartId && !errorMessage) {
        //creating the cartItem
        const sendCartItems = cart.map((item) => {
          return {
            productId: item.productId,
            quantity: item.quantity,
          };
        });
        // sending it to the bacjkend 
        dispatch(createUserCart(sendCartItems));
      }
    },
    //whenever either these two changes we will create request
    [dispatch, cartId],
  );
  return (
    <div className="max-w-md mx-auto p-5 bg-white shadow-md rounded-lg mt-16 border">
      <h1 className="text-2xl font-semibold mb-4 ">Select Payment Method</h1>
      <FormControl>
        <RadioGroup
          aria-labelledby="payment method"
          name="paymentMethod"
          value={paymentMethod}
          onChange={(e) => PaymentMethodHandler(e.target.value)}
        >
          <FormControlLabel
            value="Stripe"
            control={<Radio color="primary" />}
            label="Stripe"
            className="text-gray-700"
          />
          <FormControlLabel
            value="Paypal"
            control={<Radio color="primary" />}
            label="Paypal"
            className="text-gray-700"
          />
        </RadioGroup>
      </FormControl>
    </div>
  );
};

export default PaymentMethod;
