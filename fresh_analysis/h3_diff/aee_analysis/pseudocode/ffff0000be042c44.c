
undefined8 FUN_ffff0000be042c44(undefined4 param_1)

{
  undefined4 *puVar1;
  int iVar2;
  int iVar3;
  long lVar4;
  ulong uVar5;
  long lVar6;
  
  lVar4 = FUN_ffff0000be042b14();
  iVar2 = FUN_ffff0000be042d60(param_1);
  lVar6 = 0xffffcd7b0000;
  if (iVar2 != 0) {
    lVar6 = 0xffffcd3b0000;
  }
  iVar2 = FUN_ffff0000be042d60(param_1);
  uVar5 = FUN_ffff0000be063c7c();
  if (((uVar5 & 1) != 0) && (iVar3 = FUN_ffff0000be065774(), iVar3 == 0xfc11)) {
    *(undefined4 *)
     (((ulong)(uint)(iVar2 << 0x16) | 0xffff000032850018) + (lVar6 + lVar4 & 0xffffffff0000U)) =
         0xffff7f00;
  }
  puVar1 = (undefined4 *)
           ((lVar6 + lVar4 & 0xffffffff0000U) + ((ulong)(uint)(iVar2 << 0x16) - 0xffffcd7afffc));
  puVar1[7] = puVar1[7] | 1;
  *puVar1 = 0;
  puVar1[0x7c] = puVar1[0x7c] | 0x10000000;
  puVar1[0x7c] = puVar1[0x7c] | 0x40000000;
  puVar1[0x7c] = puVar1[0x7c] | 0x80000000;
  puVar1[0xa7] = puVar1[0xa7] | 8;
  puVar1[4] = puVar1[4] | 1;
  puVar1[4] = puVar1[4] | 0x2000000;
  puVar1[4] = puVar1[4] | 0x1000000;
  puVar1[7] = puVar1[7] | 0x400000;
  return 0;
}


