
void FUN_ffff0000be09bd08(undefined2 *param_1)

{
  if (param_1 != (undefined2 *)0x0) {
    if (*(long *)(param_1 + 8) != 0) {
      FUN_ffff0000be0a174c(*(long *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
      thunk_FUN_ffff0000be09937c(*(undefined8 *)(param_1 + 8));
    }
    *(undefined8 *)(param_1 + 4) = 0;
    *(undefined8 *)(param_1 + 8) = 0;
    *param_1 = 1;
  }
  return;
}


