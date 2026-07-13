import {
  Dialog,
  DialogBackdrop,
  DialogPanel,
  DialogTitle, // Imported but unused, you can remove if you want
} from "@headlessui/react";
import { Divider } from "@mui/material";

import { MdClose, MdDone } from "react-icons/md";
import Status from "./Status";

function ProductViewModal({ open, setOpen, product, isAvailable }) {
  
  // FIX A: If product is null, don't render anything (prevents crash)
  if (!product) {
    return null;
  }

  // Now it is safe to destructure
  const {
    productId,
    productName,
    image,
    description,
    quantity,
    price,
    discount,
    specialPrice,
  } = product;

  return (
    <>
      <Dialog
        open={open}
        as="div"
        className="relative z-10 focus:outline-none"
        // FIX B: Use the correct function to close the modal
        onClose={() => setOpen(false)} 
        __demoMode
      >
        <DialogBackdrop className="fixed inset-0 bg-black/30" />
        <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
          <div className="flex min-h-full items-center justify-center p-4">
            <DialogPanel
              transition
              // FIX D: Corrected Tailwind arbitrary value syntax
              className="relative transform overflow-hidden rounded-lg bg-white shadow-xl transition-all w-full max-w-[620px]"
            >
              {image && (
                // FIX D: Corrected aspect-ratio syntax
                <div className="flex justify-center aspect-[3/2] bg-gray-100">
                  <img 
                    src={image} 
                    alt={productName} 
                    className="object-contain max-h-full max-w-full"
                  />
                </div>
              )}

              <div className="p-4"> {/* Added padding for better spacing */}
                  <h2 className="text-2xl font-bold mb-2">{productName}</h2>
                  
                  <div className="flex items-center justify-between">
                    {/* FIX C: Show Special Price instead of Price again */}
                    {specialPrice ? (
                      <div className="flex flex-col">
                        <span className="text-gray-400 line-through text-sm">
                          ${Number(price).toFixed(2)}
                        </span>
                        <span className="text-xl font-bold text-slate-700">
                          ${Number(specialPrice).toFixed(2)}
                        </span>
                      </div>
                    ) : (
                      <span className="text-xl font-bold text-slate-700">
                        ${Number(price).toFixed(2)}
                      </span>
                    )}

                    {isAvailable ? (
                      <Status
                        text="In Stock"
                        icon={MdDone}
                        bg="bg-teal-200"
                        color="text-teal-900"
                      />
                    ) : (
                      <Status
                        text="Out of Stock"
                        icon={MdClose}
                        bg="bg-rose-200"
                        color="text-rose-700"
                      />
                    )}
                  </div>
                  <Divider className="my-2" />
                  <p className="text-gray-600 mb-4">{description}</p>

                  <div className="flex justify-end">
                    <button
                      onClick={() => setOpen(false)}
                      type="button"
                      className="px-4 py-2 text-sm font-semibold text-slate-700 border border-slate-300 rounded hover:bg-gray-100 cursor-pointer"
                    >
                      Close
                    </button>
                  </div>
              </div>
            </DialogPanel>
          </div>
        </div>
      </Dialog>
    </>
  );
}

export default ProductViewModal;