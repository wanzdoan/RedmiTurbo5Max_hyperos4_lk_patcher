// ffff0000907bf8f4  size=264  FUN_ffff0000907bf8f4

void FUN_ffff0000907bf8f4(long *param_1)

{
  long lVar1;
  ulong uVar2;
  long lVar3;
  
  if (*param_1 != 0) {
    FUN_ffff0000907c09a0();
  }
  if (param_1[5] != 0) {
    FUN_ffff0000907c09a0();
  }
  lVar1 = param_1[1];
  if (lVar1 != 0) {
    if (param_1[2] != 0) {
      lVar1 = 0;
      uVar2 = 0;
      do {
        lVar3 = param_1[1];
        if (*(long *)(lVar3 + lVar1) != 0) {
          FUN_ffff0000907c09a0();
        }
        if (*(long *)(lVar3 + lVar1 + 8) != 0) {
          FUN_ffff0000907c09a0();
        }
        uVar2 = uVar2 + 1;
        lVar1 = lVar1 + 0x20;
      } while (uVar2 < (ulong)param_1[2]);
      lVar1 = param_1[1];
    }
    FUN_ffff0000907c09a0(lVar1);
  }
  lVar1 = param_1[3];
  if (lVar1 != 0) {
    if (param_1[4] != 0) {
      lVar1 = 0;
      uVar2 = 0;
      do {
        lVar3 = param_1[3];
        if (*(long *)(lVar3 + lVar1) != 0) {
          FUN_ffff0000907c09a0();
        }
        lVar3 = lVar3 + lVar1;
        if ((*(long *)(lVar3 + 8) != 0) && (*(char *)(lVar3 + 0x18) == '\0')) {
          FUN_ffff0000907c09a0();
        }
        uVar2 = uVar2 + 1;
        lVar1 = lVar1 + 0x20;
      } while (uVar2 < (ulong)param_1[4]);
      lVar1 = param_1[3];
    }
    FUN_ffff0000907c09a0(lVar1);
  }
  FUN_ffff0000907c09a0(param_1);
  return;
}


