// ffff00009075762c  size=264  FUN_ffff00009075762c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009075762c(int param_1,long param_2)

{
  uint uVar1;
  undefined4 *puVar2;
  
  if (*(int *)(param_2 + 0xc) != 0) {
    if ((*(char *)(param_2 + 0x2d8) == '\0') || (*(char *)(param_2 + 0x990) == '\0')) {
      uVar1 = *(uint *)(param_2 + 0x92c) | *(int *)(param_2 + 0x928) << 0x10;
      puVar2 = (undefined4 *)((ulong)(param_1 == 0x27) << 0x10 | 0xffff0000321e0100);
      puVar2[8] = uVar1;
      puVar2[10] = uVar1;
      puVar2[9] = 0;
      puVar2[-0x40] = 0;
      puVar2[4] = 1;
    }
    else {
      if (param_1 == 0x27) {
        puVar2 = (undefined4 *)&DAT_ffff0000321f0100;
        _DAT_ffff0000321f0120 = *(uint *)(param_2 + 0x92c) | _DAT_ffff00009087b610 << 0x10;
        _DAT_ffff0000321f0124 = _DAT_ffff00009087b618 << 0x10;
        _DAT_ffff0000321f0128 =
             *(uint *)(param_2 + 0x92c) | (_DAT_ffff00009087b610 - _DAT_ffff00009087b618) * 0x10000;
      }
      else {
        if (param_1 != 0x26) {
          return 0;
        }
        _DAT_ffff0000321e0120 = *(uint *)(param_2 + 0x92c) | _DAT_ffff00009087b604 << 0x10;
        puVar2 = (undefined4 *)&DAT_ffff0000321e0100;
        _DAT_ffff0000321e0128 =
             *(uint *)(param_2 + 0x92c) | (_DAT_ffff00009087b604 - _DAT_ffff00009087b60c) * 0x10000;
        _DAT_ffff0000321e0124 = 0;
      }
      puVar2[-0x40] = 0;
      puVar2[4] = 1;
    }
    *puVar2 = 0xfffffffd;
  }
  return 0;
}


