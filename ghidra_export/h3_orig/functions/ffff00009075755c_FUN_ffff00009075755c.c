// ffff00009075755c  size=80  FUN_ffff00009075755c

undefined8 FUN_ffff00009075755c(int param_1,long param_2)

{
  long lVar1;
  int iVar2;
  uint uVar3;
  undefined8 uVar4;
  
  if (*(char *)(param_2 + 0x2d8) == '\0') {
    uVar4 = FUN_ffff0000907575ac(param_2 + 0x928);
    return uVar4;
  }
  if (param_1 == 0x5a) {
    uVar4 = FUN_ffff0000907575ac(&DAT_ffff00009087b61c);
    return uVar4;
  }
  if (param_1 == 0x5b) {
    uVar4 = FUN_ffff0000907575ac(param_2 + 0x2c8);
    return uVar4;
  }
  if (param_1 == 0x5c) {
    uVar4 = FUN_ffff0000907575ac(&DAT_ffff00009087b628);
    return uVar4;
  }
  iVar2 = *(int *)(param_2 + 0x2d0);
  uVar3 = *(uint *)(param_2 + 0x92c);
  lVar1 = (ulong)((param_1 - 0x5bU & 0xfffffffd) == 0) * 0x10000;
  *(undefined4 *)(lVar1 + -0xffffcdf30000) = 1;
  *(uint *)(lVar1 + -0xffffcdf2ffd0) = uVar3 | iVar2 << 0x10;
  *(undefined4 *)(lVar1 + -0xffffcdf2ffe0) = 1;
  return 0;
}


