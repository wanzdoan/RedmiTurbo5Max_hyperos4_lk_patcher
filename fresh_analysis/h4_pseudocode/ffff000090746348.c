
/* WARNING: Removing unreachable block (ram,0xffff00009074639c) */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090746348(uint param_1)

{
  uint uVar1;
  
  uVar1 = _DAT_ffff00001c010048;
  _DAT_ffff00001c010048 = 0x95000000;
  FUN_ffff00009077e11c(s_RGU_DFD_V2_ffff0000907ee4db);
  _DAT_ffff00001c010048 = param_1 & 0xfffffff | 0x90000000 | uVar1 & 0x60000000;
  return;
}


