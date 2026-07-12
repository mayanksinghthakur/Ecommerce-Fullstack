import { MdArrowBack, MdShoppingCart } from "react-icons/md";
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router";
import ItemContent from "./ItemContent";
import CartEmpty from "./CartEmpty";
import { formatPrice } from "../../utils/formatPrice";

const Cart = () => {
  const dispatch = useDispatch();

  const { cart } = useSelector((state) => state.carts);

  //we need to create copy of the cart that we will get from redux because we cannot make chnages in the cart directly
  const newCart = {
    ...cart,
  };
  newCart.totalPrice = cart?.reduce(
    (acc, curr) => acc + Number(curr?.specialPrice) * Number(curr?.quantity),
    0,
  );

  if (!cart || cart.length == 0) {
    return <CartEmpty />;
  }
  return (
    <div className="lg:px-14 sm:px-8 px-4 py-10">
      <div className="flex flex-col items-center mb-12">
        <h1 className="text-4xl font-bold text-gray-800 flex items-center gap-3 font-montserrat">
          <MdShoppingCart className="text-gray-700" size={40}></MdShoppingCart>
          My Cart
        </h1>
        <p className="text-lg text-gray-600 mt-2">Selected Items</p>
      </div>
      <div className="grid md:grid-cols-5 grid-cols-4 gap-4 pb-2 font-semibold items-center">
        <div className="md:col-span-2 justify-self-start text-lg text-slate-800 lg:ps-4">
          Product
        </div>
        <div className=" justify-self-start text-lg text-slate-800 ">Price</div>
        <div className=" justify-self-start text-lg text-slate-800 ">
          Quantity
        </div>
        <div className=" justify-self-start text-lg text-slate-800 ">Total</div>
      </div>

      <div>
        {/* passin the cartitem in the ItemCOntent */}
        {cart &&
          cart.length > 0 &&
          cart.map((item, i) => <ItemContent key={i} {...item} />)}
      </div>

      <div className="border-t-20px border-slate-200 py-4 flex sm:flex-row sm:px-0 px-2 flex-col sm:justify-between gap-4">
        <div></div>
        <div className="flex text-sm gap-1 flex-col">
          <div className=" flex justify-between w-full md:text-lg text-sm font-semibold">
            <span>Subtotal</span>
            <span>{formatPrice(newCart?.totalPrice)}</span>
          </div>
          <p className="text-slate-500 ">
            Taxes and Shiping calculated At checkOut
          </p>
          <Link className="w-full flex justify-end " to="/checkout">
            <button
              onClick={() => {}}
              className="font-semibold w-75 py-2 px-4 rounded-sm bg-custom-blue text-white flex items-center justify-center gap-2 hover:text-gray-300 transition-5"
            >
              <MdShoppingCart></MdShoppingCart>
              Checkout
            </button>
          </Link>

          <Link
            className="w-full flex items-center mt-2 text-slate-500"
            to="/products"
          >
            <MdArrowBack></MdArrowBack>
            <span> Continue Shopping</span>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Cart;
