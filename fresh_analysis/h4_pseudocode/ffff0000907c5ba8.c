
undefined8 FUN_ffff0000907c5ba8(undefined8 param_1,undefined1 *param_2)

{
  int iVar1;
  int local_4;
  
  local_4 = 1;
  if (param_2 != (undefined1 *)0x0) {
    *param_2 = 0;
    iVar1 = thunk_FUN_ffff0000907b65c4(&local_4);
    if (iVar1 == 0) {
      if (local_4 != 0) {
        return 0;
      }
      *param_2 = 1;
      return 0;
    }
  }
  return 2;
}


