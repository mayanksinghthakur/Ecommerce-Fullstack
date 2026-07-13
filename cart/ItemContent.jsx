import { useState } from "react";
import { HiOutlineTrash } from "react-icons/hi";
import SetQuantity from "./SetQuantity";
import { useDispatch } from "react-redux";
import {
  decreaseCartQuantity,
  increaseCartQuantity,
  removeFromCart,
} from "../../store/actions";
import toast from "react-hot-toast";
import { formatPrice } from "../../utils/formatPrice";
import truncateText from "../../utils/truncateText";
//we will render each cart item
const ItemContent = ({
  productId,
  productName,
  image,
  description,
  quantity,
  price,
  discount,
  specialPrice,
}) => {
  const [currentQuantity, setCurrentQuantity] = useState(quantity);

  const dispatch = useDispatch();

  //this function will be used for the quantity to be increased
  //so if quantiy incases we want to update this in many places like in ui and store and storage

  //so we despatched it in the store and we have used this in the index.js as aufnction
  const handleQtyIncrease = (cartItems) => {
    // increaseCartQuantity define in the index.js
    dispatch(
      increaseCartQuantity(
        cartItems,
        toast,
        currentQuantity,
        setCurrentQuantity,
      ),
    );
  };

  const handleQtyDecrease = (cartItems) => {
    if (currentQuantity > 1) {
      const newQuantity = currentQuantity - 1;
      setCurrentQuantity(newQuantity);
      //passing the value inside of the decreaseCartQuantity and it will be handled by the index,js
      dispatch(decreaseCartQuantity(cartItems, newQuantity));
    }
  };

  const removeItemFromCart = (cartItems) => {
    dispatch(removeFromCart(cartItems, toast));
  };

  return (
    <div className="grid md:grid-cols-5 grid-cols-4 md:text-md text-sm gap-4   items-center  border border-slate-200  rounded-md  lg:px-4  py-4 p-2">
      <div className="md:col-span-2 justify-self-start flex  flex-col gap-2 ">
        <div className="flex md:flex-row flex-col lg:gap-4 sm:gap-3 gap-0 items-start ">
          <h3 className="lg:text-[17px] text-sm font-semibold text-slate-600">
            {truncateText(productName)}
          </h3>
        </div>
        <div className="md:w-36 sm:w-24 w-12">
          <img
            src={image}
            alt={productName}
            className="md:h-36 sm:h-24 h-12 w-full object-cover rounded-md"
          />
          <div className="flex items-start gap-5 mt-3">
            <button
              onClick={() =>
                removeItemFromCart({
                  image,
                  productName,
                  description,
                  specialPrice,
                  price,
                  productId,
                  quantity,
                })
              }
              className="flex items-center font-semibold space-x-2 px-4 py-1 text-xs border border-rose-600 text-rose-600 rounded-md hover:bg-red-50 transition-colors duration-200"
            >
              <HiOutlineTrash></HiOutlineTrash>
              Remove
            </button>
          </div>
        </div>
      </div>
      <div className="justify-center lg:text-17 text-slate-700 font-semibold">
        {formatPrice(Number(specialPrice))}
      </div>
      <div className="justify-self-center lg:text-[17px] text-sm text-slate-600 font-semibold">
        <SetQuantity
          quantity={currentQuantity}
          cardCounter={true}
          //passing fucntion in the prop that has following things
          handleQtyIncrease={() => {
            handleQtyIncrease({
              image,
              productName,
              description,
              specialPrice,
              price,
              productId,
              quantity,
            });
          }}
          handleQtyDecrease={() => {
            handleQtyDecrease({
              image,
              productName,
              description,
              specialPrice,
              price,
              productId,
              quantity,
            });
          }}
        ></SetQuantity>
      </div>

      <div className="justify-self-center lg:text-[17px] text-sm text-slate-600 font-semibold">
        {formatPrice(Number(currentQuantity) * Number(specialPrice))}
      </div>
    </div>
  );
};

export default ItemContent;
