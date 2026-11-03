// ffff00009079101c  size=232  FUN_ffff00009079101c

void FUN_ffff00009079101c(long *param_1,undefined4 *param_2)

{
  uint uVar1;
  int iVar2;
  undefined8 uVar3;
  undefined4 uVar4;
  long lVar5;
  long lVar6;
  
  lVar6 = param_1[0xf];
  uVar4 = *param_2;
  uVar1 = param_2[1];
  *(undefined4 **)(lVar6 + 0x20) = param_2;
  *(char *)(lVar6 + 0x40) = (char)uVar4;
  *(undefined1 *)(lVar6 + 0x41) = 1;
  *(undefined4 *)(lVar6 + 0x38) = 0;
  *(uint *)(lVar6 + 0x3c) = uVar1;
  uVar3 = FUN_ffff000090791104(param_1,lVar6);
  FUN_ffff00009079087c(uVar3,lVar6);
  iVar2 = 1 << (ulong)(uVar1 & 0x1f);
  uVar3 = *(undefined8 *)(param_1[0xf] + 8);
  param_1[0x30] = param_1[0x30] | (long)iVar2;
  FUN_ffff000090707c50(uVar3,0x20);
  FUN_ffff000090707c90(*(undefined8 *)(param_1[0xf] + 0x10),0x34);
  FUN_ffff000090707c50(*(undefined8 *)(param_1[0xf] + 0x18),0x800);
  FUN_ffff000090707c70(*(undefined8 *)param_1[0xf],0x20);
  lVar5 = *param_1;
  if (*(int *)(lVar5 + 0x20) != 0) {
    *(int *)(lVar5 + 0x20) = *(int *)(lVar5 + 0x20);
  }
  *(int *)(lVar5 + 0x58) = iVar2;
  uVar4 = 60000;
  if (*(char *)((long)param_2 + 0xd) != 'B') {
    uVar4 = 10000;
  }
  FUN_ffff0000907909e0(param_1,lVar6,uVar4);
  return;
}


