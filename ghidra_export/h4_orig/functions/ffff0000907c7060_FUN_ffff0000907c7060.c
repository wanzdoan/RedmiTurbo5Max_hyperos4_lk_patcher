// ffff0000907c7060  size=76  FUN_ffff0000907c7060

void FUN_ffff0000907c7060(undefined2 *param_1)

{
  if (*(long *)(param_1 + 8) != 0) {
    FUN_ffff0000907cca9c(*(long *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
    thunk_FUN_ffff0000907c314c(*(undefined8 *)(param_1 + 8));
  }
  *(undefined8 *)(param_1 + 4) = 0;
  *(undefined8 *)(param_1 + 8) = 0;
  *param_1 = 1;
  return;
}


