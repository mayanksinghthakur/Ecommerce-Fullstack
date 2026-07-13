import { Skeleton } from "@mui/material";
import {
  PaymentElement,
  useElements,
  useStripe,
} from "@stripe/react-stripe-js";
import React, { useState } from "react";

const PaymentFrom = ({ clientSecret, totalPrice }) => {
  const stripe = useStripe();
  const elements = useElements();

  const [errorMessage, setErrorMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!stripe || !elements) return;

    //if  after submitting we have any problem we will show the error
    const { error: submitError } = await elements.submit();

    const { error } = await stripe.confirmPayment({
      elements,
      clientSecret,
      //after payment is conformed we need to redirect the user
      confirmParams: {
        return_url: `${import.meta.env.VITE_FRONTEND_URL}/order-confirm`,
      },
    });

    if (error) {
      setErrorMessage(error.message);
      return false;
    }
  };

  const PaymentElementOptions = {
    layout: "tabs",
  };

  const isLoading = !clientSecret || !stripe || !elements;
  return (
    <form onSubmit={handleSubmit} className="max-w-lg mx-auto p-4 py-20">
      <h2 className="text-xl font-semibold mb-4">Payment Information</h2>
      {isLoading ? (
        <Skeleton />
      ) : (
        <>
          {clientSecret && (
            <PaymentElement options={PaymentElementOptions}></PaymentElement>
          )}
          {errorMessage && (
            <div className="text-red-500 mt-2 ">{errorMessage}</div>
          )}

          {/* button will be disabled when these two condition are correct */}
          <button
            disabled={!stripe || isLoading}
            className="text-white w-full px-5 py-2  bg-black mt-2 rounded-md font-bold disabled:opacity-50 disabled:animate-pulse hover:bg-green-400 ease-in-out  "
          >
            {!isLoading
              ? `pay ${Number(totalPrice).toFixed(2)}`
              : "processsing"}
          </button>
        </>
      )}
    </form>
  );
};

export default PaymentFrom;
