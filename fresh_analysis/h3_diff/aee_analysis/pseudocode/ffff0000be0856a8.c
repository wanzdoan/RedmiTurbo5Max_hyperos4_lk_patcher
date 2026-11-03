
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000be0856a8(int param_1)

{
  int iVar1;
  
  if ((_DAT_ffff0000132604a0 >> 0x18 & 1) == 0) {
    FUN_ffff0000be05fd98(s__SEC__No_need_to_fill_MD1_pubk_h_ffff0000be0c5c3c);
  }
  else {
    iVar1 = FUN_ffff0000be0877a0();
    if (iVar1 != 0x1000000) {
      return 0x10100e;
    }
    if (param_1 == 0x1000000) {
      FUN_ffff0000be085480();
    }
  }
  return 0;
}


