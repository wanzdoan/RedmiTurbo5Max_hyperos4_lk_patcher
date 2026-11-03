
undefined8 FUN_ffff0000be045568(ulong param_1,int param_2)

{
  undefined *puVar1;
  undefined4 *puVar2;
  ulong uVar3;
  long lVar4;
  
  puVar1 = PTR_DAT_ffff0000be0f5650;
  puVar2 = *(undefined4 **)PTR_DAT_ffff0000be0f5638;
  if (param_2 == 0x12) {
    uVar3 = -(param_1 >> 0x1f & 1) & 0xffffffe000000000 | (param_1 & 0xffffffff) << 5;
    *(undefined4 *)((long)puVar2 + uVar3 + 0x20) = 1;
    lVar4 = *(long *)puVar1;
    *(undefined4 *)(lVar4 + uVar3 + 0x20) = 1;
  }
  else {
    lVar4 = *(long *)PTR_DAT_ffff0000be0f5650;
    uVar3 = (ulong)((int)param_1 << 5);
  }
  puVar1 = PTR_DAT_ffff0000be0f5648;
  *(undefined4 *)(*(long *)PTR_DAT_ffff0000be0f5640 + uVar3 + 0x20) = 1;
  *(undefined4 *)((long)puVar2 + uVar3 + 0x20) = 1;
  *puVar2 = 1;
  *(undefined4 *)(*(long *)puVar1 + uVar3 + 0x20) = 1;
  *(undefined4 *)(lVar4 + uVar3 + 0x20) = 1;
  return 0;
}


