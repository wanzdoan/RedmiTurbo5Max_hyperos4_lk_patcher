// ffff0000907886d8  size=20  FUN_ffff0000907886d8

undefined8 FUN_ffff0000907886d8(int param_1)

{
  undefined8 uVar1;
  
  if (param_1 - 4U < 0xfffffffe) {
    FUN_ffff00009077de54(s_Read_otp_version__Not_avb_or_rec_ffff0000907d85a3);
    return 0x104009;
  }
  uVar1 = FUN_ffff0000907ab9b4();
  return uVar1;
}


