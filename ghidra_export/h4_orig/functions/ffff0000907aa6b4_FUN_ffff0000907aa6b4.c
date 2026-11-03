// ffff0000907aa6b4  size=116  FUN_ffff0000907aa6b4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907aa6b4(int param_1)

{
  int iVar1;
  
  if ((_DAT_ffff0000132604a0 >> 0x18 & 1) == 0) {
    FUN_ffff00009077e11c(s__SEC__No_need_to_fill_MD1_pubk_h_ffff000090801aee);
  }
  else {
    iVar1 = FUN_ffff0000907ad5ac();
    if (iVar1 != 0x1000000) {
      return 0x10100e;
    }
    if (param_1 == 0x1000000) {
      FUN_ffff0000907aa48c();
    }
  }
  return 0;
}


