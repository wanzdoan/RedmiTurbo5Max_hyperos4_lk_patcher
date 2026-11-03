// ffff0000907a4030  size=232  FUN_ffff0000907a4030

void FUN_ffff0000907a4030(ulong *param_1,uint *param_2)

{
  ulong uVar1;
  uint uVar2;
  ulong uVar3;
  int iVar4;
  undefined1 local_28;
  undefined1 local_27;
  undefined1 local_26;
  undefined1 local_25;
  undefined1 local_24;
  undefined1 local_23;
  undefined1 local_22;
  undefined1 local_21;
  
  uVar1 = *param_1;
  uVar3 = param_1[1] << 3 | uVar1 >> 0x1d;
  iVar4 = 0x38;
  if (0x37 < (uVar1 & 0x3f)) {
    iVar4 = 0x78;
  }
  local_24 = (undefined1)(uVar1 >> 0x15);
  local_25 = (undefined1)uVar3;
  local_28 = (undefined1)(uVar3 >> 0x18);
  local_23 = (undefined1)(uVar1 >> 0xd);
  local_27 = (undefined1)(uVar3 >> 0x10);
  local_26 = (undefined1)(uVar3 >> 8);
  local_22 = (undefined1)(uVar1 >> 5);
  local_21 = (undefined1)((int)uVar1 << 3);
  FUN_ffff0000907a2bac(param_1,&DAT_ffff000090812963,iVar4 - (int)(uVar1 & 0x3f));
  FUN_ffff0000907a2bac(param_1,&local_28,8);
  uVar2 = ((uint)param_1[2] & 0xff00ff00) >> 8 | ((uint)param_1[2] & 0xff00ff) << 8;
  *param_2 = uVar2 >> 0x10 | uVar2 << 0x10;
  uVar2 = ((uint)param_1[3] & 0xff00ff00) >> 8 | ((uint)param_1[3] & 0xff00ff) << 8;
  param_2[1] = uVar2 >> 0x10 | uVar2 << 0x10;
  uVar2 = ((uint)param_1[4] & 0xff00ff00) >> 8 | ((uint)param_1[4] & 0xff00ff) << 8;
  param_2[2] = uVar2 >> 0x10 | uVar2 << 0x10;
  uVar2 = ((uint)param_1[5] & 0xff00ff00) >> 8 | ((uint)param_1[5] & 0xff00ff) << 8;
  param_2[3] = uVar2 >> 0x10 | uVar2 << 0x10;
  uVar2 = ((uint)param_1[6] & 0xff00ff00) >> 8 | ((uint)param_1[6] & 0xff00ff) << 8;
  param_2[4] = uVar2 >> 0x10 | uVar2 << 0x10;
  return;
}


