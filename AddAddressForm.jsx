import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { AiOutlineLogin } from "react-icons/ai";
import InputField from "../shared/InputField";
import { Link } from "react-router";
import { FaAddressCard } from "react-icons/fa";
import { useDispatch, useSelector } from "react-redux";
import toast from "react-hot-toast";
import { addUpdateUserAddress } from "../../store/actions";

const AddAddressForm = ({ address, setOpenAddressModal }) => {
  const dispatch = useDispatch();
  //gettin the btn loader form the errorReducer
  const { btnLoader } = useSelector((state) => state.errors);
  const {
    register,
    handleSubmit,
    reset,
    setValue,
    formState: { errors },
  } = useForm({
    mode: "onTouched",
  });

  const onSaveAddressHandler = async (data) => {
    //if there is the address present whihch is having the address id whihc is present in the redux stroew also so we will do this

    // addong the action
    dispatch(
      addUpdateUserAddress(
        data,
        toast,
        address?.addressId,
        setOpenAddressModal,
      ),
    );
  };

  //so if we selected an address and it is present in the redux strore so we could say that we have the address and we could pass the current info and it will be used to edit the form
  useEffect(() => {
    if (address?.addressId) {
      setValue("buildingName", address?.buildingName);
      setValue("city", address?.city);
      setValue("street", address?.street);
      setValue("state", address?.state);
      setValue("pincode", address?.pincode);
      setValue("country", address?.country);
    }
  }, [address]);
  return (
    <div>
      <form onSubmit={handleSubmit(onSaveAddressHandler)}>
        <div className="flex justify-center items-center mb-4 font-semibold text-2xl text-slate-800 py-2 px-4">
          <FaAddressCard className="mr-2 text-2xl" />
          <h1 className="text-slate-800 text-center font-montserrat lg:text-3xl text-2xl font-bold">
            {!address?.addressId ? "Add Address" : "UpdateAddress"}
          </h1>
        </div>

        <div className="flex flex-col gap-4">
          <InputField
            label="Building Name"
            required
            id="buildingName"
            type="text"
            message="*Building Name is required"
            placeholder="Enter Building Name"
            register={register}
            errors={errors}
          />

          <InputField
            label="City"
            required
            id="city"
            type="text"
            message="*city is required"
            placeholder="Enter City"
            register={register}
            errors={errors}
          />
          <InputField
            label="State"
            required
            id="state"
            type="text"
            message="*State is required"
            placeholder="Enter State"
            register={register}
            errors={errors}
          />
          <InputField
            label="Pincode"
            required
            id="pincode"
            type="text"
            message="*pincode is required"
            placeholder="Enter Pincode"
            register={register}
            errors={errors}
          />
          <InputField
            label="Street"
            required
            id="street"
            type="text"
            message="*street is required"
            placeholder="Enter Street"
            register={register}
            errors={errors}
          />

          <InputField
            label="Country"
            required
            id="country"
            type="text"
            message="*Country is required"
            placeholder="Enter Country"
            register={register}
            errors={errors}
          />
        </div>

        <button
          disabled={btnLoader}
          className="text-white bg-custom-blue px-4 py-2 rounded-md mt-4"
          type="submit"
        >
          {btnLoader ? (
            <>
              Loading..
            </>
          ) : (
            <>Save</>
          )}
        </button>
      </form>
    </div>
  );
};

export default AddAddressForm;
