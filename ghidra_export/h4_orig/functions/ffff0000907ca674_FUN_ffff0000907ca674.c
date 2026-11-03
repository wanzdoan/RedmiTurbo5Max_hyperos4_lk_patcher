// ffff0000907ca674  size=8  FUN_ffff0000907ca674

void FUN_ffff0000907ca674(long param_1)

{
  long lVar1;
  long lVar2;
  ulong uVar3;
  ulong uVar4;
  
  if (param_1 != 0) {
    if (*(int *)(param_1 + 0xc0) != 1) {
      FUN_ffff0000907c7058(param_1 + 8);
      FUN_ffff0000907c7058(param_1 + 0x20);
      FUN_ffff0000907c7058(param_1 + 0x38);
      FUN_ffff0000907c7058(param_1 + 0x50);
      FUN_ffff0000907c7058(param_1 + 0x68);
      FUN_ffff0000907c7058(param_1 + 0x80);
      FUN_ffff0000907c7058(param_1 + 0x98);
    }
    lVar2 = *(long *)(param_1 + 0xe8);
    if (lVar2 != 0) {
      uVar3 = *(ulong *)(param_1 + 0xf0);
      if (uVar3 != 0) {
        lVar2 = 0;
        uVar4 = 0;
        do {
          if (*(long *)(param_1 + 0xe8) != 0) {
            lVar1 = *(long *)(param_1 + 0xe8) + lVar2;
            FUN_ffff0000907c7058(lVar1);
            FUN_ffff0000907c7058(lVar1 + 0x18);
            FUN_ffff0000907c7058(lVar1 + 0x30);
            uVar3 = *(ulong *)(param_1 + 0xf0);
          }
          uVar4 = uVar4 + 1;
          lVar2 = lVar2 + 0x48;
        } while (uVar4 < uVar3);
        lVar2 = *(long *)(param_1 + 0xe8);
      }
      thunk_FUN_ffff0000907c314c(lVar2);
    }
    FUN_ffff0000907cca9c(param_1,0xf8);
    return;
  }
  return;
}


