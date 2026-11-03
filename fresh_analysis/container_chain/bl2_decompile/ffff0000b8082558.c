
void FUN_ffff0000b8082558(long param_1)

{
  long lVar1;
  
  if (param_1 != 0) {
    lVar1 = *(long *)(param_1 + -0x10);
    *(undefined8 *)(lVar1 + 0x10) = *(undefined8 *)(param_1 + -8);
    FUN_ffff0000b808257c(lVar1);
    return;
  }
  return;
}


