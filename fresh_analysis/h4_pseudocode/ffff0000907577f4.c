
undefined8 FUN_ffff0000907577f4(int param_1,long param_2)

{
  long lVar1;
  int iVar2;
  uint uVar3;
  int *piVar4;
  
  if (*(char *)(param_2 + 0x2d8) == '\0') {
    piVar4 = (int *)(param_2 + 0x928);
  }
  else if (param_1 == 0x5a) {
    piVar4 = &DAT_ffff00009087b6b4;
  }
  else if (param_1 == 0x5b) {
    piVar4 = (int *)(param_2 + 0x2c8);
  }
  else if (param_1 == 0x5c) {
    piVar4 = &DAT_ffff00009087b6c0;
  }
  else {
    piVar4 = (int *)(param_2 + 0x2d0);
  }
  iVar2 = *piVar4;
  uVar3 = *(uint *)(param_2 + 0x92c);
  lVar1 = (ulong)((param_1 - 0x5bU & 0xfffffffd) == 0) * 0x10000;
  *(undefined4 *)(lVar1 + -0xffffcdf30000) = 1;
  *(uint *)(lVar1 + -0xffffcdf2ffd0) = uVar3 | iVar2 << 0x10;
  *(undefined4 *)(lVar1 + -0xffffcdf2ffe0) = 1;
  return 0;
}


