// ffff0000907b6e94  size=152  FUN_ffff0000907b6e94

undefined8 FUN_ffff0000907b6e94(int param_1,undefined8 *param_2,undefined4 *param_3)

{
  undefined4 uVar1;
  uint *puVar2;
  
  puVar2 = (uint *)*param_2;
  if (((*puVar2 & 0xffffff) != 0x4d4d4d) || (*(short *)((long)puVar2 + 6) != 0)) {
    return 0x10a006;
  }
  if (*(char *)((long)puVar2 + 0x1a) != '\x0e') {
    if (param_1 == 0) {
      if (param_3 == (undefined4 *)0x0) {
        return 0;
      }
      uVar1 = 0x800;
      goto LAB_ffff0000907b6f1c;
    }
    if (param_1 != 1) {
      uVar1 = 0;
      if (param_3 == (undefined4 *)0x0) {
        return 0;
      }
      goto LAB_ffff0000907b6f1c;
    }
  }
  if (param_3 == (undefined4 *)0x0) {
    return 0;
  }
  uVar1 = 0x1000;
LAB_ffff0000907b6f1c:
  *param_3 = uVar1;
  return 0;
}


