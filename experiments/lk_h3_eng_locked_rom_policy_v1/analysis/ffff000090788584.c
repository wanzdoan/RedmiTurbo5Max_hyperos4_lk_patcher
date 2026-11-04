
undefined8 FUN_ffff000090788584(int param_1)

{
  undefined8 uVar1;

  if (param_1 - 4U < 0xfffffffe) {
    FUN_ffff00009077ea84(s_Read_otp_version__Not_avb_or_rec_ffff0000907d09ca);
    return 0x104009;
  }
  uVar1 = FUN_ffff0000907abf84();
  return uVar1;
}
