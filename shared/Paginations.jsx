import { Pagination } from "@mui/material";
import { useSearchParams } from "react-router";
import { useLocation, useNavigate } from "react-router";

const Paginations = ({ numberOfPage, totalProducts }) => {
  const [searchParams] = useSearchParams();
  const pathname = useLocation().pathname;
  const params = new URLSearchParams(searchParams);
  const navigate = useNavigate();
  const paramValue = searchParams.get("page")
    ? Number(searchParams.get("page"))
    : 1;

  const onChangeHandler = (event, value) => {
    //updating page value
    params.set("page", value.toString());

    //setting the prarams
    //redirecting the user to the new path
    navigate(`${pathname}?${params}`);
  };

  return (
    <div>
      {/*  //as we know backend has all the pgination info given to us by default so
      if we want to use that in our pagination we could use the url params //and
      we will be able to change the values to the url and if url is changed we
      want to send request with the modified url */}
      <Pagination
        //we need to add dynamic count and pages in these sos we will get it from the bacend to show to the screen

        count={numberOfPage}
        //we have to define page value because it is neccessary
        page={paramValue}
        defaultPage={1}
        siblingCount={0}
        boundaryCount={2}
        shape="rounded"
        onChange={onChangeHandler}
      />
    </div>
  );
};

export default Paginations;
