
undefined8 FUN_ffff0000be0440fc(uint param_1,int param_2)

{
  uint uVar1;
  ulong uVar2;
  long lVar3;
  uint uVar4;
  undefined4 uVar5;
  
  lVar3 = 0;
  uVar2 = (ulong)param_1;
  do {
    if ((&DAT_ffff0000be0dbd1c)[(ulong)param_1 * 0x4b + lVar3] == param_2) {
      if (0 < (int)lVar3) {
        return 0;
      }
      break;
    }
    lVar3 = lVar3 + 1;
  } while (lVar3 != 0x4b);
  lVar3 = 0;
  do {
    if ((&DAT_ffff0000be0dbd1c)[uVar2 * 0x4b + lVar3] == -1) break;
    lVar3 = lVar3 + 1;
  } while (lVar3 != 0x4b);
  uVar1 = (uint)lVar3;
  uVar4 = uVar1 - 1;
  if (param_2 == 0x2d) {
    if (0x49 < uVar4) {
      return 0xffffffff;
    }
    uVar5 = 0x2a;
LAB_ffff0000be0441d4:
    (&DAT_ffff0000be0dbd1c)[uVar2 * 0x4b + (ulong)uVar4] = uVar5;
    uVar4 = uVar1;
  }
  else {
    lVar3 = 0;
    do {
      if ((&DAT_ffff0000be0dbd1c)[uVar2 * 0x4b + lVar3] == -1) {
        if (lVar3 == 0) goto LAB_ffff0000be0441e4;
        break;
      }
      lVar3 = lVar3 + 1;
    } while (lVar3 != 0x4b);
    if ((&DAT_ffff0000be0dbd1c)[uVar2 * 0x4b + (ulong)((int)lVar3 - 1)] == 0x2d) {
      if (uVar4 == 0) {
        return 0xffffffff;
      }
      uVar1 = uVar1 - 2;
      uVar5 = 0xffffffff;
      goto LAB_ffff0000be0441d4;
    }
  }
LAB_ffff0000be0441e4:
  (&DAT_ffff0000be0dbd1c)[uVar2 * 0x4b + (ulong)uVar4] = param_2;
  if (param_1 != 7) goto LAB_ffff0000be0442b4;
  lVar3 = 0;
  do {
    if ((&DAT_ffff0000be0dbd1c)[lVar3] == param_2) {
      if (0 < (int)lVar3) goto LAB_ffff0000be0442b4;
      break;
    }
    lVar3 = lVar3 + 1;
  } while (lVar3 != 0x4b);
  lVar3 = 0;
  do {
    if ((&DAT_ffff0000be0dbd1c)[lVar3] == -1) break;
    lVar3 = lVar3 + 1;
  } while (lVar3 != 0x4b);
  uVar1 = (uint)lVar3;
  uVar4 = uVar1 - 1;
  if (param_2 == 0x2d) {
    if (0x49 < uVar4) goto LAB_ffff0000be0442b4;
    uVar5 = 0x2a;
LAB_ffff0000be0442a0:
    (&DAT_ffff0000be0dbd1c)[uVar4] = uVar5;
    uVar4 = uVar1;
  }
  else {
    lVar3 = 0;
    do {
      if ((&DAT_ffff0000be0dbd1c)[lVar3] == -1) {
        if (lVar3 == 0) goto LAB_ffff0000be0442a8;
        break;
      }
      lVar3 = lVar3 + 1;
    } while (lVar3 != 0x4b);
    if ((&DAT_ffff0000be0dbd1c)[(int)lVar3 - 1] == 0x2d) {
      if (uVar4 == 0) goto LAB_ffff0000be0442b4;
      uVar1 = uVar1 - 2;
      uVar5 = 0xffffffff;
      goto LAB_ffff0000be0442a0;
    }
  }
LAB_ffff0000be0442a8:
  (&DAT_ffff0000be0dbd1c)[uVar4] = param_2;
  FUN_ffff0000be0442d0(0);
LAB_ffff0000be0442b4:
  FUN_ffff0000be0442d0(param_1);
  return 0;
}


