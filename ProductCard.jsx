import { useState } from "react";
import { FaShoppingCart } from "react-icons/fa";
import ProductViewModal from "./ProductViewModal";
import truncateText from "../../utils/truncateText";
import { useDispatch } from "react-redux";
import { addToCart } from "../../store/actions";
import toast from "react-hot-toast";
// individual product that is defined
const ProductCard = ({
  productId,
  productName,
  image,
  description,
  quantity,
  price,
  discount,
  specialPrice,
}) => {
  const [openProductViewModal, setOpenProductViewModal] = useState(false);

  const btnLoader = false;
  const [selectedViewProduct, setSelectedViewProduct] = useState("");
  const dispatch = useDispatch();
  const isAvailable = quantity && Number(quantity > 0);

  const handleProductView = (product) => {
    setSelectedViewProduct(product);
    setOpenProductViewModal(true);
  };

  //cartItems is passed from here if present in the cart
  //when user will click this will be triggered
  // passing this to the action
  const addToCartHandler = (cartItems) => {
    dispatch(addToCart(cartItems, 1, toast));
  };
  return (
    <div className="border rounded-lg shadow-xl  overflow-hidden transition-shadow duration-300">
      <div
        onClick={() => {
          handleProductView({
            id: productId,
            productName,
            image,
            description,
            quantity,
            price,
            discount,
            specialPrice,
          });
        }}
        className="w-full overflow-hidden aspect=[3/2]"
      >
        <img
          className="w-full h-full cursor-pointer transition-transform duration-300 transform hover:scale-105"
          src={image}
        ></img>
      </div>
      <div className="p-4">
        <h2
          onClick={() => {
            handleProductView({
              id: productId,
              productName,
              image,
              description,
              quantity,
              price,
              discount,
              specialPrice,
            });
          }}
          className="text-lg font-semibold mb-2 cursor-pointer"
        >
          {truncateText(productName, 50)}
        </h2>
        <div className="min-h-20 max-h-20">
          <p className="text-gray-600 text-sm">
            {/* setting the caharacter limit */}
            {truncateText(description, 50)}
          </p>
        </div>

        <div className="flex items-center justify-between">
          {" "}
          {specialPrice ? (
            <div className="flex flex-col  ">
              <span className="text-gray-400 line-through">
                ${Number(price).toFixed(2)}
              </span>
              <span className="text-xl font-bold text-slate-700  w-40px">
                ${Number(price).toFixed(2)}
              </span>
            </div>
          ) : (
            <span className="text-xl font-bold text-slate-700 w-50px">
              ${Number(price).toFixed(2)}
            </span>
          )}
          {/* conditional adding of the css will be done here */}
          <button
            disabled={!isAvailable || btnLoader}
            //adiing the addToCartHandler function here
            onClick={() =>
              addToCartHandler({
                image,
                productName,
                description,
                specialPrice,
                price,
                productId,
                quantity,
              })
            }
            className={`bg-blue-500 ${isAvailable ? "opacity-100" : "opacity-70"} text-white py-2 px-3 rounded-lg items-center transition-colors duration-300 w-40 flex justify-center cursor-pointer pl-1.5`}
          >
            <FaShoppingCart className="mr-2"></FaShoppingCart>
            {isAvailable ? "Add to Cart" : "Stock Out"}
          </button>
        </div>
      </div>
      {/* paassing value to the  */}
      <ProductViewModal
        open={openProductViewModal}
        setOpen={setOpenProductViewModal}
        product={selectedViewProduct}
        isAvailable={isAvailable}
      />
    </div>
  );
};

export default ProductCard;
