const mysql=require('mysql');
const express=require('express');
const bodyparser=require('body-parser');
const bcrypt = require("bcryptjs");
var app=express();

app.use(bodyparser.json());
var mysqlConn=mysql.createConnection({
	host:'localhost',
	user:'root',
	password:'nguyenvanthieu99',
	database:'GRAB_SYSTEM_DATABASE'
	});
mysqlConn.connect((err)=>{

	if (err) throw err;
	console.log('Connected');
});

app.listen(3000,()=>console.log('Server is running at port 3000'));


//Show all Taikhoan -- debug
app.get('/Taikhoan',(req,res)=>{
	mysqlConn.query('Select * from Taikhoan', (err,rows)=>
	{
		if (err) res.send(err.code);
		else
			res.send(rows);	
	});
});



//*****************************************************
//****************LOGIN / REGISTER***************************


//register -- add a Taikhoan
app.get('/Taikhoan/register/:acct/:pass/:acct_type',(req,res)=>
{	
	bcrypt.hash(req.params.pass, 10, function(err, hashed_pass) {
	  	// Store hash in database
		mysqlConn.query('Insert into TaiKhoan value(?,?,?)',[req.params.acct, hashed_pass, req.params.acct_type],(err,rows)=>
		{
			if (err) 
			{
				res.send(err.code);
			}
			else
			{
				if (req.params.acct_type == 0) // Tai xe
				{
					mysqlConn.query('Insert into TaiXe (TenTaiKhoanTaiXe) value(?)',[req.params.acct],(err_tx,rows)=>
					{
						if (err_tx)
							res.send(err_tx.code);
					});

					mysqlConn.query('Insert into Xe (TenTaiKhoanTaiXe) value(?)',[req.params.acct],(err_xe,rows)=>
					{
						if (err_xe)
							res.send(err_xe.code);
					});
					res.send('Register successfully');
				}	
				else
					mysqlConn.query('Insert into KhachHang (TenTaiKhoanKhachHang) value(?)',[req.params.acct],(err_kh,rows)=>
					{
						if (err_kh)
							res.send(err_kh.code);
						else
				 			res.send('Register successfully');
					});						
			}
		});
	});	
});



//login -- add a Taikhoan
app.get('/Taikhoan/login/:acct/:pass/:acct_type',(req,res)=>
{	
	var query = 'Select * from Taikhoan where TenTaiKhoan = ' + mysql.escape(req.params.acct) + ' and LoaiTaiKhoan = ' + req.params.acct_type;

	mysqlConn.query(query, (err,rows)=>
	{
		if (err) res.send(err.code);
		else
		{
			if (!rows.length)                                     
				res.send('Sai tai khoan hoac mat khau!');
			else
				bcrypt.compare(req.params.pass, rows[0].MatKhau, function(err_cmp, res_cmp) 
				{
					if(res_cmp) 
					{
					// Passwords match
						res.send('Login successfully');

					} else 
					{
					// Passwords don't match
						//res_cmp.send(err_cmp.code);
					res.send('Sai tai khoan hoac mat khau!');
					} 
				});
		}
	});
});




//*****************************************************
//****************CHANGE PASSWORD***************************

//change password -- update a Taikhoan
app.get('/Taikhoan/change_password/:acct/:old_pass/:new_pass',(req,res)=>
{	
	var query = 'Select * from Taikhoan where TenTaiKhoan = ' + mysql.escape(req.params.acct);

	mysqlConn.query(query, (err,rows)=>
	{
		if (err) res.send(err.code);
		else
		{
			bcrypt.compare(req.params.old_pass, rows[0].MatKhau, function(err_cmp, res_cmp) 
			{
				if(res_cmp) 
				{
				// Passwords match

					bcrypt.hash(req.params.new_pass, 10, function(hash_new_pass_err, hash_new_pass) {
					  	// Store hash in database
						var query_change = 'Update Taikhoan Set MatKhau = ' + mysql.escape(hash_new_pass) + ' Where TenTaiKhoan = ' + mysql.escape(req.params.acct);

						mysqlConn.query(query_change, (err_update,rows_update)=>
						{
							if (err_update) res.send(err_update.code);
							else
							res.send('Change successfully');
						});
					});
				} 
				else 
				{
				// Passwords don't match
					//res_cmp.send(err_cmp.code);
					res.send('Wrong password!');
				} 
			});
		}
	});
});


//*****************************************************
//****************INFOMATION***************************

//get Infomation of TaiXe
app.get('/TaiXe/:acct/',(req,res)=>{
	mysqlConn.query('Select HoTen, NgaySinh, GioiTinh, SoDT, DiaChi, CMND from TaiXe where TenTaiKhoanTaiXe = '  + mysql.escape(req.params.acct), (err,rows)=>
	{
		if (err) res.send(err.code);
		else
			res.send(rows[0]);	
	});
});

//get Infomation of Xe
app.get('/TaiXe/Xe/:acct/',(req,res)=>{
	mysqlConn.query('Select LoaiXe, BienSoXe from Xe where TenTaiKhoanTaiXe = '  + mysql.escape(req.params.acct), (err,rows)=>
	{
		if (err) res.send(err.code);
		else
			res.send(rows[0]);	
	});
});

//change Infomation -- update a TaiXe, a Xe
app.get('/TaiXe/change_information/:acct/:HoTen/:NgaySinh/:GioiTinh/:SoDT/:DiaChi/:CMND/:LoaiXe/:BienSoXe',(req,res)=>
{	
	var query_change = 'Update TaiXe Set HoTen = ' + mysql.escape(req.params.HoTen) + 
					', NgaySinh = ' + mysql.escape(req.params.NgaySinh) + 
					', GioiTinh = ' + req.params.GioiTinh + 
					', SoDT = ' + mysql.escape(req.params.SoDT) + 
					', DiaChi = ' + mysql.escape(req.params.DiaChi) + 
					', CMND = ' + mysql.escape(req.params.CMND) + 
			' Where TenTaiKhoanTaiXe = ' + mysql.escape(req.params.acct);

	var query_xe_change = 'Update Xe Set LoaiXe = ' + mysql.escape(req.params.LoaiXe) + 
					', BienSoXe = ' + mysql.escape(req.params.BienSoXe) + 
			' Where TenTaiKhoanTaiXe = ' + mysql.escape(req.params.acct);


	mysqlConn.query(query_change, (err_update,rows_update)=>
	{
		if (err_update) res.send(err_update.code);
	});


	mysqlConn.query(query_xe_change, (err_xe_update,rows_xe_update)=>
	{
		if (err_xe_update) res.send(err_xe_update.code);
		else
			res.send('Change successfully');
	});
});



//get Infomation of KhachHang
app.get('/KhachHang/:acct/',(req,res)=>{
	mysqlConn.query('Select HoTen, NgaySinh, GioiTinh, SoDT, DiaChi from KhachHang where TenTaiKhoanKhachHang = '  + mysql.escape(req.params.acct), (err,rows)=>
	{
		if (err) res.send(err.code);
		else
			res.send(rows[0]);	
	});
});

//change Infomation -- update a KhachHang
app.get('/KhachHang/change_information/:acct/:HoTen/:NgaySinh/:GioiTinh/:SoDT/:DiaChi',(req,res)=>
{	
	var query_change = 'Update KhachHang Set HoTen = ' + mysql.escape(req.params.HoTen) + 
					', NgaySinh = ' + mysql.escape(req.params.NgaySinh) + 
					', GioiTinh = ' + req.params.GioiTinh + 
					', SoDT = ' + mysql.escape(req.params.SoDT) + 
					', DiaChi = ' + mysql.escape(req.params.DiaChi) +  
			' Where TenTaiKhoanKhachHang = ' + mysql.escape(req.params.acct);

	mysqlConn.query(query_change, (err_update,rows_update)=>
	{
		if (err_update) res.send(err_update.code);
		else
			res.send('Change successfully');
	});
});


//*****************************************************
//****************TURN MODE****************************
// turn on ready mode
app.get('/TaiXe/turnOnReadyMode/:acct/:KinhDo/:ViDo',(req,res)=>
{	
	var query_loaixe = 'Select LoaiXe from Xe Where TenTaiKhoanTaiXe = ' + mysql.escape(req.params.acct);

	mysqlConn.query(query_loaixe, (err_lx,rowsLoaiXe)=>
	{
		if (err_lx) res.send(err_lx.code);
		else
		{
			mysqlConn.query('Insert into HienTaiSanSang (TenTaiKhoanTaiXe, LoaiXe, TrangThai, KinhDo, ViDo) value (?,?,?,?,?)',[req.params.acct,rowsLoaiXe[0],0,req.params.KinhDo,req.params.ViDo],(err,rows)=>
			{
				if (err) res.send(err.code);
				else 	res.send('Turn on successfully');
			});
		}
	});
});

// turn off ready mode
app.get('/TaiXe/turnOffReadyMode/:acct',(req,res)=>
{	
	mysqlConn.query('Delete from HienTaiSanSang where TenTaiKhoanTaiXe = ' + mysql.escape(req.params.acct),(err,rows)=>
	{
		if (err) res.send(err.code);
		else 	res.send('Turn off successfully');
	});
});



//*****************************************************
//****************MAIN FLOW****************************
// get the cost
app.get('/GiaXe/:LoaiXe',(req,res)=>
{	
	var query = 'Select GiaXe from GiaXe Where LoaiXe = ' + req.params.LoaiXe;

	mysqlConn.query(query, (err,rows)=>
	{
		if (err) res.send(err.code);
		else
		{
			if(!rows.length)
				res.send("Loai xe nay chua duoc them");
			else
				res.send(rows[0]);

		}
	});
});


//********************************************************
//****************KHACH DAT XE****************************

// Yeu cau dat xe
app.get('/YeuCauDatXe/:acct/:LoaiXe/:KinhDoDi/:ViDoDi/:DiaDiemDi/:DiaDiemDen/:KhoangCach/:Gia/:TrangThai/:ThoiGianDatXe',(req,res)=>
{	
	mysqlConn.query('Select SoDT from KhachHang where TenTaiKhoanKhachHang = ' + mysql.escape(req.params.acct),(err_sdt,rows_sdt)=>
	{
		if (err_sdt) res.send(err_sdt.code);
		else
		{	
			console.log("Yeu Cau dat xe cua khach");

			if (rows_sdt[0].SoDT == null)
				res.send("Yeu cau them sdt truoc khi dat xe");			
			else
			mysqlConn.query('Insert into YeuCauDatXe (TenTaiKhoanKhachHang, SoDT, LoaiXe, KinhDoDi, ViDoDi, DiaDiemDi, DiaDiemDen, KhoangCach, Gia, TrangThai, ThoiGianDatXe) value(?,?,?,?,?,?,?,?,?,?,?)', 
								[req.params.acct, rows_sdt[0].SoDT,req.params.LoaiXe, req.params.KinhDoDi, req.params.ViDoDi, req.params.DiaDiemDi, req.params.DiaDiemDen, req.params.KhoangCach, req.params.Gia, req.params.TrangThai, req.params.ThoiGianDatXe],(err,rows)=>
			{
				if (err) res.send(err.code);
				else
					res.send('Order successfully');
			});
		}
	});


});

// Kiem tra tinh trang request
app.get('/YeuCauDatXe/Check/:acct',(req,res)=>
{	
	console.log("Khach Check");	

	mysqlConn.query('Select TrangThai from YeuCauDatXe where TenTaiKhoanKhachHang = ' + mysql.escape(req.params.acct),(err,rows)=>
	{
		if (err) res.send(err.code);
		else
		{	if (rows[0].TrangThai == 0)
				res.send('Waiting');
			if (rows[0].TrangThai == 1)
				res.send('Waiting driver accept'); // do somethings
			if (rows[0].TrangThai == 2) // Da accept
			{
				

				mysqlConn.query('Select * from ChuyenXe where TenTaiKhoanKhachHang = ' + mysql.escape(req.params.acct) + ' order by IDChuyenXe DESC',(err_ID,rows_ID)=>
				{
					if (err_ID) res.send(err.code_ID);
					else
					{
						res.send(rows_ID[0].TenTaiKhoanTaiXe);
	
					}
				});

			}


		}
	});


});

// request thong tin tai xe da accept
app.get('/YeuCauThongTinTaiXe/:TenTaiKhoanTaiXe',(req,res)=>
{	
console.log(req.params.TenTaiKhoanTaiXe);
	mysqlConn.query('Select TaiXe.HoTen, TaiXe.NgaySinh, TaiXe.SoDT, Xe.BienSoXe from TaiXe join Xe on TaiXe.TenTaiKhoanTaiXe = Xe.TenTaiKhoanTaiXe where TaiXe.TenTaiKhoanTaiXe = ' + mysql.escape(req.params.TenTaiKhoanTaiXe),(err,rows)=>
	{
		if (err) {res.send(err.code);}
		else
		{
			res.send(rows[0]);
		}
	});
});

//********************************************************
//****************TAI XE NHAN XE****************************

// check nhan request
app.get('/YeuCauDatXe/DriverCheck/:acct/:KinhDoHienTai/:ViDoHienTai',(req,res)=>
{	
	console.log("Driver check");

	mysqlConn.query('Select * from Xe where TenTaiKhoanTaiXe = ' + mysql.escape(req.params.acct),(err_getXe,rows_getXe)=>
	{
		if (err_getXe) res.send(err_getXe.code);
		else
		{
			if (rows_getXe[0].LoaiXe == null)
				res.send('Yeu cau them thong tin xe truoc');
			else
			{

				mysqlConn.query('Select * from YeuCauDatXe where TrangThai = 0 and LoaiXe = ' + mysql.escape(rows_getXe[0].LoaiXe) + ' order by sqrt(pow(KinhDoDi - ' + req.params.KinhDoHienTai + ', 2) + pow(ViDoDi - ' + req.params.ViDoHienTai + ', 2)) ASC',(err,rows)=>
				{
					if (err) res.send(err.code);
					else
					{
						if (!rows.length)
							res.send('Please wait');
						else
						{

							var query_change = 'Update YeuCauDatXe Set TrangThai = 1 where TenTaiKhoanKhachHang = ' + mysql.escape(rows[0].TenTaiKhoanKhachHang);

							mysqlConn.query(query_change, (err_update,rows_update)=>
							{
								if (err_update) res.send(err_update.code);
								else
									res.send(rows[0]);
							});
			
						}
					}
				});
			
			}
		}
	});	

});


// tu choi request
app.get('/YeuCauDatXe/DriverDeny/:acct/:TenTaiKhoanKhachHang',(req,res)=>
{	
	var query_change = 'Update YeuCauDatXe Set TrangThai = 0 where TenTaiKhoanKhachHang = ' + mysql.escape(req.params.TenTaiKhoanKhachHang);

	mysqlConn.query(query_change, (err_update,rows_update)=>
	{
		if (err_update) res.send(err_update.code);
		else
			res.send('Deny successfully');
	});
			

});

// nhan request
app.get('/YeuCauDatXe/DriverAccept/:acct/:TenTaiKhoanKhachHang',(req,res)=>
{	

	var query_IDChuyenXe = 'Select IDChuyenXe from ChuyenXe order by IDChuyenXe DESC';

	mysqlConn.query(query_IDChuyenXe, (err_chuyenXe,rows_getID)=>
	{
		if (err_chuyenXe) res.send(err_chuyenXe.code);
		else
		{
			var newID = 1;
			if (rows_getID.length) newID = rows_getID[0].IDChuyenXe + 1;

			var query_ThongTinYeuCauDatXe = 'Select * from YeuCauDatXe where TenTaiKhoanKhachHang = '+ mysql.escape(req.params.TenTaiKhoanKhachHang);

			mysqlConn.query(query_ThongTinYeuCauDatXe, (err_ThongTinYeuCauDatXe,rows_ThongTinYeuCauDatXe)=>
			{
				if (err_ThongTinYeuCauDatXe) res.send(err_ThongTinYeuCauDatXe.code);
				else
				{

					mysqlConn.query('Insert into ChuyenXe (IDChuyenXe, TenTaiKhoanTaiXe, TenTaiKhoanKhachHang, DiaDiemDi, DiaDiemDen, ThoiGianDatXe, KhoangCach, Gia, TrangThai) value(?,?,?,?,?,?,?,?,?)'
							, [newID, req.params.acct, req.params.TenTaiKhoanKhachHang, rows_ThongTinYeuCauDatXe[0].DiaDiemDi, rows_ThongTinYeuCauDatXe[0].DiaDiemDen, rows_ThongTinYeuCauDatXe[0].ThoiGianDatXe, rows_ThongTinYeuCauDatXe[0].KhoangCach, rows_ThongTinYeuCauDatXe[0].Gia, 0],
								(err_insert,rows_insert)=>
					{
						if (err_insert) {console.log(err_insert.code);res.send(err_insert.code);}
						else
						{
							/*
							mysqlConn.query('Delete from YeuCauDatXe where TenTaiKhoanKhachHang = ' + mysql.escape(req.params.TenTaiKhoanKhachHang),(err,rows)=>
							{
								if (err) res.send(err.code);
								else 	
									res.send(rows_getID[0]);
							});
							*/

							var query_change = 'Update YeuCauDatXe Set TrangThai = 2 where TenTaiKhoanKhachHang = ' + mysql.escape(req.params.TenTaiKhoanKhachHang);


							mysqlConn.query(query_change, (err_update,rows_update)=>
							{
								if (err_update) res.send(err_update.code);
								else
									{res.send(rows_getID[0]);}
							});
							

						}

					});


				}
			});
		}
			
	});	

});


// Hoan thanh chuyen di
app.get('/ChuyenXe/:IDChuyenXe',(req,res)=>
{	
	var query_select = 'Select TenTaiKhoanKhachHang from ChuyenXe where IDChuyenXe = ' + req.params.IDChuyenXe + ' order by IDChuyenXe DESC';

	mysqlConn.query(query_select, (err_select,rows_select)=>
	{
		if (err_select) {res.send(err_select.code);}
		else
			mysqlConn.query('Delete from YeuCauDatXe where TenTaiKhoanKhachHang = ' + mysql.escape(rows_select[0].TenTaiKhoanKhachHang),(err_delete,rows_delete)=>
			{
				if (err_delete) {res.send(err_delete.code);}
			});

	});

	var query_change = 'Update ChuyenXe Set TrangThai = 1 where IDChuyenXe = ' + req.params.IDChuyenXe;

	mysqlConn.query(query_change, (err_update,rows_update)=>
	{
		if (err_update) {res.send(err_update.code);}
		else
			{res.send('Finished');}
	});
});