import React from "react";
import {
  FaBuilding,
  FaCheckCircle,
  FaEdit,
  FaMapMarkerAlt,
  FaTrash,
} from "react-icons/fa";
import { BiStreetView } from "react-icons/bi";
import { useDispatch, useSelector } from "react-redux";
import { MdLocationCity } from "react-icons/md";
import { IoEarth } from "react-icons/io5";
import { TbMapPinCode } from "react-icons/tb";
import { selectUserCheckoutAddress } from "../../store/actions";
const AddressList = ({
  addresses,
  setSelectedAddress,
  setOpenAddressModal,
  setOpenDeleteModal,
}) => {
  const dispatch = useDispatch();
  //so we are getting value from the auth reducer
  const { selectedUserCheckoutAddress } = useSelector((state) => state.auth);

  //if user updates the address we will call this function and update the redux store
  const handleAddressSelection = (addresses) => {
    dispatch(selectUserCheckoutAddress(addresses));
  };

  const onEditButtonHandler = (addresses) => {
    setSelectedAddress(addresses);
    setOpenAddressModal(true);
  };

  const onDeleteButtonHandler = (addresses) => {
    setSelectedAddress(addresses);

    //getting from the addressInfo
    setOpenDeleteModal(true);
  };

  return (
    <div className="space-y-4">
      {addresses.map((address) => (
        <div
          key={address.addressId}
          onClick={() => handleAddressSelection(address)}
          className={`p-4 border rounded-md cursor-pointer relative ${
            selectedUserCheckoutAddress?.addressId === address.addressId
              ? "bg-green-100"
              : "bg-white"
          }`}
        >
          <div className="flex items-start">
            <div className="space-y-1">
              <div className="flex items-center">
                <FaBuilding
                  size={14}
                  className="mr-2 text-gray-600"
                ></FaBuilding>
                <p className="font-semibold">{address.buildingName}</p>
                {selectedUserCheckoutAddress?.addressId ===
                  address.addressId && (
                  <FaCheckCircle className="text-green-500 ml-2"></FaCheckCircle>
                )}
              </div>
              <div className="flex items-center">
                <BiStreetView
                  size={17}
                  className="mr-2 text-gray-600"
                ></BiStreetView>
                <p>{address.street}</p>
              </div>
              <div className="flex items-center">
                <FaMapMarkerAlt
                  size={17}
                  className="mr-2 text-gray-600"
                ></FaMapMarkerAlt>
                <p>
                  {address.city}
                  {"," + address.state}
                </p>
              </div>
              <div className="flex items-center">
                <TbMapPinCode
                  size={17}
                  className="mr-2 text-gray-600"
                ></TbMapPinCode>
                <p>{address.pincode}</p>
              </div>
              <div className="flex items-center">
                <IoEarth size={17} className="mr-2 text-gray-600"></IoEarth>
                <p>{address.buildingName}</p>
              </div>
            </div>
          </div>

          <div className="flex  gap-3 absolute top-4 right-2 ">
            <button onClick={() => onEditButtonHandler(address)}>
              <FaEdit
                size={18}
                className="text-teal-700 cursor-pointer"
              ></FaEdit>
            </button>

            <button onClick={() => onDeleteButtonHandler(address)}>
              <FaTrash
                size={18}
                className="text-rose-600 cursor-pointer"
              ></FaTrash>
            </button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default AddressList;
