import { MdArrowBack, MdShoppingCart } from "react-icons/md";
import { Link } from "react-router";

const CartEmpty = () => {
  return (
    <>
      <div className="min-h-200 flex flex-col items-center justify-center ">
        <div className="flex-col items-center">
          <MdShoppingCart
            size={80}
            className="mb-4 text-slate-500"
          ></MdShoppingCart>
        </div>
        <div className="text-3xl font-bold text-slate-700">
          {" "}
          Your Cart is Empty
        </div>
        <div className="text-lg  text-slate-500">
          {" "}
          Add some products to get started
        </div>
        <div className="mt-6">
          <Link
            to="/"
            className="flex gap-2 items-center text-blue-500 hover:text-blue-800 transition"
          >
            <MdArrowBack size={24}></MdArrowBack>
            <span className="font-medium text-blue-500">Start Shopping</span>
          </Link>
        </div>
      </div>
    </>
  );
};
export default CartEmpty;
