window.onload = function() {
    var canvas = document.getElementById("fireworks");
    var ctx = canvas.getContext("2d");

    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    var particles = [];

    function Particle(x, y, color) {
        this.x = x;
        this.y = y;
        this.size = Math.random() * 5 + 1;
        this.speed = Math.random() * 3 + 1;
        this.direction = Math.random() * Math.PI * 2;
        this.color = color;
        this.alpha = 1; // Độ mờ dần của các particle
    }

    Particle.prototype.update = function() {
        this.x += this.speed * Math.cos(this.direction);
        this.y += this.speed * Math.sin(this.direction);
        this.size *= 0.95; // Giảm kích thước theo thời gian
        this.alpha -= 0.02; // Dần biến mất

        // Nếu particle quá mờ hoặc quá nhỏ, loại bỏ nó
        if (this.alpha <= 0 || this.size <= 0.1) {
            return false;
        }
        return true;
    };

    // Vẽ particle lên canvas
    Particle.prototype.draw = function() {
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2);
        ctx.fillStyle = this.color;
        ctx.globalAlpha = this.alpha;
        ctx.fill();
    };

    // Tạo hiệu ứng pháo hoa
    function createFirework(x, y) {
        var numParticles = 100; // Số lượng particle trong một vụ nổ
        for (var i = 0; i < numParticles; i++) {
            var color = "hsl(" + Math.random() * 360 + ", 100%, 50%)";
            var particle = new Particle(x, y, color);
            particles.push(particle);
        }
    }

    // Hàm tạo ra pháo hoa liên tục
    function generateContinuousFireworks() {
        setInterval(function() {
            var x = Math.random() * canvas.width;
            var y = Math.random() * canvas.height;
            createFirework(x, y);
        }, 300); // Tạo pháo hoa mỗi 0.3 giây (300ms)
    }

    // Gọi hàm tạo pháo hoa liên tục khi trang tải
    generateContinuousFireworks();

    // Cập nhật và vẽ các particle
    function animate() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        // Cập nhật và vẽ tất cả particle
        particles = particles.filter(function(particle) {
            if (!particle.update()) return false;
            particle.draw();
            return true;
        });

        requestAnimationFrame(animate);
    }

    animate();
};

document.getElementById('changeSale').addEventListener('click' , function (){
    const urlParams = new URLSearchParams(window.location.search);
    const idUser = localStorage.getItem('idUserNoLogin');
    const userName = localStorage.getItem('userNameNoLogin');
    const note = localStorage.getItem('noteNoLogin');
    const phoneNumber = localStorage.getItem('phoneNumberNoLogin');
    const email = localStorage.getItem('emailNoLogin');
    const itemDiscount = localStorage.getItem('itemDiscountNoLogin');
    const moneyShip = localStorage.getItem('moneyShipNoLogin');
    const address = localStorage.getItem('addressNoLogin');
    const dateShip = localStorage.getItem('shipDateNoLogin');
    const idVoucher = localStorage.getItem('voucherIdNoLogin');
    const afterPrice = localStorage.getItem('afterPriceNoLogin');
    const billDetail = JSON.parse(localStorage.getItem('billDetailNoLogin')) || [];
    const totalItemNoLogin = localStorage.getItem('totalItemNoLogin');
    const response = {
        idUser: idUser,
        userName: userName,
        note: note,
        phoneNumber: phoneNumber,
        email: email,
        itemDiscount: itemDiscount,
        moneyShip: moneyShip,
        address: address,
        shipDate: dateShip,
        totalPrice : totalItemNoLogin ,
        idVoucher : idVoucher ,
        billDetail : billDetail,
        afterPrice : afterPrice ,
        vnp_Amount: urlParams.get('vnp_Amount'),
        vnp_BankCode: urlParams.get('vnp_BankCode'),
        vnp_BankTranNo: urlParams.get('vnp_BankTranNo'),
        vnp_CardType: urlParams.get('vnp_CardType'),
        vnp_OrderInfo: urlParams.get('vnp_OrderInfo'),
        vnp_PayDate: urlParams.get('vnp_PayDate'),
        vnp_ResponseCode: urlParams.get('vnp_ResponseCode'),
        vnp_TmnCode: urlParams.get('vnp_TmnCode'),
        vnp_TransactionNo: urlParams.get('vnp_TransactionNo'),
        vnp_TransactionStatus: urlParams.get('vnp_TransactionStatus'),
        vnp_TxnRef: urlParams.get('vnp_TxnRef'),
        vnp_SecureHash: urlParams.get('vnp_SecureHash'),
    }
    console.log("Check repon ửi đi " , response)
    if (response.vnp_ResponseCode === "00") {

        axios.post('/vnpay-success-no-login', response)
            .then(response => {
                console.log("Thanh toán thành công", response.data)
                // localStorage.removeItem('idUser');
                // localStorage.removeItem('userName');
                // localStorage.removeItem('note');
                // localStorage.removeItem('phoneNumber');
                // localStorage.removeItem('email');
                // localStorage.removeItem('openDelivery');
                // localStorage.removeItem('itemDiscount');
                // localStorage.removeItem('moneyShip');
                // localStorage.removeItem('type');
                // localStorage.removeItem('address');
                // localStorage.removeItem('deliveryDate');
                // localStorage.removeItem('voucherDetails');
            })
            .catch(error => {
                console.log("Error in save", error)
            })
    } else {
        console.log("Thanh toán không thành cng", response.vnp_ResponseCode)
    }
})