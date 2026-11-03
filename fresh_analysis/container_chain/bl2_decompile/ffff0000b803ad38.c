
undefined8 FUN_ffff0000b803ad38(uint param_1,int param_2)

{
  uint uVar1;
  ulong uVar2;
  long lVar3;
  uint uVar4;
  undefined4 uVar5;
  
  lVar3 = 0;
  uVar2 = (ulong)param_1;
  do {
    if ((&DAT_ffff0000b80eec4c)[(ulong)param_1 * 0x4b + lVar3] == param_2) {
      if (0 < (int)lVar3) {
        return 0;
      }
      break;
    }
    lVar3 = lVar3 + 1;
  } while (lVar3 != 0x4b);
  lVar3 = 0;
  do {
    if ((&DAT_ffff0000b80eec4c)[uVar2 * 0x4b + lVar3] == -1) break;
    lVar3 = lVar3 + 1;
  } while (lVar3 != 0x4b);
  uVar1 = (uint)lVar3;
  uVar4 = uVar1 - 1;
  if (param_2 == 0x2d) {
    if (0x49 < uVar4) {
      return 0xffffffff;
    }
    uVar5 = 0x2a;
LAB_ffff0000b803ae10:
    (&DAT_ffff0000b80eec4c)[uVar2 * 0x4b + (ulong)uVar4] = uVar5;
    uVar4 = uVar1;
  }
  else {
    lVar3 = 0;
    do {
      if ((&DAT_ffff0000b80eec4c)[uVar2 * 0x4b + lVar3] == -1) {
        if (lVar3 == 0) goto LAB_ffff0000b803ae20;
        break;
      }
      lVar3 = lVar3 + 1;
    } while (lVar3 != 0x4b);
    if ((&DAT_ffff0000b80eec4c)[uVar2 * 0x4b + (ulong)((int)lVar3 - 1)] == 0x2d) {
      if (uVar4 == 0) {
        return 0xffffffff;
      }
      uVar1 = uVar1 - 2;
      uVar5 = 0xffffffff;
      goto LAB_ffff0000b803ae10;
    }
  }
LAB_ffff0000b803ae20:
  (&DAT_ffff0000b80eec4c)[uVar2 * 0x4b + (ulong)uVar4] = param_2;
  if (param_1 != 7) goto LAB_ffff0000b803aef0;
  lVar3 = 0;
  do {
    if ((&DAT_ffff0000b80eec4c)[lVar3] == param_2) {
      if (0 < (int)lVar3) goto LAB_ffff0000b803aef0;
      break;
    }
    lVar3 = lVar3 + 1;
  } while (lVar3 != 0x4b);
  lVar3 = 0;
  do {
    if ((&DAT_ffff0000b80eec4c)[lVar3] == -1) break;
    lVar3 = lVar3 + 1;
  } while (lVar3 != 0x4b);
  uVar1 = (uint)lVar3;
  uVar4 = uVar1 - 1;
  if (param_2 == 0x2d) {
    if (0x49 < uVar4) goto LAB_ffff0000b803aef0;
    uVar5 = 0x2a;
LAB_ffff0000b803aedc:
    (&DAT_ffff0000b80eec4c)[uVar4] = uVar5;
    uVar4 = uVar1;
  }
  else {
    lVar3 = 0;
    do {
      if ((&DAT_ffff0000b80eec4c)[lVar3] == -1) {
        if (lVar3 == 0) goto LAB_ffff0000b803aee4;
        break;
      }
      lVar3 = lVar3 + 1;
    } while (lVar3 != 0x4b);
    if ((&DAT_ffff0000b80eec4c)[(int)lVar3 - 1] == 0x2d) {
      if (uVar4 == 0) goto LAB_ffff0000b803aef0;
      uVar1 = uVar1 - 2;
      uVar5 = 0xffffffff;
      goto LAB_ffff0000b803aedc;
    }
  }
LAB_ffff0000b803aee4:
  (&DAT_ffff0000b80eec4c)[uVar4] = param_2;
  FUN_ffff0000b803af0c(0);
LAB_ffff0000b803aef0:
  FUN_ffff0000b803af0c(param_1);
  return 0;
}


