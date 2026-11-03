
void FUN_ffff000090709968(void)

{
  long *plVar1;
  
  while (plVar1 = DAT_ffff00009083d108, DAT_ffff00009083d108 != (long *)0x0) {
    if (DAT_ffff00009083d108[1] != 0) {
      thunk_FUN_ffff0000907c314c();
    }
    if (plVar1[2] != 0) {
      thunk_FUN_ffff0000907c314c();
    }
    DAT_ffff00009083d108 = (long *)*plVar1;
    thunk_FUN_ffff0000907c314c(plVar1);
  }
  return;
}


