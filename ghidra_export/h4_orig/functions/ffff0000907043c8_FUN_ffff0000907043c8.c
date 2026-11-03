// ffff0000907043c8  size=144  FUN_ffff0000907043c8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff0000907043c8(undefined4 param_1,int param_2,int param_3,uint param_4)

{
  uint uVar1;
  ulong uVar2;
  byte local_24 [4];
  
  local_24[0] = 0;
  uVar2 = FUN_ffff00009074046c(_DAT_ffff00009083d060,param_1,local_24,1);
  if (-1 < (int)uVar2) {
    local_24[0] = local_24[0] & ((byte)(param_3 << (ulong)(param_4 & 0x1f)) ^ 0xff) |
                  (byte)(param_2 << (ulong)(param_4 & 0x1f));
    uVar1 = FUN_ffff0000907404b4(_DAT_ffff00009083d060,param_1,local_24,1);
    uVar2 = (ulong)(uVar1 & (int)uVar1 >> 0x1f);
  }
  return uVar2;
}


