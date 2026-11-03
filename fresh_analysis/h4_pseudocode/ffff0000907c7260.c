
undefined8 FUN_ffff0000907c7260(undefined2 *param_1,undefined2 *param_2)

{
  long lVar1;
  ulong uVar2;
  ulong uVar3;
  long lVar4;
  
  if (param_1 != param_2) {
    if (*(long *)(param_2 + 8) == 0) {
      if (param_1 != (undefined2 *)0x0) {
        if (*(long *)(param_1 + 8) != 0) {
          FUN_ffff0000907cca9c(*(long *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
          thunk_FUN_ffff0000907c314c(*(undefined8 *)(param_1 + 8));
        }
        *(undefined8 *)(param_1 + 4) = 0;
        *(undefined8 *)(param_1 + 8) = 0;
        *param_1 = 1;
        return 0;
      }
    }
    else {
      uVar3 = *(ulong *)(param_2 + 4);
      lVar1 = *(ulong *)(param_2 + 4) * -4;
      do {
        lVar4 = lVar1;
        uVar2 = uVar3;
        if (uVar2 - 1 == 0) {
          uVar3 = *(ulong *)(param_1 + 4);
          *param_1 = *param_2;
          if (uVar3 == 0) goto LAB_ffff0000907c7344;
          goto LAB_ffff0000907c7324;
        }
        uVar3 = uVar2 - 1;
        lVar1 = lVar4 + 4;
      } while (*(int *)(*(long *)(param_2 + 8) + -4 + uVar2 * 4) == 0);
      uVar3 = *(ulong *)(param_1 + 4);
      *param_1 = *param_2;
      if (uVar3 < uVar2) {
        if (10000 < uVar2) {
          return 0xfffffff0;
        }
LAB_ffff0000907c7344:
        lVar1 = FUN_ffff00009077d5a8(uVar2,4);
        if (lVar1 == 0) {
          return 0xfffffff0;
        }
        if (*(long *)(param_1 + 8) != 0) {
          FUN_ffff00009077eb20(lVar1,*(long *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
          FUN_ffff0000907cca9c(*(undefined8 *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
          thunk_FUN_ffff0000907c314c(*(undefined8 *)(param_1 + 8));
        }
        *(ulong *)(param_1 + 4) = uVar2;
        *(long *)(param_1 + 8) = lVar1;
      }
      else {
LAB_ffff0000907c7324:
        FUN_ffff00009077ee50(*(long *)(param_1 + 8) - lVar4,0,(uVar3 - uVar2) * 4);
        lVar1 = *(long *)(param_1 + 8);
      }
      FUN_ffff00009077eb20(lVar1,*(undefined8 *)(param_2 + 8),-lVar4);
    }
  }
  return 0;
}


