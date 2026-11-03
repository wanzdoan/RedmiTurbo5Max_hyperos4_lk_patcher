// ffff000090704174  size=68  FUN_ffff000090704174

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff000090704174(undefined4 param_1,undefined1 param_2)

{
  uint uVar1;
  undefined1 local_14 [4];
  
  local_14[0] = param_2;
  uVar1 = FUN_ffff0000907404b4(_DAT_ffff00009083d050,param_1,local_14,1);
  return uVar1 & (int)uVar1 >> 0x1f;
}


