import React from "react";
import Modal from "./Modal";

const AddStocksModal = () => {
	return (
		<Modal>
			<div className="flex flex-col gap-4 p-4">
				<h1 className="text-2xl font-semibold">Add Stocks</h1>

				<div className="relative overflow-x-auto shadow-md sm:rounded-lg">
					<table className="w-full text-sm text-left rtl:text-right text-gray-500">
						<thead className="text-xs text-gray-700 uppercase bg-gray-50">
							<tr>
								<th scope="col" className="p-4">
									<div className="flex items-center">
										<input
											id="checkbox-all-search"
											type="checkbox"
											className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 "
										/>
										<label htmlFor="checkbox-all-search" className="sr-only">
											checkbox
										</label>
									</div>
								</th>
								<th scope="col" className="px-6 py-3">
									Product name
								</th>
								<th scope="col" className="px-6 py-3">
									Color
								</th>
								<th scope="col" className="px-6 py-3">
									Category
								</th>
								<th scope="col" className="px-6 py-3">
									Accessories
								</th>
								<th scope="col" className="px-6 py-3">
									Available
								</th>
								<th scope="col" className="px-6 py-3">
									Price
								</th>
								<th scope="col" className="px-6 py-3">
									Weight
								</th>
								<th scope="col" className="px-6 py-3">
									Action
								</th>
							</tr>
						</thead>
						<tbody>
							<tr className="bg-white border-b hover:bg-gray-50">
								<td className="w-4 p-4">
									<div className="flex items-center">
										<input
											id="checkbox-table-search-1"
											type="checkbox"
											className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 "
										/>
										<label htmlFor="checkbox-table-search-1" className="sr-only">
											checkbox
										</label>
									</div>
								</td>
								<th scope="row" className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
									Apple MacBook Pro 17"
								</th>
								<td className="px-6 py-4">Silver</td>
								<td className="px-6 py-4">Laptop</td>
								<td className="px-6 py-4">Yes</td>
								<td className="px-6 py-4">Yes</td>
								<td className="px-6 py-4">$2999</td>
								<td className="px-6 py-4">3.0 lb.</td>
								<td className="flex items-center px-6 py-4">
									<a href="#" className="font-medium text-blue-600 hover:underline">
										Edit
									</a>
									<a href="#" className="font-medium text-red-600 hover:underline ms-3">
										Remove
									</a>
								</td>
							</tr>
							<tr className="bg-white border-b hover:bg-gray-50">
								<td className="w-4 p-4">
									<div className="flex items-center">
										<input
											id="checkbox-table-search-2"
											type="checkbox"
											className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 "
										/>
										<label htmlFor="checkbox-table-search-2" className="sr-only">
											checkbox
										</label>
									</div>
								</td>
								<th scope="row" className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
									Microsoft Surface Pro
								</th>
								<td className="px-6 py-4">White</td>
								<td className="px-6 py-4">Laptop PC</td>
								<td className="px-6 py-4">No</td>
								<td className="px-6 py-4">Yes</td>
								<td className="px-6 py-4">$1999</td>
								<td className="px-6 py-4">1.0 lb.</td>
								<td className="flex items-center px-6 py-4">
									<a href="#" className="font-medium text-blue-600 hover:underline">
										Edit
									</a>
									<a href="#" className="font-medium text-red-600 hover:underline ms-3">
										Remove
									</a>
								</td>
							</tr>
							<tr className="bg-white border-b hover:bg-gray-50">
								<td className="w-4 p-4">
									<div className="flex items-center">
										<input
											id="checkbox-table-search-3"
											type="checkbox"
											className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 "
										/>
										<label htmlFor="checkbox-table-search-3" className="sr-only">
											checkbox
										</label>
									</div>
								</td>
								<th scope="row" className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
									Magic Mouse 2
								</th>
								<td className="px-6 py-4">Black</td>
								<td className="px-6 py-4">Accessories</td>
								<td className="px-6 py-4">Yes</td>
								<td className="px-6 py-4">No</td>
								<td className="px-6 py-4">$99</td>
								<td className="px-6 py-4">0.2 lb.</td>
								<td className="flex items-center px-6 py-4">
									<a href="#" className="font-medium text-blue-600 hover:underline">
										Edit
									</a>
									<a href="#" className="font-medium text-red-600 hover:underline ms-3">
										Remove
									</a>
								</td>
							</tr>
							<tr className="bg-white border-b hover:bg-gray-50">
								<td className="w-4 p-4">
									<div className="flex items-center">
										<input
											id="checkbox-table-search-3"
											type="checkbox"
											className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 "
										/>
										<label htmlFor="checkbox-table-search-3" className="sr-only">
											checkbox
										</label>
									</div>
								</td>
								<th scope="row" className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
									Apple Watch
								</th>
								<td className="px-6 py-4">Black</td>
								<td className="px-6 py-4">Watches</td>
								<td className="px-6 py-4">Yes</td>
								<td className="px-6 py-4">No</td>
								<td className="px-6 py-4">$199</td>
								<td className="px-6 py-4">0.12 lb.</td>
								<td className="flex items-center px-6 py-4">
									<a href="#" className="font-medium text-blue-600 hover:underline">
										Edit
									</a>
									<a href="#" className="font-medium text-red-600 hover:underline ms-3">
										Remove
									</a>
								</td>
							</tr>
							<tr className="bg-white border-b hover:bg-gray-50">
								<td className="w-4 p-4">
									<div className="flex items-center">
										<input
											id="checkbox-table-search-3"
											type="checkbox"
											className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 "
										/>
										<label htmlFor="checkbox-table-search-3" className="sr-only">
											checkbox
										</label>
									</div>
								</td>
								<th scope="row" className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
									Apple iMac
								</th>
								<td className="px-6 py-4">Silver</td>
								<td className="px-6 py-4">PC</td>
								<td className="px-6 py-4">Yes</td>
								<td className="px-6 py-4">Yes</td>
								<td className="px-6 py-4">$2999</td>
								<td className="px-6 py-4">7.0 lb.</td>
								<td className="flex items-center px-6 py-4">
									<a href="#" className="font-medium text-blue-600 hover:underline">
										Edit
									</a>
									<a href="#" className="font-medium text-red-600 hover:underline ms-3">
										Remove
									</a>
								</td>
							</tr>
							<tr className="bg-white border-b hover:bg-gray-50">
								<td className="w-4 p-4">
									<div className="flex items-center">
										<input
											id="checkbox-table-search-3"
											type="checkbox"
											className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 "
										/>
										<label htmlFor="checkbox-table-search-3" className="sr-only">
											checkbox
										</label>
									</div>
								</td>
								<th scope="row" className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
									Apple AirPods
								</th>
								<td className="px-6 py-4">White</td>
								<td className="px-6 py-4">Accessories</td>
								<td className="px-6 py-4">No</td>
								<td className="px-6 py-4">Yes</td>
								<td className="px-6 py-4">$399</td>
								<td className="px-6 py-4">38 g</td>
								<td className="flex items-center px-6 py-4">
									<a href="#" className="font-medium text-blue-600 hover:underline">
										Edit
									</a>
									<a href="#" className="font-medium text-red-600 hover:underline ms-3">
										Remove
									</a>
								</td>
							</tr>
							<tr className="bg-white border-b hover:bg-gray-50">
								<td className="w-4 p-4">
									<div className="flex items-center">
										<input
											id="checkbox-table-search-3"
											type="checkbox"
											className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 "
										/>
										<label htmlFor="checkbox-table-search-3" className="sr-only">
											checkbox
										</label>
									</div>
								</td>
								<th scope="row" className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
									iPad Pro
								</th>
								<td className="px-6 py-4">Gold</td>
								<td className="px-6 py-4">Tablet</td>
								<td className="px-6 py-4">No</td>
								<td className="px-6 py-4">Yes</td>
								<td className="px-6 py-4">$699</td>
								<td className="px-6 py-4">1.3 lb.</td>
								<td className="flex items-center px-6 py-4">
									<a href="#" className="font-medium text-blue-600 hover:underline">
										Edit
									</a>
									<a href="#" className="font-medium text-red-600 hover:underline ms-3">
										Remove
									</a>
								</td>
							</tr>
							<tr className="bg-white border-b hover:bg-gray-50">
								<td className="w-4 p-4">
									<div className="flex items-center">
										<input
											id="checkbox-table-search-3"
											type="checkbox"
											className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500"
										/>
										<label htmlFor="checkbox-table-search-3" className="sr-only">
											checkbox
										</label>
									</div>
								</td>
								<th scope="row" className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
									Magic Keyboard
								</th>
								<td className="px-6 py-4">Black</td>
								<td className="px-6 py-4">Accessories</td>
								<td className="px-6 py-4">Yes</td>
								<td className="px-6 py-4">Yes</td>
								<td className="px-6 py-4">$99</td>
								<td className="px-6 py-4">453 g</td>
								<td className="flex items-center px-6 py-4">
									<a href="#" className="font-medium text-blue-600 hover:underline">
										Edit
									</a>
									<a href="#" className="font-medium text-red-600 hover:underline ms-3">
										Remove
									</a>
								</td>
							</tr>
							<tr className="bg-white border-b hover:bg-gray-50">
								<td className="w-4 p-4">
									<div className="flex items-center">
										<input
											id="checkbox-table-search-3"
											type="checkbox"
											className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500  focus:ring-2"
										/>
										<label htmlFor="checkbox-table-search-3" className="sr-only">
											checkbox
										</label>
									</div>
								</td>
								<th scope="row" className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
									Apple TV 4K
								</th>
								<td className="px-6 py-4">Black</td>
								<td className="px-6 py-4">TV</td>
								<td className="px-6 py-4">Yes</td>
								<td className="px-6 py-4">No</td>
								<td className="px-6 py-4">$179</td>
								<td className="px-6 py-4">1.78 lb.</td>
								<td className="flex items-center px-6 py-4">
									<a href="#" className="font-medium text-blue-600 hover:underline">
										Edit
									</a>
									<a href="#" className="font-medium text-red-600 hover:underline ms-3">
										Remove
									</a>
								</td>
							</tr>
							<tr className="bg-white border-b hover:bg-gray-50">
								<td className="w-4 p-4">
									<div className="flex items-center">
										<input
											id="checkbox-table-search-3"
											type="checkbox"
											className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 "
										/>
										<label htmlFor="checkbox-table-search-3" className="sr-only">
											checkbox
										</label>
									</div>
								</td>
								<th scope="row" className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
									AirTag
								</th>
								<td className="px-6 py-4">Silver</td>
								<td className="px-6 py-4">Accessories</td>
								<td className="px-6 py-4">Yes</td>
								<td className="px-6 py-4">No</td>
								<td className="px-6 py-4">$29</td>
								<td className="px-6 py-4">53 g</td>
								<td className="flex items-center px-6 py-4">
									<a href="#" className="font-medium text-blue-600  hover:underline">
										Edit
									</a>
									<a href="#" className="font-medium text-red-600 hover:underline ms-3">
										Remove
									</a>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</Modal>
	);
};

export default AddStocksModal;
