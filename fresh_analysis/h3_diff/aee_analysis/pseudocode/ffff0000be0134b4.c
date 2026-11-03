
void FUN_ffff0000be0134b4(void)

{
  long *plVar1;
  
  while (plVar1 = DAT_ffff0000be107958, DAT_ffff0000be107958 != (long *)0x0) {
    if (DAT_ffff0000be107958[1] != 0) {
      thunk_FUN_ffff0000be09937c();
    }
    if (plVar1[2] != 0) {
      thunk_FUN_ffff0000be09937c();
    }
    DAT_ffff0000be107958 = (long *)*plVar1;
    thunk_FUN_ffff0000be09937c(plVar1);
  }
  return;
}


