
void FUN_ffff0000be0994ac(ulong param_1,long param_2)

{
  long lVar1;
  bool bVar2;
  ulong uVar3;
  
  FUN_ffff0000be01a6f8(&DAT_ffff0000be1b7de8);
  bVar2 = (param_1 & 7) != 0;
  uVar3 = param_1 + 7 & 0xfffffffffffffff8;
  DAT_ffff0000be1b7dc8 = param_1;
  if (bVar2) {
    DAT_ffff0000be1b7dc8 = uVar3;
  }
  lVar1 = 0;
  if (bVar2) {
    lVar1 = param_1 - uVar3;
  }
  DAT_ffff0000be1b7dd8 = 0;
  DAT_ffff0000be1b7de0 = 0;
  DAT_ffff0000be1b7dd0 = lVar1 + param_2;
  DAT_ffff0000be1b7e20 = &DAT_ffff0000be1b7e20;
  DAT_ffff0000be1b7e28 = &DAT_ffff0000be1b7e20;
  if (DAT_ffff0000be1b7dd0 != 0) {
    *(long *)(DAT_ffff0000be1b7dc8 + 0x10) = DAT_ffff0000be1b7dd0;
    FUN_ffff0000be0993a0();
    return;
  }
  return;
}


