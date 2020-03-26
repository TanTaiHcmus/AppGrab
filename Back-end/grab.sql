CREATE database IF NOT EXISTS GRAB_SYSTEM_DATABASE;
USE GRAB_SYSTEM_DATABASE;

CREATE TABLE IF NOT EXISTS TaiKhoan (
    TenTaiKhoan VARCHAR(20) NOT NULL,
    MatKhau VARCHAR(100) NOT NULL,
    LoaiTaiKhoan INT NOT NULL,
    PRIMARY KEY (TenTaiKhoan)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS GiaXe (
    LoaiXe INT NOT NULL,
    GiaXe FLOAT,
    PRIMARY KEY (LoaiXe)
) ENGINE=InnoDB;




CREATE TABLE IF NOT EXISTS TaiXe (
    TenTaiKhoanTaiXe VARCHAR(20) NOT NULL,
    HoTen VARCHAR(20),
    NgaySinh VARCHAR(20),
    GioiTinh INT,
    SoDT VARCHAR(15),
    DiaChi VARCHAR(45),
    CMND VARCHAR(15),
    AnhDaiDien INT,
    PRIMARY KEY (TenTaiKhoanTaiXe),
    CONSTRAINT fkTaiXe_TaiKhoan FOREIGN KEY (TenTaiKhoanTaiXe) REFERENCES TaiKhoan (TenTaiKhoan) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS KhachHang (
    TenTaiKhoanKhachHang VARCHAR(20) NOT NULL,
    HoTen VARCHAR(20),
    NgaySinh VARCHAR(20),
    GioiTinh INT,
    SoDT VARCHAR(15),
    DiaChi VARCHAR(45),
    AnhDaiDien INT,
    PRIMARY KEY (TenTaiKhoanKhachHang),
    CONSTRAINT fkKhachHang_TaiKhoan FOREIGN KEY (TenTaiKhoanKhachHang) REFERENCES TaiKhoan (TenTaiKhoan) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;






CREATE TABLE IF NOT EXISTS ChuyenXe (
    IDChuyenXe INT NOT NULL,
    TenTaiKhoanTaiXe VARCHAR(20) NOT NULL,
    TenTaiKhoanKhachHang VARCHAR(20) NOT NULL,
    DiaDiemDi VARCHAR(50) NOT NULL,
    DiaDiemDen VARCHAR(50) NOT NULL,
    ThoiGianDatXe CHAR(50),
    KhoangCach FLOAT NOT NULL,
    Gia FLOAT NOT NULL,
    TrangThai INT,
    PRIMARY KEY (IDChuyenXe),
    CONSTRAINT fkChuyenXe_TaiXe FOREIGN KEY (TenTaiKhoanTaiXe) REFERENCES TaiXe (TenTaiKhoanTaiXe) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fkChuyenXe_KhachHang FOREIGN KEY (TenTaiKhoanKhachHang) REFERENCES KhachHang (TenTaiKhoanKhachHang) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS Xe (
    BienSoXe VARCHAR(20),
    LoaiXe INT,
    TenTaiKhoanTaiXe VARCHAR(20) NOT NULL,
    PRIMARY KEY (TenTaiKhoanTaiXe),
    CONSTRAINT fkXe_TaiXe FOREIGN KEY (TenTaiKhoanTaiXe) REFERENCES TaiXe (TenTaiKhoanTaiXe) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fkXe_GiaXe FOREIGN KEY (LoaiXe) REFERENCES GiaXe (LoaiXe) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;





CREATE TABLE IF NOT EXISTS YeuCauDatXe (
    TenTaiKhoanKhachHang VARCHAR(20) NOT NULL,
    SoDT VARCHAR(15) NOT NULL,
    LoaiXe INT NOT NULL,

    KinhDoDi FLOAT NOT NULL,
    ViDoDi FLOAT NOT NULL,

    DiaDiemDi VARCHAR(50) NOT NULL,
    DiaDiemDen VARCHAR(50) NOT NULL,
    KhoangCach FLOAT NOT NULL,
    Gia FLOAT NOT NULL,
    TrangThai INT NOT NULL,
/*
    TrangThai là trạng thái của yêu cầu:
	0: Đang chờ nhận yêu cầu từ tài xế
	1: Đã gửi yêu cầu cho tài xế và chờ tài xế xác nhận
	2: Tài xế đã xác nhận chuyến đi và đang thực hiện chuyến đi
*/
    ThoiGianDatXe VARCHAR(50),

    PRIMARY KEY (TenTaiKhoanKhachHang),
    CONSTRAINT fkYeuCauDatXe_KhachHang FOREIGN KEY (TenTaiKhoanKhachHang) REFERENCES KhachHang (TenTaiKhoanKhachHang) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fkYeuCauDatXe_GiaXe FOREIGN KEY (LoaiXe) REFERENCES GiaXe (LoaiXe) ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;

/*
CREATE TABLE IF NOT EXISTS HienTaiSanSang (
    TenTaiKhoanTaiXe VARCHAR(20) NOT NULL,
    LoaiXe INT NOT NULL,
    TrangThai INT NOT NULL,
    KinhDo FLOAT NOT NULL,
    ViDo FLOAT NOT NULL,
    PRIMARY KEY (TenTaiKhoanTaiXe)
    CONSTRAINT fkHienTaiSanSang_TaiXe FOREIGN KEY (TenTaiKhoanTaiXe) REFERENCES TaiXe (TenTaiKhoanTaiXe) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;
*/


insert into grab_system_database.giaxe (LoaiXe, GiaXe) value(0, 15000); /*xe may*/
insert into grab_system_database.giaxe (LoaiXe, GiaXe) value(1, 50000); /*xe hoi*/