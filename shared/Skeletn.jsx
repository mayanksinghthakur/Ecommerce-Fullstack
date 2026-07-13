import { colors, Skeleton } from "@mui/material";
import React from "react";

const Skeletn = () => {
  return (
    <>
      <Skeleton sx={{ bgcolor: "grey" }} />
      <Skeleton sx={{ bgcolor: "grey" }} />
      <Skeleton sx={{ bgcolor: "grey" }} />
      <Skeleton sx={{ bgcolor: "grey" }} />
      <Skeleton animation="wave" sx={{ bgcolor: "grey" }} />
      <Skeleton animation={false} sx={{ bgcolor: "grey" }} />
    </>
  );
};

export default Skeletn;
