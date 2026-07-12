import { FaExclamationTriangle } from "react-icons/fa";
import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import { fetchCategories } from "../../store/actions";
import Filter from "./Filter";
import useProductFilter from "../../hooks/useProductFilter";
import { RotatingLines } from "react-loader-spinner";
import Loader from "../shared/Loader";
import Paginations from "../shared/Paginations";
import ProductCard from "../shared/ProductCard";

const Products = () => {
  //there are defined int he error reducer
  const { isLoading, errorMessage } = useSelector((state) => state.errors);

  // const isLoading = false;
  // const errorMessage = "";

  //getting the products from the store and
  const { products, categories, pagination } = useSelector(
    (state) => state.products,
  );

  //dispatch hook
  const dispatch = useDispatch();

  //caliing useProductFilterHook

  useProductFilter();

  //1---->dynamic categories are working by the help of  this so we are useing this fetchCategories()

  // //fetch the product when the dispatch mounts
  useEffect(() => {
    //2.--->fetchCategories()is aa function in index.js
    dispatch(fetchCategories());
  }, [dispatch]);

  return (
    <div className="lg:px-14 sm:px-8 px-4 py-14 2xl:w-[90%] 2xl:mx-auto">
      <Filter categories={categories ? categories : []}></Filter>

      {/* isLoading is stead of this i want to use thre react spinners */}
      {isLoading ? (
        <Loader></Loader>
      ) : errorMessage ? (
        <div className="flex justify-center items-center h-200px">
          <FaExclamationTriangle className="text-slate-800 text-3xl mr-2" />
          <span className="text-slate-800 text-lg  font-medium">
            {errorMessage}
          </span>
        </div>
      ) : (
        <div className="min-h-700px">
          <div className="pb-6 pt-14 grid 2xl:grid-cols-4 sm:grid-cols-2 gap-y-6 gap-x-6">
            {products &&
              products.map((item, i) => <ProductCard key={i} {...item} />)}
          </div>
          <div className="flex justify-center pt-10">
            <Paginations
              //getting these proprties from backend
              numberOfPage={pagination?.totalPages}
              totalProducts={pagination?.totalElements}
            ></Paginations>
          </div>
        </div>
      )}
    </div>
  );
};

export default Products;
