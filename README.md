Cách chạy Backend:
- cd back-end
- Sửa file application.yml thay thế giá trị datasource bằng url database của bạn
- Chạy migrate để init database
- Chạy ứng dụng

Cách chạy Front-end:
- cd front-end
- Chuyển registry về private registry bằng các câu lệnh
  - npm config set -- //git.aggregatoricapaci.com/api/v4/projects/110/packages/npm/:_authToken="cxo9gH2wiw61YcUAFVpo"
  - npm config set @cardoctor:registry=https://git.aggregatoricapaci.com/api/v4/projects/110/packages/npm/
  - npm config set -- //git.aggregatoricapaci.com/api/v4/projects/116/packages/npm/:_authToken="NL_sMsVfECyainBxnKbt"
  - npm config set @cd:registry=https://git.aggregatoricapaci.com/api/v4/projects/116/packages/npm/
- Đổi env `VITE_GARAGE_SERVICE` thành địa chỉ Back-end vừa chạy
- Chạy ứng dụng bằng cách ```npm run build && npm run dev```
