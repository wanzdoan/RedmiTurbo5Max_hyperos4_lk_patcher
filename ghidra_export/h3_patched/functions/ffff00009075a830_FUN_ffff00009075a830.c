// ffff00009075a830  size=168  FUN_ffff00009075a830

undefined8 FUN_ffff00009075a830(int param_1,long param_2)

{
  ulong uVar1;
  ulong uVar2;
  uint uVar3;
  uint *puVar4;
  
  uVar2 = (ulong)(param_1 == 0x1d) << 0x10 | 0xffff000032180020;
  uVar1 = (ulong)(param_1 == 0x1d) << 0x10;
  *(undefined4 *)(uVar2 + 0x30) = 1;
  *(undefined4 *)(uVar2 + 0x34) = 0xff000000;
  *(undefined4 *)(uVar2 + 0x38) = 0xff000000;
  if (*(char *)(param_2 + 0x2d8) == '\0') {
    puVar4 = (uint *)(param_2 + 0x928);
  }
  else {
    puVar4 = (uint *)&DAT_ffff00009087b644;
    if ((param_1 != 0x17) && (puVar4 = (uint *)&DAT_ffff00009087b650, param_1 != 0x1d)) {
      uVar3 = 1;
      goto LAB_ffff00009075a8b4;
    }
  }
  uVar3 = *(uint *)(param_2 + 0x92c) & 0x1fff | (*puVar4 & 0x1fff) << 0x10;
LAB_ffff00009075a8b4:
  *(uint *)(uVar1 | 0xffff000032180030) = uVar3;
  *(undefined4 *)(uVar1 | 0xffff000032180020) = 0x147;
  return 0;
}


