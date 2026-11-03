
undefined8 FUN_ffff0000be042dcc(undefined4 param_1)

{
  int iVar1;
  long lVar2;
  long lVar3;
  
  lVar2 = FUN_ffff0000be042b14();
  iVar1 = FUN_ffff0000be042d60(param_1);
  lVar3 = 0xffffcd7b0000;
  if (iVar1 != 0) {
    lVar3 = 0xffffcd3b0000;
  }
  iVar1 = FUN_ffff0000be042d60(param_1);
  lVar3 = (lVar3 + lVar2 & 0xffffffff0000U) + (ulong)(uint)(iVar1 << 0x16);
  *(undefined4 *)(lVar3 + -0xffffcd7afffc) = 0;
  *(undefined4 *)(lVar3 + -0xffffcd7affe0) = 0;
  *(undefined4 *)(lVar3 + -0xffffcd7afff8) = 0;
  return 0;
}


