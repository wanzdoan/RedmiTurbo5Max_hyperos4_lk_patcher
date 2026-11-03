// ffff0000907099dc  size=92  FUN_ffff0000907099dc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907099dc(void)

{
  long *plVar1;
  
  while (plVar1 = _DAT_ffff00009083d168, _DAT_ffff00009083d168 != (long *)0x0) {
    if (_DAT_ffff00009083d168[1] != 0) {
      thunk_FUN_ffff0000907c2ad8();
    }
    if (plVar1[2] != 0) {
      thunk_FUN_ffff0000907c2ad8();
    }
    _DAT_ffff00009083d168 = (long *)*plVar1;
    thunk_FUN_ffff0000907c2ad8(plVar1);
  }
  return;
}


