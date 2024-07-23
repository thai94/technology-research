import http from "k6/http";
import { check, sleep } from "k6";

// Test configuration
export const options = {
    thresholds: {
      // Assert that 99% of requests finish within 3000ms.
      http_req_duration: ["p(99) < 3000"],
    },
    // Ramp the number of virtual users up and down
    stages: [
      { duration: "30s", target: 100 },
    //   { duration: "1m", target: 200 },
    //   { duration: "20s", target: 0 },
    ],
  };

 export default function () {
    let res = http.get("http://localhost:8080/helloworld");
    // Validate response status
    check(res, { "status was 200": (r) => r.status == 200 ||  r.status == 429});
    sleep(1);
 }
  