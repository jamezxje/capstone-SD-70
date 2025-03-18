const tokenApiGHN = '7d67a984-b5fe-11ef-b166-4205c1d15e61';
const shopId = '5511482';
const clientId = '4574315';
const urlProvince = 'https://online-gateway.ghn.vn/shiip/public-api/master-data/province';
const urlDistricts = 'https://online-gateway.ghn.vn/shiip/public-api/master-data/district';
const urlWard = 'https://online-gateway.ghn.vn/shiip/public-api/master-data/ward';
const urlMoneyShip = 'https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee';
const urlDayShip = 'https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/leadtime';
let provinceName = null;
let districtName = null;
let wardName = null;

// Lấy giá trị địa chỉ từ Thymeleaf
const selectedProvince = document.getElementById('provinceSelect').getAttribute("th:field");
const selectedDistrict = document.getElementById('districtSelect').getAttribute("th:field");
const selectedWard = document.getElementById('wardSelect').getAttribute("th:field");

// Load tỉnh/thành phố
fetch(urlProvince, { headers: { token: tokenApiGHN } })
    .then(response => response.json())
    .then(data => {
        const selectElement = document.getElementById('provinceSelect');
        data.data.forEach(province => {
            let option = new Option(province.ProvinceName, province.ProvinceID);
            if (province.ProvinceName === selectedProvince) option.selected = true;
            selectElement.add(option);
        });
    });

// Khi chọn tỉnh, load danh sách quận/huyện
document.getElementById('provinceSelect').addEventListener('change', function () {
    const provinceId = this.value;
    fetch(urlDistricts + `?province_id=${provinceId}`, { headers: { token: tokenApiGHN } })
        .then(response => response.json())
        .then(data => {
            const districtSelect = document.getElementById('districtSelect');
            districtSelect.innerHTML = '<option value="">Chọn</option>';
            data.data.forEach(district => {
                let option = new Option(district.DistrictName, district.DistrictID);
                if (district.DistrictName === selectedDistrict) option.selected = true;
                districtSelect.add(option);
            });
        });
});

// Khi chọn quận/huyện, load danh sách xã/phường
document.getElementById('districtSelect').addEventListener('change', function () {
    const districtId = this.value;
    fetch(urlWard + `?district_id=${districtId}`, { headers: { token: tokenApiGHN } })
        .then(response => response.json())
        .then(data => {
            const wardSelect = document.getElementById('wardSelect');
            wardSelect.innerHTML = '<option value="">Chọn</option>';
            data.data.forEach(ward => {
                let option = new Option(ward.WardName, ward.WardCode);
                if (ward.WardName === selectedWard) option.selected = true;
                wardSelect.add(option);
            });
        });
});

function fetchAllProvince() {
    axios.get(urlProvince, {
        headers: {
            token: tokenApiGHN,
        }
    })
        .then(response => {
            const proviences = response.data.data;
            const selectElement = document.getElementById('provinceSelect');
            proviences.forEach(province => {
                const option = document.createElement('option');
                option.value = province.ProvinceID;
                option.textContent = province.ProvinceName;
                selectElement.appendChild(option);

            })

            console.log(response.data);
        })
        .catch(error => {
            console.error('Có lỗi xảy ra:', error);
        });

}

function fetchProvinceDistricts(idProvince) {
    axios.get(urlDistricts, {
        params: {
            province_id: idProvince
        },
        headers: {
            token: tokenApiGHN,
        }

    })
        .then(response => {
            const districts = response.data.data;
            const districtSelect = document.getElementById('districtSelect');
            districtSelect.innerHTML = '<option value="">Chọn</option>';
            districts.forEach(district => {
                const option = document.createElement('option');
                option.value = district.DistrictID;
                option.textContent = district.DistrictName;
                districtSelect.appendChild(option);
            })

            if (idDistrictChose) {
                document.getElementById('districtSelect').value =idDistrictChose
                console.log("Check " ,idDistrictChose)
            }

            console.log("huyện", response.data)
        })
        .catch(error => {
            console.log("erorr", error)
        })

}

document.getElementById('provinceSelect').addEventListener('change', function () {
    const provinceId = this.value;
    const selectOption = this.options[this.selectedIndex];
    provinceName = selectOption.textContent || selectOption.innerText;
    console.log("Check prodvince id",provinceId);
    if (provinceId) {
        fetchProvinceDistricts(provinceId);
    } else {
        const districtSelect = document.getElementById('districtSelect');
        districtSelect.innerHTML = '<option value="">Chọn</option>';
    }
});

document.getElementById('districtSelect').addEventListener('change', function () {
    const districtID = this.value;
    const selectOption = this.options[this.selectedIndex];
    districtName = selectOption.textContent || selectOption.innerText;
    console.log("Check distric", districtID);
    if (districtID) {
        fetchProvinceWard(districtID);
    } else {
        const wardSelect = document.getElementById('wardSelect');
        wardSelect.innerHTML = '<option value="">Chọn</option>';
    }
})
let fullAddress = null;
document.getElementById('wardSelect').addEventListener('change', function () {
    const wardCode = this.value;
    const selectOption = this.options[this.selectedIndex]
    wardName = selectOption.textContent || selectOption.innerText;
    console.log("Check ward" , wardCode)
    const districtID = document.getElementById('districtSelect').value;
    if (districtID && wardCode) {
        fetchMoneyShip(districtID, wardCode, 1);
        fetchDayShip(districtID, wardCode);
        fetchAllAddress();
    } else {
        console.log(" NO id dis and ward")
    }

})

function fetchAllAddress() {
    const addressValue = document.getElementById('addressValue').value;

    if (provinceName && districtName && wardName) {
        const address = `${addressValue}, ${wardName}, ${districtName} , ${provinceName}`;
        fullAddress = address;
        console.log("Check địa chỉ đủ", fullAddress);
    } else {
        console.log("error adress")
    }
}

function fetchProvinceWard(idDistrict) {
    axios.get(urlWard, {
        params: {
            district_id: idDistrict
        },
        headers: {
            token: tokenApiGHN
        }
    })
        .then(response => {
            const wards = response.data.data;
            const wardSelect = document.getElementById('wardSelect');
            wardSelect.innerHTML = '<option value="">Chọn</option>';
            wards.forEach(ward => {
                const option = document.createElement('option');
                option.value = ward.WardCode;
                option.textContent = ward.WardName;
                wardName = ward.WardName;
                wardSelect.appendChild(option);
            })
            if (idWardCodeChose) {
                document.getElementById('wardSelect').value =idWardCodeChose
                console.log("Check " ,idWardCodeChose)
            }
            console.log("Xax", response.data);
        })
        .catch(error => {
            console.log("error", error)
        })

}