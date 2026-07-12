import {
  Button,
  FormControl,
  IconButton,
  InputLabel,
  MenuItem,
  Select,
  Tooltip,
} from "@mui/material";
import { useEffect, useState } from "react";
import { FaArrowDown, FaArrowUp, FaSearch } from "react-icons/fa";
import { FiRefreshCw } from "react-icons/fi";
import { useLocation, useNavigate, useSearchParams } from "react-router";

//passing cattegory
const Filter = ({ categories }) => {
  /////////----->major problem is that we are hardcodin ghte category name herre so we want ot dynamically passs this catergory
  // categories that will be defined in the options
  // const categories = [
  //   { categoryId: 1, categoryName: "Electronics" },
  //   { categoryId: 2, categoryName: "Clothing" },
  //   { categoryId: 3, categoryName: "Furniture" },
  //   { categoryId: 4, categoryName: "Books" },
  //   { categoryId: 5, categoryName: "Toys" },
  // ];

  const [searchParams] = useSearchParams();
  const pathname = useLocation().pathname;
  //urlSearchParam helps to work with query string and url it helps to get set query parameters
  const params = new URLSearchParams(searchParams);
  const navigate = useNavigate();

  //filter criteria that we will give to the users
  const [category, setCategory] = useState("all");
  const [sortOrder, setSortOrder] = useState("asc");
  const [searchTerm, setSearchTerm] = useState("");

  //when searchParams mounts than we do these
  useEffect(() => {
    const currentCategory = searchParams.get("category") || "all";
    const currentSortOrder = searchParams.get("sortby") || "asc";
    const currentSearchTerm = searchParams.get("keyword") || "";

    setCategory(currentCategory);
    setSortOrder(currentSortOrder);
    setSearchTerm(currentSearchTerm);
  }, [searchParams]);

  useEffect(() => {
    const handler = setTimeout(() => {
      if (searchTerm) {
        searchParams.set("keyword", searchTerm);
      } else {
        searchParams.delete("keyword");
      }
      navigate(`${pathname}?${searchParams.toString()}`);
    }, 700);
    return () => {
      clearTimeout(handler);
    };
  }, [searchParams, searchTerm, navigate, pathname]);

  //when
  const handleCategoryChange = (event) => {
    const seletedCategory = event.target.value;
    if (seletedCategory === "all") {
      params.delete("category");
    } else {
      params.set("category", seletedCategory);
    }

    //if url looks like this than we will get the  http://localhost:aaaa?keyword=telvision&sortby=desc
    //same we ahve done here

    //so when this updates the searchParams changes and all the things loads
    navigate(`${pathname}?${params}`);
    setCategory(event.target.value);
  };

  ///we will change the order of the element  order using this
  const toggleSortOrder = () => {
    setSortOrder((prevorder) => {
      const newOrder = prevorder === "asc" ? "desc" : "asc";
      params.set("sortby", newOrder);

      navigate(`${pathname}?${params}`);
      return newOrder;
    });
  };

  //when search param is changes than all filter is claered and we will original home url from browser and navigate to it
  const handleClearFilter = () => {
    navigate({ pathname: window.location.pathname });
  };
  return (
    <div className="flex lg:flex-row flex-col-reverse lg:justify-between justify-center items-center gap-4">
      {/* searching ui */}
      <div className="relative flex items-center 2xl:w-112.5 sm:w-105 w-full">
        <input
          type="text"
          placeholder="Search Products"
          className="border border-gray-400 text-slate-800 rounded-md py-2 pl-10 pr-4 w-full focus:outline-hidden focus:ring-2 focus:ring-[#1976d2]"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        ></input>

        <FaSearch className="absolute left-3 text-slate-800 size={20}"></FaSearch>
      </div>

      {/* filtering ui */}
      <div className="flex sm:flex-row flex-col gap-4 items-center">
        <FormControl
          className="text-slate-800 border-slate-700"
          variant="outlined"
          size="=small"
        >
          <InputLabel>Category</InputLabel>
          <Select
            labelId="category-select-label"
            value={category}
            onChange={handleCategoryChange}
            label="Category"
            className="min-w-30 text-slate-800 border-slate-700"
          >
            <MenuItem value="all">All</MenuItem>
            {categories.map((item) => (
              <MenuItem key={item.categoryId} value={item.categoryName}>
                {item.categoryName}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        {/* sort button and clear filter */}

        <Tooltip title="Delete">
          <Button
            variant="contained"
            color="primary"
            className=" flex items-center gap-2 h-10"
            onClick={toggleSortOrder}
          >
            Sort By
            {sortOrder === "asc" ? (
              <FaArrowUp size={20}></FaArrowUp>
            ) : (
              <FaArrowDown size={20}></FaArrowDown>
            )}
          </Button>
        </Tooltip>
        <Tooltip
          describeChild
          title="Does not add if it already exists."
        ></Tooltip>
        <button
          className="flex items-center gap-2 bg-rose-900 text-white px-3 py-2 rounded-md transition duration-300 ease-out shadow-md focus:outline-hidden cursor-pointer"
          onClick={handleClearFilter}
        >
          <FiRefreshCw className="font-semibold" size={16} />
          <span className="font-semibold ">Clear Filter</span>
        </button>
      </div>
    </div>
  );
};

export default Filter;
