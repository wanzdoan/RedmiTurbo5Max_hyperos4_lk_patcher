// ffff00009079c184  size=80  FUN_ffff00009079c184

void FUN_ffff00009079c184(long param_1)

{
  if (*(long *)(param_1 + 0x10) != 0) {
    FUN_ffff00009077ee50(*(long *)(param_1 + 0x10),0,(ulong)*(uint *)(param_1 + 4) << 1);
    if (*(long *)(param_1 + 0x10) != 0) {
      thunk_FUN_ffff0000907c314c();
    }
    *(undefined8 *)(param_1 + 0x10) = 0;
  }
  *(undefined8 *)(param_1 + 4) = 0;
  return;
}


