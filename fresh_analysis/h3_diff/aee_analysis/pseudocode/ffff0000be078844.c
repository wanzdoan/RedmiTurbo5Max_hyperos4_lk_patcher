
void FUN_ffff0000be078844(long param_1)

{
  if (param_1 != 0) {
    if (*(long *)(param_1 + 0x10) != 0) {
      FUN_ffff0000be060ae0(*(long *)(param_1 + 0x10),0,(ulong)*(uint *)(param_1 + 4) << 1);
      if (*(long *)(param_1 + 0x10) != 0) {
        thunk_FUN_ffff0000be09937c();
      }
      *(undefined8 *)(param_1 + 0x10) = 0;
    }
    *(undefined8 *)(param_1 + 4) = 0;
  }
  return;
}


