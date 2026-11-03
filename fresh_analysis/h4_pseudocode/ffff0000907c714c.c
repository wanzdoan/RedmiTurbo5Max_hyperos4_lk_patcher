
undefined8 FUN_ffff0000907c714c(long param_1,ulong param_2)

{
  ulong uVar1;
  long lVar2;
  ulong uVar3;
  
  if (param_2 < 0x2711) {
    uVar3 = *(ulong *)(param_1 + 8);
    if (param_2 <= uVar3 && uVar3 != param_2) {
      do {
        uVar1 = uVar3;
        uVar3 = uVar1 - 1;
        if (uVar3 == 0) break;
      } while (*(int *)(*(long *)(param_1 + 0x10) + uVar3 * 4) == 0);
      uVar3 = param_2;
      if (param_2 < uVar1) {
        uVar3 = uVar1;
      }
      lVar2 = FUN_ffff00009077d5a8(param_2,4);
      if (lVar2 != 0) {
        if (*(long *)(param_1 + 0x10) != 0) {
          FUN_ffff00009077eb20(lVar2,*(long *)(param_1 + 0x10),uVar3 << 2);
          FUN_ffff0000907cca9c(*(undefined8 *)(param_1 + 0x10),*(long *)(param_1 + 8) << 2);
          thunk_FUN_ffff0000907c314c(*(undefined8 *)(param_1 + 0x10));
        }
        *(ulong *)(param_1 + 8) = uVar3;
        *(long *)(param_1 + 0x10) = lVar2;
        return 0;
      }
    }
    else {
      if (param_2 <= uVar3) {
        return 0;
      }
      lVar2 = FUN_ffff00009077d5a8(param_2,4);
      if (lVar2 != 0) {
        if (*(long *)(param_1 + 0x10) != 0) {
          FUN_ffff00009077eb20(lVar2,*(long *)(param_1 + 0x10),*(long *)(param_1 + 8) << 2);
          FUN_ffff0000907cca9c(*(undefined8 *)(param_1 + 0x10),*(long *)(param_1 + 8) << 2);
          thunk_FUN_ffff0000907c314c(*(undefined8 *)(param_1 + 0x10));
        }
        *(ulong *)(param_1 + 8) = param_2;
        *(long *)(param_1 + 0x10) = lVar2;
        return 0;
      }
    }
  }
  return 0xfffffff0;
}


