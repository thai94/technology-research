java -jar git-to-consul-1.0.0.jar <service-name> <version> <env> <work-space-dir> <sync-type>

<service-name>: Giá trị này phải khớp với thông tin service-name được cấu hình trong file application.yaml
<version>: git release version. Format: x.y.z-<incresing number>
<env>: xác định môi trường nào sẽ được update config
<work-space-dir>: Nơi chứa config để sync lên consul
<sync-type>: chỉ sync pub config hoặc chỉ sync secret config hoặc cả 2.

VD: 
java -jar git-to-consul-1.0.0.jar sample 2.0.0-200 qc ./resources/transfer/study all