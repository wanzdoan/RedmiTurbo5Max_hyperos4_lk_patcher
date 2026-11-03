
undefined8 FUN_ffff0000b803c6d4(int param_1,long param_2)

{
  ulong uVar1;
  ulong uVar2;
  uint uVar3;
  uint *puVar4;
  
  if (*(int *)(param_2 + 0xc) == 0) {
    return 0;
  }
  uVar2 = (ulong)(param_1 == 0x1d) << 0x10 | 0xffff000032180020;
  uVar1 = (ulong)(param_1 == 0x1d) << 0x10;
  *(undefined4 *)(uVar2 + 0x30) = 1;
  *(undefined4 *)(uVar2 + 0x34) = 0xff000000;
  *(undefined4 *)(uVar2 + 0x38) = 0xff000000;
  if (*(char *)(param_2 + 0x2d8) == '\0') {
    puVar4 = (uint *)(param_2 + 0x928);
  }
  else {
    puVar4 = (uint *)0xffff0000b812901c;
    if ((param_1 != 0x17) && (puVar4 = &DAT_ffff0000b8129028, param_1 != 0x1d)) {
      uVar3 = 1;
      goto LAB_ffff0000b803c764;
    }
  }
  uVar3 = *(uint *)(param_2 + 0x92c) & 0x1fff | (*puVar4 & 0x1fff) << 0x10;
LAB_ffff0000b803c764:
  *(uint *)(uVar1 | 0xffff000032180030) = uVar3;
  *(undefined4 *)(uVar1 | 0xffff000032180020) = 0x147;
  return 0;
}


