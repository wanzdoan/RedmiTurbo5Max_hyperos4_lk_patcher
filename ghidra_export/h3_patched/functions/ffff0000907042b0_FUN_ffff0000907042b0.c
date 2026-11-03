// ffff0000907042b0  size=108  FUN_ffff0000907042b0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907042b0(undefined4 param_1,undefined2 *param_2,int param_3,uint param_4)

{
  undefined8 uVar1;
  byte local_14 [4];
  
  local_14[0] = 0;
  uVar1 = FUN_ffff0000907401d4(_DAT_ffff00009083d0b8,param_1,local_14,1);
  if (-1 < (int)uVar1) {
    uVar1 = 0;
    *param_2 = (short)((param_3 << (ulong)(param_4 & 0x1f) & (uint)local_14[0]) >>
                      (ulong)(param_4 & 0x1f));
  }
  return uVar1;
}


