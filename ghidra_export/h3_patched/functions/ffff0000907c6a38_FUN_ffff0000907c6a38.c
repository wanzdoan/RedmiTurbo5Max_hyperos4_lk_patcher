// ffff0000907c6a38  size=160  FUN_ffff0000907c6a38

undefined8 FUN_ffff0000907c6a38(long param_1,ulong param_2)

{
  long lVar1;
  
  if (param_2 < 0x2711) {
    if (param_2 <= *(ulong *)(param_1 + 8)) {
      return 0;
    }
    lVar1 = FUN_ffff00009077d2e0(param_2,4);
    if (lVar1 != 0) {
      if (*(long *)(param_1 + 0x10) != 0) {
        FUN_ffff00009077e858(lVar1,*(long *)(param_1 + 0x10),*(long *)(param_1 + 8) << 2);
        FUN_ffff0000907cc428(*(undefined8 *)(param_1 + 0x10),*(long *)(param_1 + 8) << 2);
        thunk_FUN_ffff0000907c2ad8(*(undefined8 *)(param_1 + 0x10));
      }
      *(ulong *)(param_1 + 8) = param_2;
      *(long *)(param_1 + 0x10) = lVar1;
      return 0;
    }
  }
  return 0xfffffff0;
}


