
void FUN_ffff0000be09937c(long param_1)

{
  long lVar1;
  
  if (param_1 != 0) {
    lVar1 = *(long *)(param_1 + -0x10);
    *(undefined8 *)(lVar1 + 0x10) = *(undefined8 *)(param_1 + -8);
    FUN_ffff0000be0993a0(lVar1);
    return;
  }
  return;
}


