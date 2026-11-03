// ffff000090758e88  size=468  FUN_ffff000090758e88

undefined8 FUN_ffff000090758e88(uint param_1,int param_2)

{
  uint uVar1;
  ulong uVar2;
  long lVar3;
  uint uVar4;
  undefined4 uVar5;
  
  lVar3 = 0;
  uVar2 = (ulong)param_1;
  do {
    if ((&DAT_ffff00009081f3dc)[(ulong)param_1 * 0x4b + lVar3] == param_2) {
      if (0 < (int)lVar3) {
        return 0;
      }
      break;
    }
    lVar3 = lVar3 + 1;
  } while (lVar3 != 0x4b);
  lVar3 = 0;
  do {
    if ((&DAT_ffff00009081f3dc)[uVar2 * 0x4b + lVar3] == -1) break;
    lVar3 = lVar3 + 1;
  } while (lVar3 != 0x4b);
  uVar1 = (uint)lVar3;
  uVar4 = uVar1 - 1;
  if (param_2 == 0x2d) {
    if (0x49 < uVar4) {
      return 0xffffffff;
    }
    uVar5 = 0x2a;
LAB_ffff000090758f60:
    (&DAT_ffff00009081f3dc)[uVar2 * 0x4b + (ulong)uVar4] = uVar5;
    uVar4 = uVar1;
  }
  else {
    lVar3 = 0;
    do {
      if ((&DAT_ffff00009081f3dc)[uVar2 * 0x4b + lVar3] == -1) {
        if (lVar3 == 0) goto LAB_ffff000090758f70;
        break;
      }
      lVar3 = lVar3 + 1;
    } while (lVar3 != 0x4b);
    if ((&DAT_ffff00009081f3dc)[uVar2 * 0x4b + (ulong)((int)lVar3 - 1)] == 0x2d) {
      if (uVar4 == 0) {
        return 0xffffffff;
      }
      uVar1 = uVar1 - 2;
      uVar5 = 0xffffffff;
      goto LAB_ffff000090758f60;
    }
  }
LAB_ffff000090758f70:
  (&DAT_ffff00009081f3dc)[uVar2 * 0x4b + (ulong)uVar4] = param_2;
  if (param_1 != 7) goto LAB_ffff000090759040;
  lVar3 = 0;
  do {
    if ((&DAT_ffff00009081f3dc)[lVar3] == param_2) {
      if (0 < (int)lVar3) goto LAB_ffff000090759040;
      break;
    }
    lVar3 = lVar3 + 1;
  } while (lVar3 != 0x4b);
  lVar3 = 0;
  do {
    if ((&DAT_ffff00009081f3dc)[lVar3] == -1) break;
    lVar3 = lVar3 + 1;
  } while (lVar3 != 0x4b);
  uVar1 = (uint)lVar3;
  uVar4 = uVar1 - 1;
  if (param_2 == 0x2d) {
    if (0x49 < uVar4) goto LAB_ffff000090759040;
    uVar5 = 0x2a;
LAB_ffff00009075902c:
    (&DAT_ffff00009081f3dc)[uVar4] = uVar5;
    uVar4 = uVar1;
  }
  else {
    lVar3 = 0;
    do {
      if ((&DAT_ffff00009081f3dc)[lVar3] == -1) {
        if (lVar3 == 0) goto LAB_ffff000090759034;
        break;
      }
      lVar3 = lVar3 + 1;
    } while (lVar3 != 0x4b);
    if ((&DAT_ffff00009081f3dc)[(int)lVar3 - 1] == 0x2d) {
      if (uVar4 == 0) goto LAB_ffff000090759040;
      uVar1 = uVar1 - 2;
      uVar5 = 0xffffffff;
      goto LAB_ffff00009075902c;
    }
  }
LAB_ffff000090759034:
  (&DAT_ffff00009081f3dc)[uVar4] = param_2;
  FUN_ffff00009075905c(0);
LAB_ffff000090759040:
  FUN_ffff00009075905c(param_1);
  return 0;
}


