
undefined8 * FUN_ffff0000be05d2b0(ulong param_1)

{
  long lVar1;
  
  if (0xfffeffffffffffff < param_1) {
    return &DAT_ffff0000be127118;
  }
  if (param_1 - 0x1000000 < 0xfffffe000000) {
    lVar1 = tpidr_el1;
    return *(undefined8 **)(lVar1 + 0x38);
  }
  return (undefined8 *)0x0;
}


