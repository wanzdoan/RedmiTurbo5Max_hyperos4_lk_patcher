
void FUN_ffff0000be0133fc(void)

{
  while (DAT_ffff0000be107950 != (long *)0x0) {
    DAT_ffff0000be107950 = (long *)*DAT_ffff0000be107950;
    thunk_FUN_ffff0000be09937c();
  }
  return;
}


