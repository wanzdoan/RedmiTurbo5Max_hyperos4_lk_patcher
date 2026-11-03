
uint FUN_ffff0000be0977f8(undefined8 *param_1)

{
  uint uVar1;
  uint *puVar2;
  int local_4;
  
  uVar1 = 0;
  if (param_1 != (undefined8 *)0x0) {
    if (*(int *)*param_1 == 0x1000000) {
      local_4 = 0;
      puVar2 = (uint *)FUN_ffff0000be0976e4(param_1,s_phandle_ffff0000be0bc639,&local_4);
      if ((puVar2 == (uint *)0x0) || (local_4 != 4)) {
        puVar2 = (uint *)FUN_ffff0000be0976e4(param_1,s_linux_phandle_ffff0000be0a875b,&local_4);
        if (puVar2 == (uint *)0x0) {
          return 0;
        }
        if (local_4 != 4) {
          return 0;
        }
      }
      uVar1 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
      uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    else {
      uVar1 = 0;
    }
  }
  return uVar1;
}


