
void FUN_ffff0000907098b0(void)

{
  while (DAT_ffff00009083d100 != (long *)0x0) {
    DAT_ffff00009083d100 = (long *)*DAT_ffff00009083d100;
    thunk_FUN_ffff0000907c314c();
  }
  return;
}


