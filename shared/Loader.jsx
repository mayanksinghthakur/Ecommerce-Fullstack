import { RotatingLines } from "react-loader-spinner";

const Loader = ({ text }) => {
  return (
    <div className="flex justify-center items-center w-full h-450px">
      <div className="flex flex-col items-center gap-1">
        <RotatingLines
          visible={true}
          height="50"
          width="50"
          color="green"
          strokeWidth="5"
          animationDuration="0.75"
          ariaLabel="rotating-lines-loading"
          wrapperStyle={{}}
          wrapperClass=""
        />
        <p className="text-xl font-semibold">
          {text ? text : "Please wait..."}
        </p>
      </div>
    </div>
  );
};
export default Loader;
