java -jar git-to-consul-1.0.0.jar <host> <port> <token> <path-to-yaml-file> <consul-key>

VD: 
java -jar git-to-consul-1.0.0.jar --host http://10.40.81.2 --port 7510 --token 43c2237e-6ded-9ab6-7f2f-b3bfc0b4ab34 --yml-file ./td-conf-app-client-topup/pup/qc/application.yaml --consul-path /transfer/study/pub/qc/2.0.1-4/application