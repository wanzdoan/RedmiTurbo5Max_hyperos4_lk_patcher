
/* WARNING: Removing unreachable block (ram,0xffff0000be035958) */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be035904(uint param_1)

{
  uint uVar1;
  
  uVar1 = _DAT_ffff00001c010048;
  _DAT_ffff00001c010048 = 0x95000000;
  FUN_ffff0000be05fd98(s_RGU_DFD_V2_ffff0000be0b8bea);
  _DAT_ffff00001c010048 = param_1 & 0xfffffff | 0x90000000 | uVar1 & 0x60000000;
  return;
}


