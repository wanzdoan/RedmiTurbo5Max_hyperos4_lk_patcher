// ffff000090757fd8  size=248  FUN_ffff000090757fd8

undefined8 FUN_ffff000090757fd8(undefined4 param_1)

{
  long lVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  
  lVar3 = FUN_ffff0000907578a0();
  iVar2 = FUN_ffff000090757aec(param_1);
  lVar4 = 0xffffcd7b0000;
  lVar1 = lVar4;
  if (iVar2 != 0) {
    lVar1 = 0xffffcd3b0000;
  }
  iVar2 = FUN_ffff000090757aec(param_1);
  lVar1 = (lVar1 + lVar3 & 0xffffffff0000U) + (ulong)(uint)(iVar2 << 0x16);
  *(undefined4 *)(lVar1 + -0xffffcd7affe0) = 0;
  iVar2 = FUN_ffff000090757aec(param_1);
  if (iVar2 != 0) {
    lVar4 = 0xffffcd3b0000;
  }
  iVar2 = FUN_ffff000090757aec(param_1);
  lVar4 = (lVar4 + lVar3 & 0xffffffff0000U) + (ulong)(uint)(iVar2 << 0x16);
  iVar2 = 0x7d1;
  *(undefined4 *)(lVar4 + -0xffffcd7affdc) = 1;
  *(undefined4 *)(lVar4 + -0xffffcd7affdc) = 0;
  do {
    if ((*(uint *)(lVar4 + -0xffffcd7afdc0) & 3) != 0) break;
    FUN_ffff00009077cbd8(10);
    iVar2 = iVar2 + -1;
  } while (iVar2 != 0);
  *(undefined4 *)(lVar1 + -0xffffcd7afffc) = 0;
  *(undefined4 *)(lVar1 + -0xffffcd7afff8) = 0;
  FUN_ffff0000907548b0(param_1);
  return 0;
}


