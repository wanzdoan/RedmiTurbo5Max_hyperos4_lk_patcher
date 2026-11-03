// ffff0000907040f0  size=80  FUN_ffff0000907040f0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907040f0(undefined4 param_1,ushort *param_2)

{
  undefined8 uVar1;
  byte local_4 [4];
  
  local_4[0] = 0;
  uVar1 = FUN_ffff00009074046c(_DAT_ffff00009083d050,param_1,local_4,1);
  if (-1 < (int)uVar1) {
    uVar1 = 0;
    *param_2 = (ushort)local_4[0];
  }
  return uVar1;
}


