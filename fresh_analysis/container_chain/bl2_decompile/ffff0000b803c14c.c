
undefined8 FUN_ffff0000b803c14c(ulong param_1)

{
  long lVar1;
  long lVar2;
  undefined *puVar3;
  ulong uVar4;
  
  puVar3 = PTR_DAT_ffff0000b8108750;
  uVar4 = -(param_1 >> 0x1f & 1) & 0xffffffe000000000 | (param_1 & 0xffffffff) << 5;
  lVar1 = *(long *)PTR_DAT_ffff0000b8108738 + uVar4;
  *(undefined4 *)(lVar1 + 0x34) = 0;
  *(undefined4 *)(lVar1 + 0x30) = 0;
  lVar2 = *(long *)puVar3 + uVar4;
  *(undefined4 *)(lVar2 + 0x34) = 0;
  *(undefined4 *)(lVar1 + 0x30) = 0;
  *(undefined4 *)(lVar1 + 0x2c) = 1;
  *(undefined4 *)(lVar1 + 0x2c) = 0;
  *(undefined4 *)(lVar2 + 0x2c) = 1;
  *(undefined4 *)(lVar2 + 0x2c) = 0;
  return 0;
}


