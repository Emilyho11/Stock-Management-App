import axios from "axios";

const AxiosClient = axios.create({
	baseURL: "http://localhost:8080/",
	headers: {
		"Content-Type": "application/json",
	},
});

export default AxiosClient;
