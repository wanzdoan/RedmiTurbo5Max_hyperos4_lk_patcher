// ffff000090788998  size=20  FUN_ffff000090788998

undefined8 FUN_ffff000090788998(int param_1)

{
  undefined8 uVar1;
  
  if (param_1 - 4U < 0xfffffffe) {
    FUN_ffff00009077e11c(s_Read_otp_version__Not_avb_or_rec_ffff0000907d8639);
    return 0x104009;
  }
  uVar1 = FUN_ffff0000907abc74();
  return uVar1;
}


