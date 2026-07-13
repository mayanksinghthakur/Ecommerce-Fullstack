import { Elements } from "@stripe/react-stripe-js";
import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import PaymentFrom from "./PaymentFrom";
import { loadStripe } from "@stripe/stripe-js";
import { createStripePaymentSecret } from "../../store/actions";
import { Skeleton } from "@mui/material";

const stripePromise = loadStripe(import.meta.env.VITE_STRIPE_PUBLISHABLE_KEY);
const StripePayment = () => {
  const dispatch = useDispatch();

  const { clientSecret } = useSelector((state) => state.auth);
  const { totalPrice } = useSelector((state) => state.carts);

  const { isLoading, errorMessage } = useSelector((state) => state.errors);

  const { user, selectedUserCheckoutAddress } = useSelector(
    (state) => state.auth,
  );

  useEffect(() => {
    if (!clientSecret) {
      const sendData = {
        amount: Number(totalPrice * 100),
        currency: "usd",
        email: user.email,
        name: `${user.username}`,
        address: selectedUserCheckoutAddress,
        description: `Order for ${user.email}`,
        metadata: {
          test: "1",
        },
      };

      // Stripe expects amounts in cents (e.g., $50.00 -> 5000)
      // Assuming totalPrice is in dollars like 50.00
      const amountInCents = Math.round(totalPrice * 100);

      // Create the payload object matching StripePaymentDto in Java
      const paymentData = {
        amount: amountInCents,
        currency: "usd", // Make sure this matches your backend/default currency
      };

      dispatch(createStripePaymentSecret(sendData));
    }
  }, [clientSecret, dispatch, totalPrice]);

  if (isLoading) {
    return (
      <div className="max-w-lg mx-auto">
        <Skeleton />
      </div>
    );
  }
  return (
    <div>
      {clientSecret && (
        <Elements stripe={stripePromise} options={{ clientSecret }}>
          <PaymentFrom clientSecret={clientSecret} totalPrice={totalPrice} />
        </Elements>
      )}
    </div>
  );
};

export default StripePayment;
