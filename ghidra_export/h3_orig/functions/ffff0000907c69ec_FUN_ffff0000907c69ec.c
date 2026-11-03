// ffff0000907c69ec  size=76  FUN_ffff0000907c69ec

void FUN_ffff0000907c69ec(undefined2 *param_1)

{
  if (*(long *)(param_1 + 8) != 0) {
    FUN_ffff0000907cc428(*(long *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
    thunk_FUN_ffff0000907c2ad8(*(undefined8 *)(param_1 + 8));
  }
  *(undefined8 *)(param_1 + 4) = 0;
  *(undefined8 *)(param_1 + 8) = 0;
  *param_1 = 1;
  return;
}


