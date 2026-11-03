
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000b8067ce8(int param_1)

{
  int iVar1;
  
  if ((_DAT_ffff0000132604a0 >> 0x18 & 1) == 0) {
    FUN_ffff0000b804c33c(s__SEC__No_need_to_fill_MD1_pubk_h_ffff0000b80a0bbc);
  }
  else {
    iVar1 = FUN_ffff0000b8069da4();
    if (iVar1 != 0x1000000) {
      return 0x10100e;
    }
    if (param_1 == 0x1000000) {
      FUN_ffff0000b8067ac0();
    }
  }
  return 0;
}


